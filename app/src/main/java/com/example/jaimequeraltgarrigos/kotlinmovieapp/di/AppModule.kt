package com.example.jaimequeraltgarrigos.kotlinmovieapp.di

import android.app.Application
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.MovieDBDataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.MovieDatabase
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.getDatabase
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MovieNetworkDataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.getNetworkService
import com.example.jaimequeraltgarrigos.kotlinmovieapp.repository.MovieRepositoryImpl
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
    fun movieNetworkDataSourceProvider(
        mainNetwork: MainNetwork,
        networkEntityMapper: NetworkEntityMapperImpl
    ): MovieNetworkDataSource =
        MovieNetworkDataSource(mainNetwork, networkEntityMapper)

    @Provides
    @Singleton
    fun movieDBDataSourceProvider(
        movieDatabase: MovieDatabase,
        dbEntityMapper: DBEntityMapperImpl
    ): MovieDBDataSource =
        MovieDBDataSource(movieDatabase.movieDao, dbEntityMapper)

    @Provides
    @Singleton
    fun movieRepositoryProvider(
        networkDataSource: MovieNetworkDataSource,
        dbDataSource: MovieDBDataSource
    ) =
        MovieRepositoryImpl(networkDataSource, dbDataSource)
}