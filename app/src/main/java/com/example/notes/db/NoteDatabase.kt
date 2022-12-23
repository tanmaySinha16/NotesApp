package com.example.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.models.Note
import com.example.notes.util.Constants.Companion.DATABASE_NAME
import java.time.Instant

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase:RoomDatabase() {

    abstract fun getNoteDao() : NoteDao

    companion object{
        @Volatile
        private var INSTANCE:NoteDatabase?=null

        fun getDatabase(context: Context):NoteDatabase{

            return INSTANCE?:synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                instance

            }
        }
    }
}