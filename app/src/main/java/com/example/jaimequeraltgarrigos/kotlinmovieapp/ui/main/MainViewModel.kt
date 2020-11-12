package com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.MovieRepository
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.MoviesRefreshError
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _snackBar = MutableLiveData<String?>()

    val snackbar: LiveData<String?>
        get() = _snackBar

    val movies = repository.movies

    private val _spinner = MutableLiveData<Boolean>(false)

    val spinner: LiveData<Boolean>
        get() = _spinner

    fun onMainViewCreated() {
        refreshMovies()
    }

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the title, showing a loading spinner while it refreshes and errors via snackbar.
     */
    private fun refreshMovies() {
        launchDataLoad {
            repository.refreshMovies()
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: MoviesRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}