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
    @ColumnInfo(name = "selectedDay") val day: Int,
    @ColumnInfo(name = "selectedMonth") val month: Int,
    @ColumnInfo(name = "selectedYear") val year: Int,
    @ColumnInfo(name = "selectedHour") val hour: Int,
    @ColumnInfo(name = "selectedMin") val min: Int,
)