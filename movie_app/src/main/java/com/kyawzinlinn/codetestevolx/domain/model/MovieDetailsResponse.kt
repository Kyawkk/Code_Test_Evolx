package com.kyawzinlinn.codetestevolx.domain.model

data class MovieDetailsResponse(
    val backdrop_path: String,
    val genres: List<Genre>,
    val id: Int,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double
)