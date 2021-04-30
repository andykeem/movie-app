package com.example.movieapp.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.model.MostPopular
import com.example.movieapp.model.Movie
import com.example.movieapp.model.NowPlaying
import com.example.movieapp.network.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeViewModel"

class HomeViewModel(val service: MovieService) : ViewModel() {

    private val _nowPlaying = MutableLiveData<List<Movie>>()
    val nowPlaying: LiveData<List<Movie>> = _nowPlaying

    private val _mostPopular = MutableLiveData<List<Movie>>()
    val mostPopular: LiveData<List<Movie>> = _mostPopular

    val moviesLoaded = ObservableBoolean()

    fun fetchNowPlaying() {
        service.nowPlaying().enqueue(object : Callback<NowPlaying> {
            override fun onResponse(call: Call<NowPlaying>, response: Response<NowPlaying>) {
                Log.d(TAG, "response: $response")
                response.body()?.let {
                    _nowPlaying.postValue(it.results)
                    setMoviesLoaded(true)
                }
            }

            override fun onFailure(call: Call<NowPlaying>, t: Throwable) {
                Log.e(TAG, t.message, t)
            }
        })
    }

    fun fetchMostPoular() {
        service.mostPopular().enqueue(object : Callback<MostPopular> {
            override fun onResponse(call: Call<MostPopular>, response: Response<MostPopular>) {
                Log.d(TAG, "response: $response")
                response.body()?.let {
                    _mostPopular.postValue(it.results)
                }
            }

            override fun onFailure(call: Call<MostPopular>, t: Throwable) {
                Log.e(TAG, t.message, t)
            }
        })
    }

    fun setMoviesLoaded(loaded: Boolean) {
        moviesLoaded.set(loaded)
    }

    class Factory(val service: MovieService) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(service) as T
        }
    }
}