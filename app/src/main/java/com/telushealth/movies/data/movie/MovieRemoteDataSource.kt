package com.telushealth.movies.data.movie

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MovieRemoteDataSource"

/**
 * Remote data source for movie data from TMDB API
 */
@Singleton
class MovieRemoteDataSource @Inject constructor(
    private val apiService: TmdbApiService,
    private val apiKey: String,
) {
    //Fetches movies Benedict Cumberbatch person ID (71580)

    suspend fun getMoviesWithPerson(personId: Int): Result<MovieResponse> {
        return try {
            val response = apiService.discoverMoviesWithPerson(
                apiKey = apiKey,
                personId = personId,
            )
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            Result.failure(e)
        }
    }

    //Fetches movie details by movie ID

    suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val response = apiService.getMovieDetails(movieId, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie details", e)
            Result.failure(e)
        }
    }

    //Fetches similar movies for a given movie ID

    suspend fun getSimilarMovies(movieId: Int): Result<SimilarMoviesResponse> {
        return try {
            val response = apiService.getSimilarMovies(movieId, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching similar movies", e)
            Result.failure(e)
        }
    }
}

