package com.kyawzinlinn.codetestevolx.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kyawzinlinn.codetestevolx.R
import com.kyawzinlinn.codetestevolx.databinding.CarouselItemBinding
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import com.kyawzinlinn.codetestevolx.utils.IMG_URL_PREFIX_300

class CarouselAdapter(private val onMovieClick: (Movie) -> Unit, private val onFavoriteClick: (String,Boolean) -> Unit): ListAdapter<Movie, CarouselAdapter.ViewHolder>(MovieItemAdapter.DiffCallBack) {
    class ViewHolder(private val binding: CarouselItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onFavoriteClick: (String, Boolean) -> Unit){

            var isFavorite = movie.isFavorite

            if (isFavorite) binding.ivFavorite.load(R.drawable.round_bookmark_24)
            else binding.ivFavorite.load(R.drawable.round_bookmark_border_24)

            binding.apply {
                ivCarousel.load("${IMG_URL_PREFIX_300}${movie.poster_path}")
                tvCarouselTitle.text = movie.title

                ivFavorite.setOnClickListener {
                    isFavorite = !isFavorite
                    if (isFavorite) binding.ivFavorite.load(R.drawable.round_bookmark_24)
                    else binding.ivFavorite.load(R.drawable.round_bookmark_border_24)

                    onFavoriteClick(movie.id, isFavorite)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CarouselItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),onFavoriteClick)
        holder.itemView.setOnClickListener { onMovieClick(getItem(position)) }
    }
}