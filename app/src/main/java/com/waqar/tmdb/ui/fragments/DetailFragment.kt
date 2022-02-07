package com.waqar.tmdb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.waqar.tmdb.base.BaseFragment
import com.waqar.tmdb.databinding.DetailFragmentBinding
import com.waqar.tmdb.network.Resource
import com.waqar.tmdb.network.responses.MovieDetail
import com.waqar.tmdb.utils.handleApiErrorWithToast
import com.waqar.tmdb.utils.loadImage
import com.waqar.tmdb.viewmodels.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailFragmentBinding>() {

    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieID = arguments?.getInt("movieID")
        observeMoveDetail()
        viewModel.getMovieDetails(movieID)

    }

    private fun observeMoveDetail() {
        viewModel.movieDetails.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Success -> {

                    val movieDetails = it.value

                    setUpMovieDetail(movieDetails)

                }
                is Resource.Failure -> {
                    handleApiErrorWithToast(it)
                }
                Resource.Loading -> {

                }
            }

        }
    }

    private fun setUpMovieDetail(movieDetails: MovieDetail) {

        binding.movie = movieDetails
        binding.executePendingBindings()

        movieDetails.backdropPath?.let {
            binding.imgBackdrop.loadImage("https://image.tmdb.org/t/p/w500/$it")
        }

        movieDetails.posterPath?.let {
            binding.imgPoster.loadImage("https://image.tmdb.org/t/p/w500/$it")
        }

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= DetailFragmentBinding.inflate(
        inflater,
        container,
        false
    )
}