package com.example.movieapp.network

import com.example.movieapp.model.MostPopular
import com.example.movieapp.model.NowPlaying
import retrofit2.Call
import retrofit2.http.GET

interface MovieService {

    @GET(value = "3/movie/now_playing?api_key=${MovieApi.MOVIE_API_KEY}")
    fun nowPlaying(): Call<NowPlaying>

    @GET(value = "3/movie/popular?api_key=${MovieApi.MOVIE_API_KEY}")
    fun mostPopular(): Call<MostPopular>
}