package com.kyawzinlinn.codetestevolx.data.repository

import android.util.Log
import com.kyawzinlinn.codetestevolx.data.local.LocalDataSource
import com.kyawzinlinn.codetestevolx.data.local.dao.FavoriteDao
import com.kyawzinlinn.codetestevolx.data.local.dao.MovieDao
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseFavoriteMovieId
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import com.kyawzinlinn.codetestevolx.data.remote.RemoteDataSource
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import com.kyawzinlinn.codetestevolx.domain.model.MovieDetailsResponse
import com.kyawzinlinn.codetestevolx.utils.MovieType
import com.kyawzinlinn.codetestevolx.utils.Resource
import com.kyawzinlinn.codetestevolx.utils.toDatabaseMovies
import com.kyawzinlinn.codetestevolx.utils.toMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : MovieRepository{
    override fun getPopularMovies(page: String): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val type = MovieType.POPULAR
        val favoriteMovieIds = localDataSource.getAllFavoriteMovieIds().map { it.id }

        val cachedMoviesFlow = localDataSource.getAllMovies(type.toString()).map {
            it.toMovies().map { movie ->
                movie.copy(isFavorite = movie.id in favoriteMovieIds)
            }
        }

        val cachedMovies = cachedMoviesFlow.first()
        if (cachedMovies.size == 0) emit(Resource.Loading())
        else emit(Resource.Success(cachedMovies))

        try {
            val moviesFromApi = remoteDataSource.getPopularMovies(page)

            moviesFromApi.results.map { movie -> movie.isFavorite = movie.id in favoriteMovieIds }

            emit(Resource.Success(data = moviesFromApi.results))
            withContext(Dispatchers.IO){
                localDataSource.deleteMovies(type.toString())
                localDataSource.saveMovies(moviesFromApi.results.toDatabaseMovies(type.toString()))
            }
        }catch (e: Exception){
            emit(Resource.Error(formatError(e)))
        }

        localDataSource.getAllMovies(type.toString()).collect{
            emit(Resource.Success(it.toMovies()))
        }
    }

    override fun getUpcomingMovies(page: String): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val type = MovieType.UPCOMING
        val favoriteMovieIds = localDataSource.getAllFavoriteMovieIds().map { it.id }
        // Load cached movies with isFavorite info

        val cachedMoviesFlow = localDataSource.getAllMovies(type.toString()).map {
            it.toMovies().map { movie ->
                movie.copy(isFavorite = movie.id in favoriteMovieIds)
            }
        }

        val cachedMovies = cachedMoviesFlow.first()
        if (cachedMovies.size == 0) emit(Resource.Loading())
        else emit(Resource.Success(cachedMovies))

        try {
            val moviesFromApi = remoteDataSource.getUpcomingMovies(page)

            moviesFromApi.results.map { movie -> movie.isFavorite = movie.id in favoriteMovieIds }

            emit(Resource.Success(data = moviesFromApi.results))
            localDataSource.deleteMovies(type.toString())
            localDataSource.saveMovies(moviesFromApi.results.toDatabaseMovies(type.toString()))
        }catch (e: Exception){
            emit(Resource.Error(formatError(e)))
        }

        localDataSource.getAllMovies(type.toString()).collect{
            emit(Resource.Success(it.toMovies()))
        }

    }

    override suspend fun getCachedMovies(type: String): Flow<List<DatabaseMovie>> = localDataSource.getAllMovies(type)
    override suspend fun getMovieDetails(movieId: String): Flow<Resource<MovieDetailsResponse>> = flow {
        emit(Resource.Loading())

        try {
            val movieDetails = remoteDataSource.getMovieDetails(movieId)
            emit(Resource.Success(movieDetails))
        }catch (e: Exception){
            e.printStackTrace()
            emit(Resource.Error(formatError(e)))
        }
    }

    override suspend fun deleteMovies(type: String) = localDataSource.deleteMovies(type)

    override suspend fun toggleMovieFavorite(movieId: String, isFavorite: Boolean){
        localDataSource.toggleFavoriteMovie(movieId, isFavorite)
        if (isFavorite) localDataSource.addFavoriteMovie(DatabaseFavoriteMovieId(id = movieId))
        else localDataSource.deleteFavoriteMovieId(movieId)
    }

    override suspend fun addFavoriteMovieId(movieId: DatabaseFavoriteMovieId) = localDataSource.addFavoriteMovie(movieId)

    override suspend fun getAllFavoriteMovieIds(): List<DatabaseFavoriteMovieId> = localDataSource.getAllFavoriteMovieIds()

    override suspend fun deleteFavoriteMovieId(movieId: String) = localDataSource.deleteFavoriteMovieId(movieId)

    override suspend fun isFavorite(movieId: String): Boolean {
        val favoriteMovieIds = localDataSource.getAllFavoriteMovieIds().map { it.id }
        return movieId in favoriteMovieIds
    }

    private fun formatError(e: Exception): String{
        return when(e){
            is IOException -> "Network Unavailable: Please check your internet connection and try again."
            is HttpException -> "An error occurred. Please check your internet connection."
            else -> e.message.toString()
        }
    }

}