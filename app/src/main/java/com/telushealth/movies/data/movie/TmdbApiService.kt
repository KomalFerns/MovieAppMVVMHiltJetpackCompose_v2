package com.telushealth.movies.data.movie

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Retrofit service interface for TMDB API

interface TmdbApiService {
    /**
     * Discover movies with a specific person (Benedict Cumberbatch has person ID 71580)
     * Documentation: https://developers.themoviedb.org/3/discover/movie-discover
     */
    @GET("discover/movie")
    suspend fun discoverMoviesWithPerson(
        @Query("api_key") apiKey: String,
        @Query("with_people") personId: Int,
        @Query("page") page: Int = 1,
    ): MovieResponse

    /**
     * Get movie details
     * Documentation: https://developers.themoviedb.org/3/movies/get-movie-details
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Movie

    /**
     * Get similar movies
     * Documentation: https://developers.themoviedb.org/3/movies/get-similar-movies
     */
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
    ): SimilarMoviesResponse
}

