package com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * TitleDatabase provides a reference to the dao to repositories
 */
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}

private lateinit var INSTANCE: MovieDatabase

/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): MovieDatabase {
    synchronized(MovieDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movies_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}