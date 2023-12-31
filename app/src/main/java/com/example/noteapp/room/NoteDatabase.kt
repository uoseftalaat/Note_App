package com.example.noteapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun dao(): NoteDao
}