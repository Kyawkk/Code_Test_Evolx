package com.kyawzinlinn.codetestevolx.domain.model

data class MovieResponse(
    val page: String,
    val results: List<Movie>,
    val total_pages: String,
    val total_results: String
)