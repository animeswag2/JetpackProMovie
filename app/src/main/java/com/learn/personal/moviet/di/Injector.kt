package com.learn.personal.moviet.di

import android.content.Context
import com.learn.personal.moviet.data.CatalogueRepository
import com.learn.personal.moviet.data.local.LocalDataSource
import com.learn.personal.moviet.data.local.room.CatalogueDatabase
import com.learn.personal.moviet.data.remote.RemoteDataSource
import com.learn.personal.moviet.utils.AppExecutors

class Injector {
    companion object {
        fun provideRepository(context: Context): CatalogueRepository {

            val appExecutors = AppExecutors()

            val catalogueDao = CatalogueDatabase.getInstance(context).getCatalogueDao()
            val localDataSource = LocalDataSource.getInstance(catalogueDao)
            val remoteDataSource = RemoteDataSource.getInstance()
            return CatalogueRepository.getInstance(localDataSource, remoteDataSource, appExecutors)
        }
    }
}