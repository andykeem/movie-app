package com.example.movieapp.network

import com.example.movieapp.model.GenreList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreService {

    @GET(value = "genre/movie/list")
    fun genreList(@Query(value = "api_key") apiKey: String): Call<GenreList>
}