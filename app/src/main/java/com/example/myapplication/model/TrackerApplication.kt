package com.example.myapplication.model

import android.app.Application

class TrackerApplication: Application() {
    private val _database by lazy { TrackerDatabase.getTrackerDatabase(this) }
    val repository by lazy { TrackerRepository(_database.trackerDao()) }
}