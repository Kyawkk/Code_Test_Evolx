package com.kyawzinlinn.codetestevolx.data.remote

import com.kyawzinlinn.codetestevolx.domain.model.MovieDetailsResponse
import com.kyawzinlinn.codetestevolx.domain.model.MovieResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {
    suspend fun getPopularMovies(page: String): MovieResponse{
        return movieApi.getPopularMovies(page = page)
    }

    suspend fun getUpcomingMovies(page: String): MovieResponse{
        return movieApi.getUpcomingMovies(page = page)
    }

    suspend fun getMovieDetails(movieId: String): MovieDetailsResponse{
        return movieApi.getMovieDetails(movieId = movieId)
    }
}