package com.telushealth.movies.data.movie

//Data model for TMDB API movie response

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int,
)

//Data model for individual movie

data class Movie(
    val id: Int,
    val original_title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val releaseDate: String?,
)


//Data model for similar movies response

data class SimilarMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int,
)

