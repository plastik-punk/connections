package com.example.connections.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.connections.dao.WordConnectionDao
import com.example.connections.entity.Connection
import com.example.connections.entity.Word

@Database(entities = [Connection::class, Word::class], version = 1)
abstract class WordConnectionDatabase : RoomDatabase() {

    abstract fun wordConnectionDao(): WordConnectionDao

    companion object {
        @Volatile
        private var INSTANCE: WordConnectionDatabase? = null

        fun getDatabase(context: Context): WordConnectionDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordConnectionDatabase::class.java,
                    "word_connection_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}