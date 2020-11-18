package com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network

import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.SkipNetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w300"
private val service: MainNetwork by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(SkipNetworkInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MainNetwork::class.java)
}

fun getNetworkService() = service

/**
 * Main network interface which will fetch a new welcome title for us
 */
interface MainNetwork {
    @GET("next_title.json")
    suspend fun fetchMovies(): List<MovieNetworkEntity>
}
