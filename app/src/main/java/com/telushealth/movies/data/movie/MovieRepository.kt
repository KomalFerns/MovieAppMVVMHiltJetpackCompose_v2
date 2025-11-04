package com.telushealth.movies.data.movie

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
) {
    companion object {
        const val BENEDICT_CUMBERBATCH_PERSON_ID = 71580
    }

    //Get all movies starring Benedict Cumberbatch

    suspend fun getBenedictCumberbatchMovies(): Result<MovieResponse> {
        return remoteDataSource.getMoviesWithPerson(BENEDICT_CUMBERBATCH_PERSON_ID)
    }

    //Get movie details by ID

    suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return remoteDataSource.getMovieDetails(movieId)
    }

    //Get similar movies for a given movie

    suspend fun getSimilarMovies(movieId: Int): Result<SimilarMoviesResponse> {
        return remoteDataSource.getSimilarMovies(movieId)
    }
}

