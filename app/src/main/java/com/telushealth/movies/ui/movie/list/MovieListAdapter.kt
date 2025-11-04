package com.telushealth.movies.ui.movie.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telushealth.movies.R
import com.telushealth.movies.data.movie.Movie
import com.bumptech.glide.Glide

//RecyclerView adapter for displaying movie list items

class MovieListAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit,
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImageView: ImageView = itemView.findViewById(R.id.movie_poster_image)
        val titleTextView: TextView = itemView.findViewById(R.id.movie_title_text)

        fun bind(movie: Movie, onMovieClick: (Movie) -> Unit) {
            titleTextView.text = movie.original_title
            titleTextView.contentDescription = "Movie: ${movie.original_title}"

            // Load poster image using Glide
            // TMDB image base URL: https://image.tmdb.org/t/p/w500
            val posterUrl = if (!movie.poster_path.isNullOrEmpty()) {
                "https://image.tmdb.org/t/p/w500${movie.poster_path}"

            } else {
                null
            }
            Log.d("imageurl",posterUrl +"")

            if (posterUrl != null) {
                Glide.with(itemView.context)
                    .load(posterUrl)
                    .into(posterImageView)
            }

            posterImageView.contentDescription = "Poster for ${movie.original_title}"

            itemView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_list, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], onMovieClick)
    }

    override fun getItemCount(): Int = movies.size
}

