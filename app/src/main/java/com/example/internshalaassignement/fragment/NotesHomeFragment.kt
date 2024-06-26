package com.example.internshalaassignement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.internshalaassignement.R
import com.example.internshalaassignement.adapter.NotesAdapter
import com.example.internshalaassignement.databinding.FragmentNotesHomeBinding
import com.example.internshalaassignement.db.NotesDatabaseHelper


class NotesHomeFragment : Fragment() {

    private lateinit var binding: FragmentNotesHomeBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        db = NotesDatabaseHelper(requireContext())
        notesAdapter = NotesAdapter(db.getAllNotes(), requireContext())

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = notesAdapter


        binding.addButton.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_notesHomeFragment_to_anyNoteDetailFragment, Bundle().apply {
                    putBoolean("IS_NEW", true)
                })
        }
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}

