package com.example.movieapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.db.dao.FavoriteDao
import com.example.movieapp.db.entity.Favorite

@Database(entities = arrayOf(Favorite::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}