package com.example.lockapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lockapp.db.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>


    @Insert
    suspend fun insertAll(notes: List<Note>)

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(notes: Note)


}