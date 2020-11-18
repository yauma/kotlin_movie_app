package com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jaimequeraltgarrigos.kotlinmovieapp.getOrAwaitValue
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.MovieRepositoryImpl
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainViewModelTest : TestCase() {
    // Subject under test
    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var repositoryImpl: MovieRepositoryImpl

    @Mock
    private lateinit var handle: SavedStateHandle

    @Before
    fun setupViewModel() {
        MockitoAnnotations.openMocks(this)
        repositoryImpl
        viewModel = MainViewModel(repositoryImpl, handle)
    }

    @Test
    fun spinner_value_should_be_false_at_the_beginning() {
        //When
        val value = viewModel.spinner.getOrAwaitValue()

        //Then
        assertThat(value, `is`(false))
    }

    @Test
    fun whenOnSnackbarShownCalled_thenValueShouldBeNull() {
        //When
        viewModel.onSnackbarShown()

        //Then
        val value = viewModel.snackbar.getOrAwaitValue()
        assertEquals(value, null)
    }
}