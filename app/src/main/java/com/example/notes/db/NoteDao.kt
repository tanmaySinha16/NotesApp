package com.example.notes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes.models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note:Note)

    @Query("SELECT * FROM notes_table order by id ASC")
    fun getAllNotes():LiveData<List<Note>>

    @Query("Update notes_table Set title = :title,note = :note WHERE id=:id")
    suspend fun update(id:Int?,title:String?,note:String?)
}