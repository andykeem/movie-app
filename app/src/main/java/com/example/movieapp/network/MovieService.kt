package com.example.movieapp.network

import com.example.movieapp.model.MostPopular
import com.example.movieapp.model.NowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET(value = "movie/now_playing")
    fun nowPlaying(@Query(value = "api_key") apiKey: String): Call<NowPlaying>

    @GET(value = "movie/popular")
    fun mostPopular(@Query(value = "api_key") apiKey: String): Call<MostPopular>
}