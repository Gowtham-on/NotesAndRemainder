package com.example.lockapp.db.entity

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lockapp.selectApps.data.AppListData

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val noteId: Long,
    @ColumnInfo(name = "noteSubject") val subject: String,
    @ColumnInfo(name = "noteContent") val content: String,
    @ColumnInfo(name = "noteTime") val time: String,
    @ColumnInfo(name = "selectedApps") val selectedApps: String,
)