package com.waqar.tmdb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waqar.tmdb.PAGE_SIZE_LIMIT
import com.waqar.tmdb.datasources.PopularMoviesPagingSource
import com.waqar.tmdb.models.popularmovies.Result
import com.waqar.tmdb.network.ApiService
import com.waqar.tmdb.network.Resource
import com.waqar.tmdb.network.responses.PopularMovies
import com.waqar.tmdb.repositories.HomeFragmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(var apiService: ApiService, var homeFragmentRepository: HomeFragmentRepository) : ViewModel() {

    private val _searchHistory: MutableLiveData<Resource<PopularMovies>> =
        MutableLiveData()
    val searchHistory: LiveData<Resource<PopularMovies>>
        get() = _searchHistory

    var autoCompleteViewJob: Job? = null

    fun search(searchFor: String, searchText: String) : Job? {
        autoCompleteViewJob?.cancel()
        autoCompleteViewJob = viewModelScope.launch {
            delay(300) // time to debounce
            if (searchText == searchFor) {
                _searchHistory.value = Resource.Loading
                _searchHistory.value = homeFragmentRepository.getSearchSuggestions(searchText.replace("[;\\/:*?\"<>|&()']".toRegex()," ")) // call your api here
            }
        }

        return autoCompleteViewJob

    }


    fun getPopularMovies(): Flow<PagingData<Result>> = Pager(
        PagingConfig(pageSize = PAGE_SIZE_LIMIT)
    ) {
        PopularMoviesPagingSource(
            apiService
        )
    }.flow

}