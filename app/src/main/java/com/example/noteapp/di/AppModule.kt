package com.example.noteapp.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.noteapp.room.Note
import com.example.noteapp.room.NoteDao
import com.example.noteapp.room.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "Note Database"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideNoteDao(db: NoteDatabase) = db.dao
}