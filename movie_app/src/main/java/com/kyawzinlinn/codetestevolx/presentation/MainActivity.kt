package com.kyawzinlinn.codetestevolx.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.kyawzinlinn.codetestevolx.data.remote.RemoteDataSource
import com.kyawzinlinn.codetestevolx.databinding.ActivityMainBinding
import com.kyawzinlinn.codetestevolx.domain.MovieViewModel
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import com.kyawzinlinn.codetestevolx.presentation.adapter.CarouselAdapter
import com.kyawzinlinn.codetestevolx.presentation.adapter.MovieItemAdapter
import com.kyawzinlinn.codetestevolx.utils.IS_FAVORITE_INTENT_EXTRA
import com.kyawzinlinn.codetestevolx.utils.MOVIE_ID_INTENT_EXTRA
import com.kyawzinlinn.codetestevolx.utils.Resource
import com.kyawzinlinn.codetestevolx.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var adapter : MovieItemAdapter
    private lateinit var handler: Handler
    private val runnable = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        handler = Handler(Looper.myLooper()!!)
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        loadPopularMovies()
        loadUpcomingMovies()
    }

    private fun loadUpcomingMovies() {
        viewModel.apply {
            if(viewModel.upcomingMovies.value == null) getUpcomingMovies("1")
            upcomingMovies.observe(this@MainActivity){
                CoroutineScope(Dispatchers.Main).launch{
                    when(it){
                        is Resource.Loading -> {
                            binding.upcomingProgressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.upcomingProgressBar.visibility = View.GONE
                            setUpUpcomingCarousel(it.data)
                        }
                        is Resource.Error -> {
                            binding.upcomingProgressBar.visibility = View.GONE
                            showErrorSnackBar(it.message!!){
                                loadUpcomingMovies()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpUpcomingCarousel(data: List<Movie>?) {
        val carouselAdapter = CarouselAdapter(onMovieClick = {movie ->
            startMovieDetailActivity(movie)
        }, onFavoriteClick = {id, isFavorite ->
            viewModel.toggleFavorite(id,isFavorite)
        })

        binding.viewPager.adapter = carouselAdapter
        carouselAdapter.submitList(data)
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))

        binding.viewPager.setPageTransformer(transformer)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })
    }

    private fun loadPopularMovies() {
        viewModel.apply {
            if(viewModel.popularMovies.value == null) getPopularMovies("1")
            popularMovies.observe(this@MainActivity){
                when(it){
                    is Resource.Loading -> {
                        binding.popularProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.popularProgressBar.visibility = View.GONE
                        setUpPopularRecyclerview(it.data)
                    }
                    is Resource.Error -> {
                        binding.popularProgressBar.visibility = View.GONE
                        showErrorSnackBar(it.message!!){
                            loadPopularMovies()
                        }
                    }
                }
            }
        }
    }

    private fun setUpPopularRecyclerview(movies: List<Movie>?) {
        binding.rvPopular.setHasFixedSize(true)
        adapter = MovieItemAdapter(onFavoriteClick = {id, isFavorite ->
            viewModel.toggleFavorite(id,isFavorite)
        }, onMovieClick = {movie ->
            startMovieDetailActivity(movie)
        })
        binding.rvPopular.adapter = adapter
        adapter.submitList(movies)
    }

    private fun startMovieDetailActivity(movie: Movie){
        val intent = Intent(this, MovieDetailActivity::class.java)
        val bundle = bundleOf("movie_id" to movie.id)
        intent.putExtras(bundle)
        intent.putExtra(IS_FAVORITE_INTENT_EXTRA,movie.isFavorite)
        startActivity(intent)
    }
}