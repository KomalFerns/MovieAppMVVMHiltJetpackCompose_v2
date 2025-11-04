package com.telushealth.movies.ui.movie.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telushealth.movies.data.movie.Movie
import com.telushealth.movies.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadMovies()
    }

    /**
     * Loads movies starring Benedict Cumberbatch
     */
    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            movieRepository.getBenedictCumberbatchMovies()
                .onSuccess { response ->
                    _movies.value = response.results
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to load movies"
                    _isLoading.value = false
                }
        }
    }

    /**
     * Retry loading movies after an error
     */
    fun retryLoadMovies() {
        loadMovies()
    }
}

