package com.example.movielando.data.repo


import com.example.movielando.data.db.MoviesDB
import com.example.movielando.data.db.MoviesDao
import com.example.movielando.data.remote.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository  @Inject constructor (
    val movieDB: MoviesDB
    ) {

    suspend fun addMovie(movie: Item) {
        return movieDB.moviesDao().addMovie(movie)
    }

    //fun addMovie(movie: Item) = dao.addMovie(movie = movie)

    fun editMovie(movie: Item) = movieDB.moviesDao().editMovie(movie = movie)

    suspend fun deleteMovie(movie: Item) = movieDB.moviesDao().deleteMovie(movie = movie)

    fun deleteAll() = movieDB.moviesDao().deleteAll()

    fun exists(id: String) = movieDB.moviesDao().exists(id = id)

    fun getAllMovies(): Flow<List<Item>> = movieDB.moviesDao().getMovies()


}