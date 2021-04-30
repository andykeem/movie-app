package com.example.movieapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapp.db.entity.Favorite

@Dao
interface FavoriteDao {

    @Query(value = "SELECT * FROM favorite")
    fun findAll(): List<Favorite>

    @Query(value = "SELECT * FROM favorite WHERE fid IN (:fids)")
    fun findAllByIds(fids: IntArray): List<Favorite>

    @Query(value = "SELECT * FROM favorite WHERE fid = :fid")
    fun findById(fid: Int): Favorite

    @Query(value = "SELECT COUNT(*) FROM Favorite WHERE movie_id = :movieId")
    fun numFavoriteByMovieId(movieId: Int?): Int

    @Insert
    fun insertAll(vararg favorites: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query(value = "DELETE FROM favorite WHERE movie_id = :movieId")
    fun deleteByMovieId(movieId: Int): Int
}