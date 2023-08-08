package com.example.noteapp.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.databinding.NoteviewBinding
import com.example.noteapp.room.Note
import com.example.noteapp.ui.viewmodel.NoteViewModel

class NoteAdapter(
    private val notes:MutableList<Note>,
    private val viewModel: NoteViewModel
):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(NoteviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.apply {
            title.text = notes[position].title.toString()
            details.text = notes[position].Details.toString()
            buttonDelete.setOnClickListener {
                viewModel.deleteNote(notes[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteViewHolder(val binding:NoteviewBinding):RecyclerView.ViewHolder(binding.root)
}