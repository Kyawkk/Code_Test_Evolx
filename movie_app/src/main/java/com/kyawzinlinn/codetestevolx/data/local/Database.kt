package com.kyawzinlinn.codetestevolx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kyawzinlinn.codetestevolx.data.local.dao.FavoriteDao
import com.kyawzinlinn.codetestevolx.data.local.dao.MovieDao
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseFavoriteMovieId
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie

@Database(entities = [DatabaseFavoriteMovieId::class, DatabaseMovie::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
}