package com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper

import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.Movie
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MovieNetworkEntity
import javax.inject.Inject

class NetworkEntityMapperImpl @Inject constructor() :
    EntityMapper<MovieNetworkEntity, MovieEntity> {

    override fun entityToModel(entity: MovieNetworkEntity): MovieEntity {
        val title = entity.title ?: "No Title Found"
        val url = entity.backdrop_path ?: ""
        return MovieEntity(title, url, entity.id)
    }

    fun entityToDBModel(entity: MovieNetworkEntity): Movie {
        val title = entity.title ?: "No Title Found"
        val url = entity.backdrop_path ?: ""
        return Movie(title, url, entity.id)
    }

    override fun modelToEntity(model: MovieEntity): MovieNetworkEntity {
        return MovieNetworkEntity(model.title, model.backdrop_path, model.id)
    }
}