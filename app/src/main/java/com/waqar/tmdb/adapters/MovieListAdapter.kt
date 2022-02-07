package com.waqar.tmdb.adapters

//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.waqar.tmdb.databinding.PopularMoviesListItemBinding
//import com.waqar.tmdb.models.popularmovies.Result
//import com.waqar.tmdb.utils.loadImage
//
//class MovieListAdapter : PagingDataAdapter<Result, MoviePosterViewHolder>(MovieDiffCallBack()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
//        return MoviePosterViewHolder(
//            PopularMoviesListItemBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
//
//        val item = getItem(position)
//        holder.binding.item = item
//
//        holder.bind(item?.posterPath)
//    }
//}
//
//class MovieDiffCallBack : DiffUtil.ItemCallback<Result>() {
//    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
//        return oldItem == newItem
//    }
//}
//
//class MoviePosterViewHolder(
//    val binding: PopularMoviesListItemBinding
//) : RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(path: String?) {
//        path?.let {
//            binding.posterImage.loadImage("https://image.tmdb.org/t/p/w500/$it")
//        }
//    }
//}