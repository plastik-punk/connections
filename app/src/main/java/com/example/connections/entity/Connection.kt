package com.example.connections.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Connection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var round: Int,
    var info: String,
    var difficulty: Int
)
