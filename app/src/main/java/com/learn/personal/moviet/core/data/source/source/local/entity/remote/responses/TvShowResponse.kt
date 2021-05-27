package com.learn.personal.moviet.core.data.source.source.local.entity.remote.responses

import com.google.gson.annotations.SerializedName
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.models.TvShow

class TvShowResponse {
    @SerializedName("results")
    val results: List<TvShow>? = null
}