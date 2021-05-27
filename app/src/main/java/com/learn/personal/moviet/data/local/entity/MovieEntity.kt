package com.learn.personal.moviet.data.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movieentities")
@Parcelize
data class MovieEntity (
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