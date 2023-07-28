package com.kyawzinlinn.codetestevolx.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import com.kyawzinlinn.codetestevolx.domain.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM cached_movies where type = :type")
    fun getAllMovies(type: String): Flow<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<DatabaseMovie>)

    @Query("delete from cached_movies where type = :type")
    suspend fun deleteMovies(type: String)

    @Query("UPDATE cached_movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun setMovieFavorite(movieId: String, isFavorite: Boolean)
}