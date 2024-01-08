package com.example.lockapp.notes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import com.example.lockapp.R
import com.example.lockapp.databinding.FragmentNotesListBinding
import com.example.lockapp.db.NoteDatabase
import com.example.lockapp.notes.adapter.NotesInfoAdapter
import com.example.lockapp.selectApps.data.AppListData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class NotesListFragment : Fragment() {


    private lateinit var notesDetailsBinding: FragmentNotesListBinding


    // Views
    private lateinit var notesRv: RecyclerView
    private lateinit var floatingBtn: FloatingActionButton
    private lateinit var noNotesFoundText: TextView

    val adapter = NotesInfoAdapter()

    companion object {
        @JvmStatic
        fun getInstance(): NotesListFragment {
            return NotesListFragment()
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        notesDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes_list, container, false
        )

        notesDetailsBinding.apply {
            notesRv = notesRecyclerView
            floatingBtn = addNotesData
            noNotesFoundText = textV
        }

        floatingBtn.setOnClickListener {
            replaceFragment(NotesDetailsFragment.getInstance())
        }

        notesRv.layoutManager = LinearLayoutManager(requireContext())

        // Setting the Adapter with the recyclerview
        notesRv.adapter = adapter

        observeNotes()

        return notesDetailsBinding.root
    }

    private fun observeNotes() {
        val noteDatabase = NoteDatabase.getDatabase(requireContext()).noteDao()
        lifecycleScope.launch {
            noteDatabase.getNotes().collect { notesList ->
                if (notesList.isNotEmpty()) {
                    adapter.loadList(notesList)
                    noNotesFoundText.visibility = View.GONE
                } else {
                    noNotesFoundText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    @TypeConverter
    fun fromSelectedAppsJson(json: String): List<AppListData> {
        return Gson().fromJson(
            json, object : TypeToken<List<AppListData>>() {}.type
        )
    }
}
