package com.example.movielando.data.remote

import kotlin.random.Random

// By-default search parameters for imdb-api
class IMDBContants {
    companion object {
        val baseUrl: String = "https://imdb-api.com/API/AdvancedSearch/"
        const val apikey: String = "k_f0u1sn7v?title_type=feature,tv_movie"
        /* val titleType: String = "tv_movie" */
        val numVotes: String = "1000,"
        val runtime: String = "60,"
        val count: String = "250"
    }
}