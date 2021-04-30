package com.example.movieapp.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true) val fid: Int,
    @ColumnInfo(name = "movie_id") val movieId: Int?,
    @ColumnInfo(name = "movie_title") val movieTitle: String?,
    @ColumnInfo(name = "movie_json") val movieJson: String?
) {
    constructor(movieId: Int?, movieTitle: String?, movieJson: String?) :
            this(0, movieId, movieTitle, movieJson)
}
