package com.learn.personal.moviet.core.data.source.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learn.personal.moviet.core.data.source.source.local.entity.MovieEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.TvShowEntity

@Database(entities = [TvShowEntity::class, MovieEntity::class], version = 1)
abstract class CatalogueDatabase: RoomDatabase() {
    abstract fun getCatalogueDao(): CatalogueDao

    companion object {
        @Volatile
        private var INSTANCE: CatalogueDatabase? = null

        fun getInstance(context: Context): CatalogueDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CatalogueDatabase::class.java,
                    "catalogue.db"
                ).build()
            }
    }

}