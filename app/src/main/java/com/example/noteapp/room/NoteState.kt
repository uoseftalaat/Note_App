package com.example.noteapp.room

import com.example.noteapp.other.SortingTypes

data class NoteState (
    val Noteslist:List<Note> = emptyList<Note>(),
    val title:String = "",
    val details:String = "",
    val isAddingNewNote:Boolean = false,
    val isDeleteingNote:Boolean = false,
    val isUpdatingNote:Boolean = false,
    val sortType:SortingTypes = SortingTypes.SORT_BY_TIME
)