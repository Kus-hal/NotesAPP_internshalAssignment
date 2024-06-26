package com.example.internshalaassignement.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignement.R
import com.example.internshalaassignement.db.Note
import com.example.internshalaassignement.db.NotesDatabaseHelper
import com.example.internshalaassignement.fragment.AnyNoteDetailFragment

class NotesAdapter(private var notes: List<Note>, context: Context) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

        private val db : NotesDatabaseHelper = NotesDatabaseHelper(context)

        class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val titleTextView: TextView = itemView.findViewById(R.id.titelTextView)
            val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
            val root: CardView = itemView.rootView as CardView
            val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent,false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.root.setOnClickListener{
            it.findNavController().navigate(R.id.action_notesHomeFragment_to_anyNoteDetailFragment,
                Bundle().apply {
                    putInt("ID", notes[position].id)
                    putBoolean("IS_NEW", false)
                }
            )
        }

        holder.deleteButton.setOnClickListener{
            db.deleteNote(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData(newNotes: List<Note>)
    {
        notes = newNotes
        notifyDataSetChanged()
    }
}