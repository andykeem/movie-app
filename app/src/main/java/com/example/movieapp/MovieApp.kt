package com.example.movieapp

import android.app.Application
import androidx.room.Room
import com.example.movieapp.db.AppDatabase

const val DATABASE_NAME = "movie.db"
class MovieApp : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        initDatabase()
    }

    private fun initDatabase() {
        db = Room.databaseBuilder(
            this, AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
}