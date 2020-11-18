package com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper

import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.Movie
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import javax.inject.Inject

class DBEntityMapperImpl @Inject constructor() : EntityMapper<Movie, MovieEntity> {
    override fun entityToModel(entity: Movie): MovieEntity {
        return MovieEntity(entity.movieTitle, entity.moviePosterUrl, entity.id)
    }

    override fun modelToEntity(model: MovieEntity): Movie {
        return Movie(model.title, model.backdrop_path, model.id)
    }

    fun mapFromEntityList(list: List<Movie>): List<MovieEntity> {
        return list.map {
            entityToModel(it)
        }
    }

    fun mapFromMovieList(list: List<MovieEntity>): List<Movie> {
        return list.map {
            modelToEntity(it)
        }
    }
}