package com.example.movielando.data.remote

import com.example.movielando.data.remote.IMDBContants.Companion.apikey
import com.example.movielando.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

// Assembles search queries
interface IMDBApi {
    @GET(apikey)
    fun getMovies(/*@Query("") api_key: String,*/
        /*@Query("title_type") title_type: String,*/
        @Query("num_votes") num_votes: String,
        @Query("runtime") runtime: String,
        @Query("count") count: String,
        @Query("languages") languages: String,
        @Query("sort") sort: String): Flow<Resource<SearchResponse>>
}