package com.example.movielando.data.db

import androidx.room.*
import com.example.movielando.data.remote.Item
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    // suspend functions are functions that can be paused and resumed at a later time
    // they can execute a long running operation without blocking, meaning they run in another thread

    @Insert
    suspend fun addMovie(movie: Item)

    @Update
    fun editMovie(movie: Item)

    @Delete
    suspend fun deleteMovie(movie: Item)

    @Query("SELECT * from movies")
    fun getMovies(): Flow<List<Item>>

    @Query("DELETE FROM movies")
    fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM movies WHERE id = :id)")
    fun exists(id: String): Boolean


}