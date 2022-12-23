package com.example.notes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val title:String?,
    val note:String?,
    val date:String?
):Serializable