package com.kyawzinlinn.codetestevolx.data.local

import com.kyawzinlinn.codetestevolx.data.local.dao.FavoriteDao
import com.kyawzinlinn.codetestevolx.data.local.dao.MovieDao
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseFavoriteMovieId
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao
) : LocalDataSource{
    override fun getAllMovies(type: String): Flow<List<DatabaseMovie>> = movieDao.getAllMovies(type)

    override suspend fun saveMovies(movies: List<DatabaseMovie>) = movieDao.insertMovies(movies)

    override suspend fun deleteMovies(type: String) = movieDao.deleteMovies(type)

    override suspend fun toggleFavoriteMovie(movieId: String, isFavorite: Boolean) = movieDao.setMovieFavorite(movieId, isFavorite)

    override suspend fun addFavoriteMovie(movieId: DatabaseFavoriteMovieId) = favoriteDao.addFavoriteMovie(movieId)

    override suspend fun getAllFavoriteMovieIds(): List<DatabaseFavoriteMovieId> = favoriteDao.getAllFavoriteIds()

    override suspend fun deleteFavoriteMovieId(movieId: String) = favoriteDao.deleteFavoriteId(movieId)
}