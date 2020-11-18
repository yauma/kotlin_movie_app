package com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jaimequeraltgarrigos.kotlinmovieapp.MainCoroutineScopeRule
import com.example.jaimequeraltgarrigos.kotlinmovieapp.captureValues
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.FakeDataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.MainNetworkCompletableFake
import com.example.jaimequeraltgarrigos.kotlinmovieapp.getOrAwaitValue
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.MovieRepositoryImpl
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.getMoviesNetwork
import com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.viewmodel.MainViewModel
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.NetworkEntityMapperImpl
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class MainViewModelTest : TestCase() {
    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var moviesNetwork: MutableList<MovieEntity>

    // Subject under test
    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var repositoryImpl: MovieRepositoryImpl

    @Mock
    private lateinit var handle: SavedStateHandle

    @Before
    fun setupViewModel() {
        MockitoAnnotations.openMocks(this)
        moviesNetwork = getMoviesNetwork(NetworkEntityMapperImpl())
        repositoryImpl =
            MovieRepositoryImpl(FakeDataSource(moviesNetwork), FakeDataSource(moviesNetwork))
        viewModel =
            MainViewModel(
                repositoryImpl,
                handle
            )
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

    @Test
    fun whenMainViewCreatedThenSpinnerShownAndHide() {
        coroutineScope.runBlockingTest {
            val dbDataSource = FakeDataSource(moviesNetwork)
            val repositoryImpl =
                MovieRepositoryImpl(FakeDataSource(moviesNetwork), dbDataSource)
            val viewModel =
                MainViewModel(
                    repositoryImpl,
                    handle
                )
            viewModel.spinner.captureValues {
                assertEquals(values, listOf(false))
                viewModel.onMainViewCreated()
                assertEquals(values, listOf(false, true, false))
                assertEquals(dbDataSource.observeMovies().getOrAwaitValue(), moviesNetwork)
            }
        }
    }

    @Test
    fun whenMainViewCreatedThenDBisUpdated() {
        coroutineScope.runBlockingTest {
            val dbDataSource = FakeDataSource(moviesNetwork)
            val repositoryImpl =
                MovieRepositoryImpl(FakeDataSource(moviesNetwork), dbDataSource)
            val viewModel =
                MainViewModel(
                    repositoryImpl,
                    handle
                )
            viewModel.spinner.captureValues {
                viewModel.onMainViewCreated()
                assertEquals(dbDataSource.observeMovies().getOrAwaitValue(), moviesNetwork)
            }
        }
    }

    @Test
    fun whenError_itShowsErrorAndHidesSpinner() {
        coroutineScope.runBlockingTest {
            val networkCompletableFake = MainNetworkCompletableFake()
            val repositoryImpl =
                MovieRepositoryImpl(networkCompletableFake, FakeDataSource(moviesNetwork))
            val viewModel =
                MainViewModel(
                    repositoryImpl,
                    handle
                )
            viewModel.spinner.captureValues {
                viewModel.onMainViewCreated()
                assertEquals(values, listOf(false, true))
                networkCompletableFake.sendErrorToCurrentRequests(makeErrorResult("Error"))
                assertEquals(values, listOf(false, true, false))
            }
        }
    }

    @Test
    fun whenError_itShowsErrorText() = coroutineScope.runBlockingTest {
        val networkCompletableFake = MainNetworkCompletableFake()
        val repositoryImpl =
            MovieRepositoryImpl(networkCompletableFake, FakeDataSource(moviesNetwork))
        val viewModel =
            MainViewModel(
                repositoryImpl,
                handle
            )
        viewModel.onMainViewCreated()
        networkCompletableFake.sendErrorToCurrentRequests(makeErrorResult("An error"))
        assertThat(viewModel.snackbar.getOrAwaitValue()).isEqualTo("Unable to refresh movies")
        viewModel.onSnackbarShown()
        assertThat(viewModel.snackbar.getOrAwaitValue()).isEqualTo(null)
    }

    private fun makeErrorResult(result: String): HttpException {
        return HttpException(
            Response.error<String>(
                500,
                ResponseBody.create(
                    MediaType.get("application/json"),
                    "\"$result\""
                )
            )
        )
    }
}

