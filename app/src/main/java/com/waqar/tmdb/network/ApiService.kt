package com.waqar.tmdb.network

import com.waqar.tmdb.network.responses.PopularMovies
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("getSolrSearchSuggestion")
    suspend fun getSearchSuggestion(
        @Query("page") page: String?
    ): PopularMovies

}