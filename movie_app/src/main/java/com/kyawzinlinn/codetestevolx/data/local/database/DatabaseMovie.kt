package com.kyawzinlinn.codetestevolx.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cached_movies")
data class DatabaseMovie(
    @PrimaryKey
    val id: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val type: String,
    var isFavorite: Boolean = false
)
