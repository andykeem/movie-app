package com.example.movieapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.math.roundToInt

const val MOVIE_IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

@Parcelize
data class Movie (
    val poster_path: String?,
    val overview: String?,
    val release_date: String?,
    val genre_ids: List<Int>?,
    val id: Int,
    val title: String?,
    val backdrop_path: String?,
    val vote_average: Float?,
    var json: String?
) : Parcelable {

    fun getPosterUrl() = "${MOVIE_IMAGE_PATH}/$poster_path"

    fun getBackdropUrl() = "${MOVIE_IMAGE_PATH}/$backdrop_path"

    fun getRating(): Int = vote_average?.times(10)?.roundToInt() ?: 0

    fun getReleaseYear() = release_date?.split("-")?.get(0) ?: ""

    fun getTitleWithReleaseYear(): String? {
        return release_date?.let {
            val (year, month, day) = it.split("-")
            "$title ($year)"
        } ?: title
    }
}