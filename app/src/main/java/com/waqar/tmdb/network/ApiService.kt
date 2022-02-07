package com.waqar.tmdb.network

import com.waqar.tmdb.network.responses.MovieDetail
import com.waqar.tmdb.network.responses.PopularMovies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int?
    ): PopularMovies

    @GET("search/movie")
    suspend fun getSearchSuggestion(
        @Query("query") searchKey: String?
    ): PopularMovies

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int?,
    ): MovieDetail

}