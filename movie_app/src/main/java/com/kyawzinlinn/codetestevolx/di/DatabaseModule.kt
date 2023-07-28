package com.kyawzinlinn.codetestevolx.di

import android.content.Context
import androidx.room.Room
import com.kyawzinlinn.codetestevolx.data.local.Database
import com.kyawzinlinn.codetestevolx.data.local.dao.FavoriteDao
import com.kyawzinlinn.codetestevolx.data.local.dao.MovieDao
import com.kyawzinlinn.codetestevolx.data.local.database.DatabaseMovie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext application: Context): Database {
        return Room.databaseBuilder(
            application,
            Database::class.java,
            "Movie"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: Database): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun providesFavoriteDao(database: Database): FavoriteDao {
        return database.favoriteDao()
    }
}