package com.example.movieapp.network

import com.example.movieapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApi {

    const val MOVIE_API_KEY = BuildConfig.MOVIE_API_KEY
    const val MOVIE_API_URL = "https://api.themoviedb.org"

    fun httpClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        return Retrofit.Builder()
                .baseUrl(MOVIE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
    }
}