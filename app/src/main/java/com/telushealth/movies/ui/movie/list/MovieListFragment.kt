package com.telushealth.movies.ui.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.telushealth.movies.R
import com.telushealth.movies.data.movie.Movie
import dagger.hilt.android.AndroidEntryPoint

//displaying list of movies using XML layout

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.movie_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = MovieListAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }
        recyclerView.adapter = adapter

        val loadingProgressBar = view.findViewById<android.view.View>(R.id.loading_progress_bar)
        val errorMessageText = view.findViewById<android.widget.TextView>(R.id.error_message_text)
        val retryButton = view.findViewById<android.widget.Button>(R.id.retry_button)
        val emptyStateText = view.findViewById<android.widget.TextView>(R.id.empty_state_text)

        // Observe movies
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter = MovieListAdapter(movies) { movie ->
                navigateToMovieDetail(movie)
            }
            recyclerView.adapter = adapter
            emptyStateText.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        // Observe error state
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                errorMessageText.text = errorMessage
                errorMessageText.visibility = View.VISIBLE
                retryButton.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                errorMessageText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
        }

        retryButton.setOnClickListener {
            viewModel.retryLoadMovies()
        }
    }

    //Navigate to movie detail screen (Compose screen)

    private fun navigateToMovieDetail(movie: Movie) {
        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(
            movieId = movie.id,
            movieTitle = movie.original_title,
        )
        findNavController().navigate(action)
    }
}

