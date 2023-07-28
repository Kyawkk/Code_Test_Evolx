package com.kyawzinlinn.codetestevolx.domain.model

data class Movie(
    val id: String,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    var isFavorite: Boolean = false
)