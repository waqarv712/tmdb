package com.waqar.tmdb.network.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waqar.tmdb.models.popularmovies.Result

@JsonClass(generateAdapter = true)
data class PopularMovies(
    @Json(name = "page")
    val page: Int?,
    @Json(name = "results")
    val results: List<Result>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?
)