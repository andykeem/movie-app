package com.example.movieapp.network

import com.example.movieapp.model.GenreList
import retrofit2.Call
import retrofit2.http.GET

interface GenreService {

    @GET(value = "3/genre/movie/list?api_key=${MovieApi.MOVIE_API_KEY}")
    fun genreList(): Call<GenreList>
}