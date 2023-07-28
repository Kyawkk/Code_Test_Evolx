package com.kyawzinlinn.codetestevolx.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseFavoriteMovieId

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteMovie(movieId: DatabaseFavoriteMovieId)

    @Query("select * from favorite_movie_ids")
    suspend fun getAllFavoriteIds(): List<DatabaseFavoriteMovieId>

    @Query("delete from favorite_movie_ids where id = :movieId")
    suspend fun deleteFavoriteId(movieId: String)
}