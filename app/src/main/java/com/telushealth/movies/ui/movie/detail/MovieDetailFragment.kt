package com.telushealth.movies.ui.movie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint

// movie detail screen using Jetpack Compose

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by viewModel.state.collectAsState()

                // Initialize view model with movie ID
                LaunchedEffect(key1 = args.movieId) {
                    viewModel.initialize(args.movieId)
                }

                MovieDetailScreen(
                    state = state,
                    onSimilarMovieClick = { movieId, movieTitle ->
                        navigateToMovieDetail(movieId, movieTitle)
                    },
                )
            }
        }
    }

    private fun navigateToMovieDetail(movieId: Int, movieTitle: String) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToSelf(
            movieId = movieId,
            movieTitle = movieTitle,
        )
        findNavController().navigate(action)
    }
}
