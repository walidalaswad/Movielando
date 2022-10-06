package com.example.movielando.di

import android.content.Context
import androidx.room.Room
import com.example.movielando.data.db.MoviesDB
import com.example.movielando.data.db.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context): MoviesDB {
        return Room.databaseBuilder(
            context,
            MoviesDB::class.java,
            "movies_db.db"
        ).build()
    }
}