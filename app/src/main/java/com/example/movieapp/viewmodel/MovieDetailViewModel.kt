package com.example.movieapp.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.db.entity.Favorite
import com.example.movieapp.model.Genre
import com.example.movieapp.model.GenreResponse
import com.example.movieapp.model.Movie
import com.example.movieapp.network.GenreService
import com.example.movieapp.network.MovieApi.MOVIE_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MovieDetailViewModel"
class MovieDetailViewModel(val db: AppDatabase,
                           val service: GenreService,
                           val movie: Movie) : ViewModel() {

    private val _genreList = MutableLiveData<List<Genre>>()
    val genreList: LiveData<List<Genre>> = _genreList
    val movieGenres = ObservableField<String>()
    val _savedMovieAsFavorite = MutableLiveData<Boolean>()
    val savedMovieAsFavorite = _savedMovieAsFavorite
    val _removedFavorite = MutableLiveData<Boolean>()
    val removedFavorite = _removedFavorite
    val _addedFavorite = MutableLiveData<Boolean>()
    val addedFavorite = _addedFavorite
    val movieLoaded = ObservableBoolean()

    fun fetchGenreList() {
        service.genreList(MOVIE_API_KEY).enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                Log.d(TAG, "response: $response")
                _genreList.postValue(response.body()?.genres)
                setMovieLoaded(true)
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                Log.e(TAG, t.message, t)
            }
        })
    }

    fun loadGenreList(list: List<Genre>) {
        val genres = mutableListOf<String>()
        movie.genre_ids?.let {
            var ids: List<Int> = it
            if (it.size > 3) {
                ids = it.subList(0, 4)
            }
            ids.forEach { id ->
                list.forEach { genre ->
                    genre.name?.let { name ->
                        if (id == genre.id) {
                            genres.add(name)
                        }
                    }
                }
            }
            movieGenres.set(genres.joinToString(separator = ", "))
        }
    }

    fun addMovieToFavorite(movie: Movie?) {
        viewModelScope.launch {
            val insertedRows = insertFavorite(movie)
            _addedFavorite.value = insertedRows > 0
        }
    }

    private suspend fun insertFavorite(movie: Movie?): Int {
        var insertedRows = 0
        return withContext(Dispatchers.IO) {
            val favorited = didMovieFavorite(movie)
            if (!favorited) { // if this movie is never saved to favorite db table then save it
                movie?.title?.let {
                    val favorite =
                        Favorite(movieId = movie.id,
                            movieTitle = movie.getTitleWithReleaseYear(),
                            movieJson = movie.json)
                    db.favoriteDao().insertAll(favorite)
                    db.favoriteDao().numFavoriteByMovieId(movie.id)
                }
            }
            insertedRows
        }
    }

    fun checkFavoriteByMovie(movie: Movie?) {
        viewModelScope.launch {
            val favorited = didMovieFavorite(movie)
            _savedMovieAsFavorite.postValue(favorited)
        }
    }

    private suspend fun didMovieFavorite(movie: Movie?): Boolean {
        return withContext(Dispatchers.IO) {
            val cnt = db.favoriteDao().numFavoriteByMovieId(movie?.id)
            cnt > 0
        }
    }

    fun removeMovieFromFavorite(movie: Movie?) {
        movie?.let {
            viewModelScope.launch {
                val deletedRows = removeFavoriteByMovieId(it.id)
                _removedFavorite.value = deletedRows > 0
            }
        }
    }

    private suspend fun removeFavoriteByMovieId(movieId: Int): Int {
        return withContext(Dispatchers.IO) {
            db.favoriteDao().deleteByMovieId(movieId)
        }
    }

    fun setMovieLoaded(loaded: Boolean) {
        movieLoaded.set(loaded)
    }

    class Factory(val db: AppDatabase, val service: GenreService, val movie: Movie) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MovieDetailViewModel(db, service, movie) as T
        }
    }
}