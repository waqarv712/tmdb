package com.waqar.tmdb.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.waqar.tmdb.R
import com.waqar.tmdb.adapters.MoviesAdapter
import com.waqar.tmdb.base.BaseFragment
import com.waqar.tmdb.databinding.HomeFragmentBinding
import com.waqar.tmdb.listeners.HomeMoviesListener
import com.waqar.tmdb.network.Resource
import com.waqar.tmdb.utils.handleApiErrorWithToast
import com.waqar.tmdb.utils.toast
import com.waqar.tmdb.viewmodels.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(), HomeMoviesListener {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter

    private lateinit var textListener: TextWatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()
        prepareRcView()
        observeMovies()

    }

    private fun observeMovies() {
        viewModel.searchHistory.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Success -> {

                    val movies = it.value.results

                    if (movies.isNullOrEmpty()) {
                        binding.eyeImg.setImageResource(R.drawable.ic_search_grey)
                    }

                    if (binding.searchBar.text.isNotEmpty()) {

                        moviesAdapter?.submitList(movies)
                    } else {
    //                        getHistory()
                    }

                    if (!movies.isNullOrEmpty()) {
                        binding.rvMovies.layoutManager?.scrollToPosition(0)
                    }


                }
                is Resource.Failure -> {
                    handleApiErrorWithToast(it)
                }
                Resource.Loading -> {

                }
            }

        }
    }

    private fun setupSearchView() {

        binding.eyeImg.setOnClickListener {
            binding.searchBar.setText("")
            binding.eyeImg.setImageResource(R.drawable.ic_search_grey)
        }

        binding.searchBar.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                Timber.i("Enter pressed")
                if (binding.searchBar.text.toString().trim().isEmpty()) {
                    toast(getString(R.string.please_enter_a_movie_nam))
                } else {
                    try {

                        val query = binding.searchBar.text.toString()
                        viewModel.search(query, query)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val imm: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    binding.searchBar.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
            }
            false
        })

        textListener = object : TextWatcher {
            private var searchFor = "" // Or view.editText.text.toString()

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()

                if (searchText.isNotEmpty()) {
                    binding.eyeImg.setImageResource(R.drawable.ic_close)
                    if (searchText != searchFor) {
                        searchFor = searchText

                        if (searchText.length > 2) {
                            viewModel.search(searchFor, searchText)
                        }
                    }
                } else {
                    binding.eyeImg.setImageResource(R.drawable.ic_search_grey)
                }
            }
        }

        binding.searchBar.addTextChangedListener(textListener)
    }

    private fun prepareRcView() {

        moviesAdapter = MoviesAdapter(this)
        binding.rvMovies.adapter = moviesAdapter

    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = HomeFragmentBinding.inflate(
        inflater,
        container,
        false
    )


    override fun onResume() {
        super.onResume()
        binding.searchBar.addTextChangedListener(textListener)
    }

    override fun onPause() {
        binding.searchBar.removeTextChangedListener(textListener)
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.autoCompleteViewJob?.cancel()
        super.onDestroy()
    }

    override fun onMovieClicked(id: Int) {

        val bundle = bundleOf("movieID" to id)
        Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

}