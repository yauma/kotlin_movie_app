package com.example.jaimequeraltgarrigos.kotlinmovieapp.di

import android.app.Application
import com.example.jaimequeraltgarrigos.kotlinmovieapp.database.MovieDatabase
import com.example.jaimequeraltgarrigos.kotlinmovieapp.database.getDatabase
import com.example.jaimequeraltgarrigos.kotlinmovieapp.network.MainNetwork
import com.example.jaimequeraltgarrigos.kotlinmovieapp.network.getNetworkService
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.MovieRepository
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.DBEntityMapperImpl
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.NetworkEntityMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application): MovieDatabase = getDatabase(app)

    @Provides
    @Singleton
    fun serviceProvider(): MainNetwork = getNetworkService()

    @Provides
    @Singleton
    fun movieRepositoryProvider(
        network: MainNetwork, db: MovieDatabase, networkEntityMapper: NetworkEntityMapperImpl,
        dbEntityMapper: DBEntityMapperImpl
    ) =
        MovieRepository(network, db.movieDao, networkEntityMapper, dbEntityMapper)
}