package com.learn.personal.moviet.core.data.api

import com.learn.personal.moviet.core.data.api.ApiConfig.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun initClient(): Retrofit {

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
                .client(client)
        }.build()
    }
}