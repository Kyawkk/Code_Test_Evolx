package com.kyawzinlinn.codetestevolx.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.codetestevolx.data.repository.MovieRepository
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import com.kyawzinlinn.codetestevolx.domain.model.MovieDetailsResponse
import com.kyawzinlinn.codetestevolx.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
) : ViewModel(){
    private val _popularMovies = MutableLiveData<Resource<List<Movie>>>()
    val popularMovies: LiveData<Resource<List<Movie>>> get() = _popularMovies

    private val _upcomingMovies = MutableLiveData<Resource<List<Movie>>>()
    val upcomingMovies: LiveData<Resource<List<Movie>>> get() = _upcomingMovies

    private val _movieDetails = MutableLiveData<Resource<MovieDetailsResponse>>()
    val movieDetails: LiveData<Resource<MovieDetailsResponse>> get() = _movieDetails

    private val _movieId = savedStateHandle.getLiveData<String>("movie_id")
    val movieId: LiveData<String> get() = _movieId

    fun getPopularMovies(page: String){
        viewModelScope.launch {
            repository.getPopularMovies(page).collect{
                _popularMovies.value = it
            }
        }
    }

    fun getUpcomingMovies(page: String){
        viewModelScope.launch {
            repository.getUpcomingMovies(page).collect{
                _upcomingMovies.value = it
            }
        }
    }

    fun getMovieDetails(movieId: String){
        viewModelScope.launch {
            repository.getMovieDetails(movieId).collect{
                _movieDetails.value = it
            }
        }
    }

    fun toggleFavorite(movieId: String, isFavorite: Boolean){
        viewModelScope.launch {
            repository.toggleMovieFavorite(movieId, isFavorite)
        }
    }
}