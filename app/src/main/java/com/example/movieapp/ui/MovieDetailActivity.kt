package com.example.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.MovieApp
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMovieDetailBinding
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import com.example.movieapp.network.GenreService
import com.example.movieapp.network.MovieApi
import com.example.movieapp.viewmodel.MovieDetailViewModel
import com.example.movieapp.viewmodel.MovieViewModel.Companion.EXTRA_MOVIE

private const val TAG = "MovieDetailActivity"
class MovieDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieDetailBinding
    lateinit var viewModel: MovieDetailViewModel
    var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        movie = intent.getParcelableExtra(EXTRA_MOVIE)
        Log.d(TAG, "movie: $movie")

        if (movie == null) return

        movie?.let {
            val db = (application as MovieApp).db
            val service = MovieApi.httpClient().create(GenreService::class.java)
            val factory = MovieDetailViewModel.Factory(db, service, it)
            viewModel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
            binding.model = viewModel
        }

        viewModel.genreList.observe(this) {
            Log.d(TAG, "genre list: $it")
            updateGenreList(it)
        }

        viewModel.savedMovieAsFavorite.observe(this) {
            it?.let { saved ->
                if (saved) {
                    movieSavedToFavorite()
                }
            }
        }

        viewModel.addedFavorite.observe(this) {
            it?.let { added ->
                if (added) {
                    movieAddedToFavorite()
                }
            }
        }

        viewModel.removedFavorite.observe(this) {
            it?.let { removed ->
                if (removed) {
                    movieRemovedFromFavorite()
                }
            }
        }

        viewModel.fetchGenreList()
        viewModel.checkFavoriteByMovie(movie)

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_home -> {
                    with (Intent(this@MovieDetailActivity, MainActivity::class.java)) {
                        startActivity(this)
                    }
                    true
                }
                R.id.menu_item_favorite -> {
                    if (it.isChecked) {
                        viewModel.removeMovieFromFavorite(movie)
                    } else {
                        viewModel.addMovieToFavorite(movie)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun updateGenreList(list: List<Genre>) {
        viewModel.loadGenreList(list)
    }

    private fun selectFavoriteMenuItem() {
        binding.bottomNav.menu.findItem(R.id.menu_item_favorite).setChecked(true)
    }

    private fun unselectFavoriteMenuItem() {
        binding.bottomNav.menu.findItem(R.id.menu_item_none).setChecked(true)
    }

    private fun movieSavedToFavorite() {
        selectFavoriteMenuItem()
    }

    private fun movieAddedToFavorite() {
        selectFavoriteMenuItem()
        val text = getString(R.string.msg_favorite_added)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun movieRemovedFromFavorite() {
        unselectFavoriteMenuItem()
        val text = getString(R.string.msg_favorite_removed)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}