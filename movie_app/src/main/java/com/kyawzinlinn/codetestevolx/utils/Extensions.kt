package com.kyawzinlinn.codetestevolx.utils

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import com.kyawzinlinn.codetestevolx.domain.model.Movie

fun List<DatabaseMovie>.toMovies(): List<Movie>{
    return map{
        Movie(
            it.id,
            it.title,
            it.overview,
            it.releaseDate,
            it.posterPath,
            it.isFavorite
        )
    }
}

fun List<Movie>.toDatabaseMovies(type: String): List<DatabaseMovie>{
    return map{
        DatabaseMovie(
            it.id,
            it.title,
            it.overview,
            it.release_date,
            it.poster_path,
            type,
            it.isFavorite
        )
    }
}

fun Activity.showErrorSnackBar(message: String, onRetry: () -> Unit){
    Snackbar.make(window.decorView,message,Snackbar.LENGTH_INDEFINITE)
        .setAction("Retry", object : View.OnClickListener{
            override fun onClick(p0: View?) {
                onRetry()
            }
        }).show()
}