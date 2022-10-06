package com.example.movielando.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movielando.data.remote.Item

@Database(
    entities = [Item::class],
    version = 2,
    exportSchema = false
)
abstract class MoviesDB: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDB? = null

        private fun buildDatabase(context: Context): MoviesDB {
            return Room
                .databaseBuilder(context, MoviesDB::class.java, "movies_database")
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // do work on first db creation
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // do work on each start
                        }
                    }
                )
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getDatabase(context: Context): MoviesDB {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }
    }
}