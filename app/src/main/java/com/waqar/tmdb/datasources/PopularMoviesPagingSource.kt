package com.waqar.tmdb.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waqar.tmdb.models.popularmovies.Result
import com.waqar.tmdb.network.ApiService
import java.lang.Exception

class PopularMoviesPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Result>() {

    var movies: List<Result> = listOf()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {

        return try {
            val nextPage = params.key ?: 1
            val response = apiService.getPopularMovies(
                "en-US",
                nextPage
            )

            if (movies.isNullOrEmpty()) {
                movies = response.results as List<Result>
            }

            LoadResult.Page(
                data = response.results!!,
                prevKey = null,
                nextKey = if (response.results.isEmpty()) null else nextPage + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}