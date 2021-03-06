package com.example.jaimequeraltgarrigos.kotlinmovieapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.Movie
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import kotlinx.coroutines.CompletableDeferred

open class FakeDataSource(private var movies: MutableList<MovieEntity>? = mutableListOf()) :
    DataSource<MovieEntity> {
    private val observableMovies = MutableLiveData<List<MovieEntity>>()

    override fun observeMovies(): LiveData<List<MovieEntity>> {
        return observableMovies
    }

    override suspend fun getMovies(): List<MovieEntity> {
        movies?.let {
            return it
        }
        return mutableListOf()
    }

    override suspend fun insertAll(list: List<MovieEntity>) {
        movies?.addAll(list)
        observableMovies.value = list
    }

    override suspend fun insertMovie(movie: MovieEntity) {
        movies?.add(movie)
    }

}

class FakeDataCompletableSource : FakeDataSource() {
    private var completable = CompletableDeferred<List<MovieEntity>>()

    override suspend fun getMovies(): List<MovieEntity> = completable.await()
}

/**
 * Testing Fake for MainNetwork that lets you complete or error all current requests
 */
class MainNetworkCompletableFake() : DataSource<MovieEntity> {
    private var completable = CompletableDeferred<List<MovieEntity>>()

    fun sendCompletionToAllCurrentRequests(result: List<MovieEntity>) {
        completable.complete(result)
        completable = CompletableDeferred()
    }

    fun sendErrorToCurrentRequests(throwable: Throwable) {
        completable.completeExceptionally(throwable)
        completable = CompletableDeferred()
    }

    override fun observeMovies(): LiveData<List<MovieEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovies(): List<MovieEntity> {
        return completable.await()
    }

    override suspend fun insertAll(list: List<MovieEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovie(movie: MovieEntity) {
        TODO("Not yet implemented")
    }

}