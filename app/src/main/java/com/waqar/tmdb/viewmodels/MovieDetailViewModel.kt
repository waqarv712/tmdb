package com.waqar.tmdb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waqar.tmdb.network.ApiService
import com.waqar.tmdb.network.Resource
import com.waqar.tmdb.network.responses.MovieDetail
import com.waqar.tmdb.repositories.MovieDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(var apiService: ApiService, var movieDetailRepository: MovieDetailRepository) : ViewModel() {

    private val _movieDetails: MutableLiveData<Resource<MovieDetail>> =
        MutableLiveData()
    val movieDetails: LiveData<Resource<MovieDetail>>
        get() = _movieDetails


    fun getMovieDetails(id: Int?) = viewModelScope.launch {
        _movieDetails.value = Resource.Loading
        _movieDetails.value = movieDetailRepository.getMovieDetails(id)
    }

}