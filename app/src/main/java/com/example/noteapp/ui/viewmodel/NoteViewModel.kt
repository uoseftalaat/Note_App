package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.other.SortingTypes
import com.example.noteapp.room.Note
import com.example.noteapp.room.NoteDao
import com.example.noteapp.room.NoteEvent
import com.example.noteapp.room.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val dao: NoteDao
):ViewModel() {
    private val _state = MutableStateFlow(NoteState())
    private val _sorttype = MutableStateFlow(SortingTypes.SORT_BY_TIME)
    private val _notes = _sorttype.flatMapLatest {sortType ->
        when(sortType){
            SortingTypes.SORT_BY_TITLE -> dao.getNotesByTitle()
            SortingTypes.SORT_BY_TIME -> dao.getNotesByTime()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sorttype,_notes){state , sorttype , notes ->
        state.copy(
            sortType = sorttype,
            Noteslist = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())
    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            NoteEvent.HideDialog -> _state.update {
                it.copy(
                    isAddingNewNote = false
                )
            }
            NoteEvent.SaveContent -> {
                val title = state.value.title
                val details = state.value.details
                if(title.isBlank() || details.isBlank()){
                    return
                }
                val newNote = Note(title = title , Details = details)
                viewModelScope.launch {
                    dao.UpsertNote(newNote)
                }
                _state.update {
                    it.copy(
                        isAddingNewNote = false,
                        title = "" ,
                        details = ""
                    )
                }
            }
            is NoteEvent.SetDetails -> _state.update {
                it.copy(
                    details = event.Details
                )
            }
            is NoteEvent.SetTitle -> _state.update {
                it.copy(
                    title = event.title
                )
            }
            NoteEvent.ShowDialog -> _state.update {
                it.copy(
                    isAddingNewNote = true
                )
            }
            is NoteEvent.SortNotes -> _sorttype.value = event.sortType
        }
    }
}