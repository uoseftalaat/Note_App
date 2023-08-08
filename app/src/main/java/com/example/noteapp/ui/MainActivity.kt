package com.example.noteapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
        createDatabase()
        viewModel.notes.observe(this) { it ->
            adapter = NoteAdapter(it, viewModel)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
        binding.apply {
            addButton.setOnClickListener {
                addNoteDialog(this@MainActivity).show()
            }
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.sort_by_title -> {
                        viewModel.getNotesByTitle()
                    }

                    R.id.sort_by_time -> {
                        viewModel.getNotesByTime()
                    }
                }
            }
        }
    }

    fun createDatabase(){
        db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            TABLE_NAME
        ).build()
    }

    fun addNoteDialog(context: Context): AlertDialog {
        val bind= DialogBinding.inflate(LayoutInflater.from(context))
        val noteDialog = AlertDialog.Builder(context)
            .setTitle("Add new Note")
            .setView(bind.root)
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Add") { _, _ ->
                viewModel.addNote(Note(
                    title = bind.addTitle.text.toString(),
                    Details = bind.addDetails.text.toString()
                ))
                if(binding.radioGroup.checkedRadioButtonId == R.id.sort_by_title){
                    viewModel.getNotesByTitle()
                }
                else{
                    viewModel.getNotesByTime()
                }
            }
            .create()
        return noteDialog
    }

}