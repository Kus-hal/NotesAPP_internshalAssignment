package com.example.internshalaassignement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.internshalaassignement.R
import com.example.internshalaassignement.databinding.FragmentAnyNoteDetailBinding
import com.example.internshalaassignement.db.Note
import com.example.internshalaassignement.db.NotesDatabaseHelper


class AnyNoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentAnyNoteDetailBinding
    private lateinit var db: NotesDatabaseHelper
    private val noteId: Int? by lazy { arguments?.getInt("ID") }
    private val isNew: Boolean by lazy { arguments?.getBoolean("IS_NEW")!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnyNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = NotesDatabaseHelper(requireContext())


        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            findNavController().popBackStack(R.id.anyNoteDetailFragment, true)
        }
        if (!isNew) {
            noteId?.let {
                val note = db.getNotesById(it)
                binding.titelEditText.setText(note.title)
                binding.contentEditText.setText(note.content)
            }
        }


        binding.saveButton.setOnClickListener {
            if (!isNew) {
                noteId?.let {
                    val newTitle = binding.titelEditText.text.toString()
                    val newContent = binding.contentEditText.text.toString()
                    val updatedNote = Note(it, newTitle, newContent)
                    db.updateNotes(updatedNote)
                    Toast.makeText(requireContext(), "Changes Saved!", Toast.LENGTH_SHORT).show()
                }
            } else {
                val title: String = binding.titelEditText.text.toString()
                val content: String = binding.contentEditText.text.toString()
                db.insertNote(Note(0, title, content))
                Toast.makeText(requireContext(), "Notes Added!", Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack(R.id.anyNoteDetailFragment, true)
        }
    }
}