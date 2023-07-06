package com.example.noteapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.room.Note
import com.example.noteapp.room.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch



class NoteViewModel(
    private val dao: NoteDao
):ViewModel() {
    var _notes = MutableLiveData<MutableList<Note>>()
    val notes:LiveData<MutableList<Note>>
        get() = _notes
    init{
        getNotesByTime()
    }
    fun deleteNote(note: Note){
        var data:MutableList<Note> = (_notes.value?.toMutableList() ?: emptyList()) as MutableList<Note>
        data.remove(note)
        _notes.value = data
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }
    fun addNote(note:Note){

        var data:MutableList<Note> = mutableListOf(note)
        data += _notes.value?.toMutableList() ?: emptyList()
        _notes.value = data
        viewModelScope.launch {
            dao.UpsertNote(note)
        }
    }

    fun getNotesByTime() {
        viewModelScope.launch {
            _notes.postValue(dao.getNotesByTime().toMutableList())
        }
    }

    fun getNotesByTitle(){
        viewModelScope.launch {
            _notes.postValue(dao.getNotesByTitle().toMutableList())
        }
    }

}