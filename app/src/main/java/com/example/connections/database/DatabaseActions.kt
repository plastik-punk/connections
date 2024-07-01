package com.example.connections.database

import com.example.connections.entity.Connection
import com.example.connections.entity.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseActions {

    suspend fun initializeData(database: WordConnectionDatabase) {
        withContext(Dispatchers.IO) {
            if (database.wordConnectionDao().getConnectionsByRound(1).isEmpty()) {
                val round = 1
                val difficulties = listOf(0, 1, 2, 3) // Unique difficulties for each connection

                // Connection data (adjust as needed)
                val connectionData = listOf(
                    listOf("Button", "Text", "Image", "View"),
                    listOf("Layout", "Widget", "Theme", "Style"),
                    listOf("Activity", "Fragment", "Service", "Broadcast"),
                    listOf("Kotlin", "Java", "XML", "Gradle")
                )

                // Insert connections and words
                connectionData.forEachIndexed { index, words ->
                    val connection = Connection(
                        round = round,
                        info = "Connection ${index + 1}", // Add more descriptive info if needed
                        difficulty = difficulties[index]
                    )
                    val connectionId = database.wordConnectionDao().insertConnection(connection)
                    words.forEach { wordValue ->
                        val word = Word(value = wordValue, connectionId = connectionId)
                        database.wordConnectionDao().insertWord(word)
                    }
                }
            }
        }
    }
}