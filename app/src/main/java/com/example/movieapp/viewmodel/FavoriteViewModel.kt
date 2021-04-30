package com.example.movieapp.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.db.entity.Favorite
import com.example.movieapp.model.Movie
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FavoriteViewModel"
class FavoriteViewModel(val db: AppDatabase, val gson: Gson) : ViewModel() {

    val _favoriteList = MutableLiveData<List<Favorite>>()
    val favoriteList: LiveData<List<Favorite>> = _favoriteList
    val emptyFavorites = ObservableBoolean()

    fun getFavoriteList() {
        viewModelScope.launch {
            val favList: List<Favorite> = fetchFavoritesFromDb()
            Log.d(TAG, "favorite list: $favList")
            _favoriteList.value = favList
            setEmptyFavorites(favList.isEmpty())
        }
    }

    suspend fun fetchFavoritesFromDb(): List<Favorite> {
        return withContext(Dispatchers.IO) {
            db.favoriteDao().findAll()
        }
    }

    fun getMovieByFavorite(fav: Favorite) =
        gson.fromJson(fav.movieJson, Movie::class.java)

    fun setEmptyFavorites(empty: Boolean) {
        emptyFavorites.set(empty)
    }

    class Factory(val db: AppDatabase, val gson: Gson) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FavoriteViewModel(db, gson) as T
    }
}