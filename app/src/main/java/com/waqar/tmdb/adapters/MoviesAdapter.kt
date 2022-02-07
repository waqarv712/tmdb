package com.waqar.tmdb.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waqar.tmdb.R
import com.waqar.tmdb.databinding.MoviesListItemBinding
import com.waqar.tmdb.listeners.HomeMoviesListener
import com.waqar.tmdb.models.popularmovies.Result
import com.waqar.tmdb.utils.loadImage
import com.waqar.tmdb.utils.setFadeAnimation
import java.util.*

class MoviesAdapter(private val homeMoviesListener: HomeMoviesListener) : ListAdapter<Result, MoviesAdapter.MoviesViewHolder>(Companion) {

    inner class MoviesViewHolder(val itemBinding: MoviesListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Result) {

            movie.posterPath?.let {
                itemBinding.posterImage.loadImage("https://image.tmdb.org/t/p/w500/$it")
            }

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            if (movie.releaseDate != null) {
                if (movie.releaseDate.split("-")[0] == currentYear.toString()) {
                    itemBinding.tvDate.setTextColor(Color.RED)
                    itemBinding.tvDate.setTypeface(itemBinding.tvDate.typeface, Typeface.BOLD)
                    movie.isCurrentYear = true
                } else {
                    itemBinding.tvDate.setTextColor(Color.BLACK)
                    itemBinding.tvDate.setTypeface(itemBinding.tvDate.typeface, Typeface.NORMAL)
                    movie.isCurrentYear = false
                }
            }

        }
    }

    companion object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {

        return MoviesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.movies_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = getItem(position)

        holder.itemBinding.movie = movie
        holder.itemBinding.executePendingBindings()

        holder.bind(movie)


        holder.itemBinding.rootView.setOnClickListener {
            homeMoviesListener.onMovieClicked(movie.id!!)
        }

        holder.itemView.setFadeAnimation()
    }

}