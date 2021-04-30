package com.example.movieapp.viewmodel

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.Movie
import com.example.movieapp.ui.MovieDetailActivity

class MovieViewModel(val movie: Movie) : ViewModel() {

    private val _selectMovie = MutableLiveData<Movie>()
    val movieSelected: LiveData<Movie> = _selectMovie
    val title = ObservableField<String>()

    init {
        title.set(movie.title)
    }

    fun onMovieClick(view: View) {
        with (Intent(view.context, MovieDetailActivity::class.java)) {
            putExtra(EXTRA_MOVIE, movie)
            view.context.startActivity(this)
        }
    }

    companion object {

        const val EXTRA_MOVIE = "com.example.movieapp.EXTRA_MOVIE"
    }
}