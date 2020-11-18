package com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieNetworkEntity(
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("backdrop_path")
    @Expose
    val backdrop_path: String?,
    @SerializedName("id")
    @Expose
    val id: Int
)