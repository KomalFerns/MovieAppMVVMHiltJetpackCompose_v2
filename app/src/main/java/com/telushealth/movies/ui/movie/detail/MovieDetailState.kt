package com.telushealth.movies.ui.movie.detail

import com.telushealth.movies.data.movie.Movie

//UI State for movie detail screen Following MVVM architecture pattern

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val similarMovies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val isLoadingSimilar: Boolean = false,
)
