package com.example.connections.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.connections.entity.Connection
import com.example.connections.entity.Word

@Dao
interface WordConnectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnection(connection: Connection): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Query("SELECT * FROM Connection WHERE round = :round")
    suspend fun getConnectionsByRound(round: Int): List<Connection>

    @Query("SELECT * FROM Word WHERE connectionId = :connectionId")
    suspend fun getWordsByConnection(connectionId: Long): List<Word>
}