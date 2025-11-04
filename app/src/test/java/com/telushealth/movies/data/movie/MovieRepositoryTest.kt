package com.telushealth.movies.data.movie

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for MovieRepository
 * Tests data fetching and error handling
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    private lateinit var remoteDataSource: MovieRemoteDataSource
    private lateinit var repository: MovieRepository

    @Before
    fun setup() {
        remoteDataSource = mockk()
        repository = MovieRepository(remoteDataSource)
    }

    @Test
    fun `getBenedictCumberbatchMovies should return success result`() = runTest {
        // Given
        val movies = listOf(
            Movie(1, "Movie 1", "Overview 1", "/poster1.jpg", null, "2024-01-01"),
            Movie(2, "Movie 2", "Overview 2", "/poster2.jpg", null, "2024-01-02"),
        )
        val response = MovieResponse(page = 1, results = movies, totalPages = 1, totalResults = 2)
        coEvery {
            remoteDataSource.getMoviesWithPerson(MovieRepository.BENEDICT_CUMBERBATCH_PERSON_ID)
        } returns Result.success(response)

        // When
        val result = repository.getBenedictCumberbatchMovies()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(movies.size, result.getOrNull()?.results?.size)
    }

    @Test
    fun `getBenedictCumberbatchMovies should return failure on error`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery {
            remoteDataSource.getMoviesWithPerson(MovieRepository.BENEDICT_CUMBERBATCH_PERSON_ID)
        } returns Result.failure(exception)

        // When
        val result = repository.getBenedictCumberbatchMovies()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `getMovieDetails should return success result`() = runTest {
        // Given
        val movieId = 123
        val movie = Movie(movieId, "Test Movie", "Overview", "/poster.jpg", null, "2024-01-01")
        coEvery { remoteDataSource.getMovieDetails(movieId) } returns Result.success(movie)

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(movieId, result.getOrNull()?.id)
    }

    @Test
    fun `getSimilarMovies should return success result`() = runTest {
        // Given
        val movieId = 123
        val similarMovies = listOf(
            Movie(2, "Similar 1", "Overview", "/poster1.jpg", null, "2024-01-01"),
            Movie(3, "Similar 2", "Overview", "/poster2.jpg", null, "2024-01-02"),
        )
        val response = SimilarMoviesResponse(page = 1, results = similarMovies, totalPages = 1, totalResults = 2)
        coEvery { remoteDataSource.getSimilarMovies(movieId) } returns Result.success(response)

        // When
        val result = repository.getSimilarMovies(movieId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(similarMovies.size, result.getOrNull()?.results?.size)
    }
}

