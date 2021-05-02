package com.example.movieapp.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.network.MovieApi.MOVIE_API_KEY
import com.example.movieapp.network.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeViewModel"

class HomeViewModel(val service: MovieService) : ViewModel() {

    private val _nowPlaying = MutableLiveData<List<Movie>>()
    val nowPlaying: LiveData<List<Movie>> = _nowPlaying
    val nowPlayingFailed = ObservableBoolean()

    private val _mostPopular = MutableLiveData<List<Movie>>()
    val mostPopular: LiveData<List<Movie>> = _mostPopular
    val mostPopularFailed = ObservableBoolean()

    val moviesLoaded = ObservableBoolean()

    fun fetchNowPlaying() {
        service.nowPlaying(MOVIE_API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                Log.d(TAG, "response: $response")
                response.body()?.let {
                    _nowPlaying.postValue(it.results)
                    setMoviesLoaded(true)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, t.message, t)
                setNowPlayingFailed(true)
            }
        })
    }

    fun fetchMostPoular() {
        service.mostPopular(MOVIE_API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                Log.d(TAG, "response: $response")
                response.body()?.let {
                    _mostPopular.postValue(it.results)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, t.message, t)
                setMostPopularFailed(true)
            }
        })
    }

    fun setMoviesLoaded(loaded: Boolean) {
        moviesLoaded.set(loaded)
    }

    fun setNowPlayingFailed(failed: Boolean) {
        nowPlayingFailed.set(failed)
    }

    fun setMostPopularFailed(failed: Boolean) {
        mostPopularFailed.set(failed)
    }

    class Factory(val service: MovieService) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(service) as T
        }
    }
}