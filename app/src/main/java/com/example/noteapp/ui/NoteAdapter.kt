package com.example.noteapp.ui

import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.ui.viewmodel.NoteViewModel

class NoteAdapter(
    val notes:List<com.example.noteapp.room.Note>
):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.noteview,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val title = holder.itemView.findViewById<TextView>(R.id.title)
        val details = holder.itemView.findViewById<TextView>(R.id.title)
        title.text = notes[position].toString()
        details.text = notes[position].toString()
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
}