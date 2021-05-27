package com.learn.personal.moviet.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.learn.personal.moviet.data.remote.models.TvShow

class TvShowResponse {
    @SerializedName("results")
    val results: List<TvShow>? = null
}