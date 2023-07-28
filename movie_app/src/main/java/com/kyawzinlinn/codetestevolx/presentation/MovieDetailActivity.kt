package com.kyawzinlinn.codetestevolx.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.kyawzinlinn.codetestevolx.R
import com.kyawzinlinn.codetestevolx.databinding.ActivityMovieDetailBinding
import com.kyawzinlinn.codetestevolx.domain.MovieViewModel
import com.kyawzinlinn.codetestevolx.domain.model.MovieDetailsResponse
import com.kyawzinlinn.codetestevolx.utils.IMG_URL_PREFIX_500
import com.kyawzinlinn.codetestevolx.utils.IS_FAVORITE_INTENT_EXTRA
import com.kyawzinlinn.codetestevolx.utils.Resource
import com.kyawzinlinn.codetestevolx.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var viewModel: MovieViewModel
    private var movieId: String = ""
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        setContentView(binding.root)

        movieId = viewModel.movieId.value ?: ""
        isFavorite = intent?.extras?.getBoolean(IS_FAVORITE_INTENT_EXTRA) as Boolean

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bookmarkMovie()
        loadMovieDetails()
    }

    private fun bookmarkMovie() {

        if (isFavorite) binding.ivFavorite.load(R.drawable.round_bookmark_24)
        else binding.ivFavorite.load(R.drawable.round_bookmark_border_24)

        binding.ivFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) binding.ivFavorite.load(R.drawable.round_bookmark_24)
            else binding.ivFavorite.load(R.drawable.round_bookmark_border_24)

            viewModel.toggleFavorite(movieId,isFavorite)
        }
    }

    private fun loadMovieDetails() {
        viewModel.getMovieDetails(movieId)
        viewModel.movieDetails.observe(this){
            when(it){
                is Resource.Loading -> binding.movieDetailProgressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.ivFavorite.visibility = View.VISIBLE
                    binding.movieDetailProgressBar.visibility = View.GONE
                    bindUI(it.data)
                }
                is Resource.Error -> {
                    binding.movieDetailProgressBar.visibility = View.GONE
                    showErrorSnackBar(it.message!!){
                        loadMovieDetails()
                    }
                }
            }
        }
    }

    private fun bindUI(movieDetails: MovieDetailsResponse?) {
        title = movieDetails?.title
        binding.apply {
            ivMovieBackDrop.load("${IMG_URL_PREFIX_500}${movieDetails?.backdrop_path}"){crossfade(true)}
            tvMovieName.text = movieDetails?.title
            tvMovieOverview.text = movieDetails?.overview
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return true
    }
}