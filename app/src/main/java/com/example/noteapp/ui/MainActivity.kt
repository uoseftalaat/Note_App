package com.example.noteapp.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.databinding.DialogBinding
import com.example.noteapp.other.Constant.TABLE_NAME
import com.example.noteapp.room.Note
import com.example.noteapp.room.NoteDatabase
import com.example.noteapp.ui.viewmodel.NoteViewModel
import java.util.zip.Inflater


class MainActivity : AppCompatActivity() {
    lateinit var db:NoteDatabase
    lateinit var binding: ActivityMainBinding
    private val viewModel:NoteViewModel by viewModels<NoteViewModel> {
        object :ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteViewModel(db.dao()) as T
            }
        }
    }
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            TABLE_NAME
        ).build()
        val notes: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf(Note(title = "do", Details = "this")))
        val size = viewModel.notes.value?.size
        viewModel.notes.observe(this) { it ->
            adapter = NoteAdapter(it, viewModel)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
        binding.addButton.setOnClickListener{
            addNoteDialog(this).show()
        }
    }

    fun addNoteDialog(context: Context): AlertDialog {
        val binding = DialogBinding.inflate(LayoutInflater.from(context))
        val noteDialog = AlertDialog.Builder(context)
            .setTitle("Add new Note")
            .setView(binding.root)
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Add") { _, _ ->
                viewModel.addNote(Note(
                    title = binding.addTitle.text.toString(),
                    Details = binding.addDetails.text.toString()
                ))
            }
            .create()
        return noteDialog
    }


}