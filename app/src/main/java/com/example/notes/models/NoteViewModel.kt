package com.example.notes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.db.NoteDatabase
import com.example.notes.repository.NotesRepository
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

class NoteViewModel(application: Application):AndroidViewModel(application) {

    private val repository: NotesRepository
    val allnotes:LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allnotes = repository.allNotes
    }
    fun deleteNote(note:Note)=viewModelScope.launch {
        repository.delete(note)
    }
    fun insertNode(note:Note)=viewModelScope.launch {
        repository.insert(note)
    }
    fun updateNode(note:Note)=viewModelScope.launch {
        repository.insert(note)
    }
}