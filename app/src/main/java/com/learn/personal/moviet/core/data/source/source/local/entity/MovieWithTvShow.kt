package com.learn.personal.moviet.core.data.source.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MovieWithTvShow (
    @Embedded
    var mMovie: MovieEntity,

    @Relation(parentColumn = "movieId", entityColumn ="movieId")
    var mTvShow: List<TvShowEntity>

    )