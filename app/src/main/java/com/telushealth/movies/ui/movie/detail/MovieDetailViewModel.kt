package com.telushealth.movies.ui.movie.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telushealth.movies.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for movie detail screen (Jetpack Compose) Uses MVVM architecture pattern with StateFlow for state management

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state

    //Initialize with movie ID from navigation args

    fun initialize(movieId: Int) {
        loadMovieDetails(movieId)
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            movieRepository.getMovieDetails(movieId)
                .onSuccess { movie ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        movie = movie,
                        errorMessage = null,
                    )
                    // Load similar movies after movie details are loaded
                    loadSimilarMovies(movieId)
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load movie details",
                    )
                }
        }
    }

    private fun loadSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingSimilar = true)

            movieRepository.getSimilarMovies(movieId)
                .onSuccess { response ->
                    _state.value = _state.value.copy(
                        isLoadingSimilar = false,
                        similarMovies = response.results,
                    )
                }
                .onFailure {
                    // Don't show error for similar movies, just keep empty list
                    _state.value = _state.value.copy(isLoadingSimilar = false)
                }
        }
    }
}
