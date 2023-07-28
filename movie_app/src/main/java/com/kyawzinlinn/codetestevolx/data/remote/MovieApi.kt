package com.kyawzinlinn.codetestevolx.data.remote

import com.kyawzinlinn.codetestevolx.domain.model.CastResponse
import com.kyawzinlinn.codetestevolx.domain.model.MovieDetailsResponse
import com.kyawzinlinn.codetestevolx.domain.model.MovieResponse
import com.kyawzinlinn.codetestevolx.utils.TOKEN
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Query("page")page: String
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Query("page")page: String
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Path("movie_id") movieId: String
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getCasts(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Path("movie_id") movieId: String
    ): CastResponse
}