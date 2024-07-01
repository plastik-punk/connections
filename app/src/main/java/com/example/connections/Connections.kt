package com.example.connections

import android.app.Application
import com.example.connections.database.WordConnectionDatabase

class Connections : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
    }

    private fun initializeDatabase() {
        WordConnectionDatabase.getDatabase(this)
    }
}