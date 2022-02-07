package com.waqar.tmdb.repositories

import com.waqar.tmdb.base.BaseRepository
import com.waqar.tmdb.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFragmentRepository @Inject constructor(var apiService: ApiService) : BaseRepository() {

    suspend fun getSearchSuggestions(searchKey: String) =
        safeApiCall {
            apiService.getSearchSuggestion(searchKey)
        }

}