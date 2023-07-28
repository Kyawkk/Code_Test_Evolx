package com.kyawzinlinn.codetestevolx.data.local

import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseFavoriteMovieId
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllMovies(type: String): Flow<List<DatabaseMovie>>

    suspend fun saveMovies(movies: List<DatabaseMovie>)

    suspend fun deleteMovies(type: String)

    suspend fun toggleFavoriteMovie(movieId: String, isFavorite: Boolean)

    suspend fun addFavoriteMovie(movieId: DatabaseFavoriteMovieId)

    suspend fun getAllFavoriteMovieIds(): List<DatabaseFavoriteMovieId>

    suspend fun deleteFavoriteMovieId(movieId: String)
}