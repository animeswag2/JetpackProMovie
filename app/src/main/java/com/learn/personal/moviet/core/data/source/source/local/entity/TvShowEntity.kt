package com.learn.personal.moviet.core.data.source.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tvshowentities")
@Parcelize
data class TvShowEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieId")
    var movieId: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "poster")
    var poster: String,

    @ColumnInfo(name = "vote")
    var vote: Float,

    @ColumnInfo(name = "release_date")
    var release: String,

    @ColumnInfo(name = "isBookmarked")
    var isBookmarked: Boolean = false
): Parcelable