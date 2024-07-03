package com.example.connections.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Connection::class,
            parentColumns = ["id"],
            childColumns = ["connectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var value: String,
    var connectionId: Long
) {
    override fun toString(): String {
        return value
    }
}

