package com.kyawzinlinn.codetestevolx.data.repository

import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseFavoriteMovieId
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import com.kyawzinlinn.codetestevolx.domain.model.MovieDetailsResponse
import com.kyawzinlinn.codetestevolx.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(page: String): Flow<Resource<List<Movie>>>
    fun getUpcomingMovies(page: String): Flow<Resource<List<Movie>>>
    suspend fun getCachedMovies(type: String): Flow<List<DatabaseMovie>>
    suspend fun getMovieDetails(movieId: String): Flow<Resource<MovieDetailsResponse>>
    suspend fun deleteMovies(type: String)
    suspend fun toggleMovieFavorite(movieId: String, isFavorite: Boolean)

    suspend fun addFavoriteMovieId(movieId: DatabaseFavoriteMovieId)
    suspend fun getAllFavoriteMovieIds(): List<DatabaseFavoriteMovieId>
    suspend fun deleteFavoriteMovieId(movieId: String)
    suspend fun isFavorite(movieId: String): Boolean
}