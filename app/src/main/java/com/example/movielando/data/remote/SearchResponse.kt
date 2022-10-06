package com.example.movielando.data.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "results")
    val items: List<Item>,

    val totalCount: Int = items.count()
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "movies")
data class Item(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @Json(name = "image")
    @ColumnInfo(name = "movie_image")
    val image: String,
    @Json(name = "title")
    @ColumnInfo(name = "movie_title")
    val title: String,
    @Json(name = "runtimeStr")
    @ColumnInfo(name = "movie_runtime")
    val runtimeStr: String?, //
    @Json(name = "genres")
    @ColumnInfo(name = "movie_genre")
    val genres: String?,
    @Json(name = "plot")
    @ColumnInfo(name = "movie_plot")
    val plot: String?,
    @Json(name = "imDbRating")
    @ColumnInfo(name = "movie_rating")
    val imdbRating: String?,
    @Json(name = "imDbRatingVotes")
    @ColumnInfo(name = "movie_votes")
    val imdbRatingVotes: String?,
    @Json(name = "stars")
    @ColumnInfo(name = "movie_stars")
    val stars: String?,
)