package com.app.mvvmdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.mvvmdemo.model.Note
import com.app.mvvmdemo.repo.NoteRepository

open class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var noteRepository: NoteRepository? = null
    private var allNotes: LiveData<List<Note>>? = null

    init {
        noteRepository = NoteRepository(application)
        allNotes = noteRepository?.getAllNotes()
    }

    fun insertNote(note: Note) {
        noteRepository?.insert(note)
    }

    fun updateNote(note: Note) {
        noteRepository?.update(note)
    }

    fun deleteNote(note: Note) {
        noteRepository?.delete(note)
    }

    fun deleteAllNotes() {
        noteRepository?.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note>>? {
        return allNotes
    }
}