package com.kyawzinlinn.codetestevolx.di

import com.kyawzinlinn.codetestevolx.data.local.LocalDataSource
import com.kyawzinlinn.codetestevolx.data.local.LocalDataSourceImpl
import com.kyawzinlinn.codetestevolx.data.local.dao.FavoriteDao
import com.kyawzinlinn.codetestevolx.data.local.dao.MovieDao
import com.kyawzinlinn.codetestevolx.data.remote.RemoteDataSource
import com.kyawzinlinn.codetestevolx.data.repository.MovieRepository
import com.kyawzinlinn.codetestevolx.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRepositoryModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(movieDao: MovieDao, favoriteDao: FavoriteDao): LocalDataSource {
        return LocalDataSourceImpl(movieDao,favoriteDao)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): MovieRepository {
        return MovieRepositoryImpl(localDataSource,remoteDataSource)
    }
}