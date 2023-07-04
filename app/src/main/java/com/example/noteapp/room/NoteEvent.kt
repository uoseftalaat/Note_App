package com.example.noteapp.room

import com.example.noteapp.other.SortingTypes

sealed interface NoteEvent{
    object SaveContent: NoteEvent
    data class SetTitle(val title:String):NoteEvent
    data class SetDetails(val Details:String):NoteEvent
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
    data class SortNotes(val sortType:SortingTypes):NoteEvent
    data class DeleteNote(val note:Note):NoteEvent
}