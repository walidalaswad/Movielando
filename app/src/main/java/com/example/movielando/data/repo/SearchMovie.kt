package com.example.movielando.data.repo

import com.example.movielando.data.remote.IMDBApi
import com.example.movielando.data.remote.SearchResponse
import com.example.movielando.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMovie @Inject constructor(
    private val imdbApi: IMDBApi
    ) {
    // TODO: Create a domain model and mapper to avoid using remote model at UI level
    fun search(num_votes: String, runtime: String, count: String, languages: String, sort: String): Flow<Resource<SearchResponse>> {
        return imdbApi.getMovies(num_votes = num_votes,
            runtime = runtime,
            count = count,
            languages = languages,
            sort = sort)
    }
}