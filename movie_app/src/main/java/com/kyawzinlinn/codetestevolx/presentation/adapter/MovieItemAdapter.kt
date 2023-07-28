package com.kyawzinlinn.codetestevolx.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kyawzinlinn.codetestevolx.R
import com.kyawzinlinn.codetestevolx.databinding.MovieItemBinding
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import com.kyawzinlinn.codetestevolx.utils.IMG_URL_PREFIX_300

class MovieItemAdapter(private val onMovieClick: (Movie) -> Unit, private val onFavoriteClick: (String, Boolean) -> Unit): ListAdapter<Movie,MovieItemAdapter.ViewHolder>(DiffCallBack) {

    class ViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie, onFavoriteClick: (String, Boolean) -> Unit){
            var isFavorite = movie.isFavorite

            if (isFavorite) binding.ivBookmark.load(R.drawable.round_bookmark_24)
            else binding.ivBookmark.load(R.drawable.round_bookmark_border_24)

            binding.apply {
                ivMoviePoster.load("${IMG_URL_PREFIX_300}${movie.poster_path}"){crossfade(true)}
                tvMovieTitle.text = movie.title

                ivBookmark.setOnClickListener {
                    isFavorite = !isFavorite
                    if (isFavorite) binding.ivBookmark.load(R.drawable.round_bookmark_24)
                    else binding.ivBookmark.load(R.drawable.round_bookmark_border_24)

                    onFavoriteClick(movie.id, isFavorite)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),onFavoriteClick)
        holder.itemView.setOnClickListener { onMovieClick(getItem(position)) }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

    }
}