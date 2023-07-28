package com.kyawzinlinn.codetestevolx.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movie_ids")
data class DatabaseFavoriteMovieId (
    @PrimaryKey
    @ColumnInfo("id")
    val id: String
)