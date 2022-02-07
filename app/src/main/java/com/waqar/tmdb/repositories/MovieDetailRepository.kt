package com.waqar.tmdb.repositories

import com.waqar.tmdb.base.BaseRepository
import com.waqar.tmdb.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailRepository @Inject constructor(var apiService: ApiService) : BaseRepository() {

    suspend fun getMovieDetails(id: Int?) =
        safeApiCall {
            apiService.getMovieDetails(id)
        }

}