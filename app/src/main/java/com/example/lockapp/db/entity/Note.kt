package com.example.lockapp.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val noteId: Long,
    @ColumnInfo(name = "noteSubject") val subject: String,
    @ColumnInfo(name = "noteContent") val content: String,
    @ColumnInfo(name = "noteTime") val time: String,
    @ColumnInfo(name = "selectedApps") val selectedApps: String,
)