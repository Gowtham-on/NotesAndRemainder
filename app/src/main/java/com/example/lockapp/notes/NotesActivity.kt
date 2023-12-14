package com.example.lockapp.notes

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lockapp.R
import com.example.lockapp.databinding.ActivityMainBinding
import com.example.lockapp.db.NoteDatabase
import com.example.lockapp.details.DetailsActivity
import com.example.lockapp.notes.adapter.NotesInfoAdapter
import com.example.lockapp.service.ForegroundService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Views
    private lateinit var notesRv: RecyclerView
    private lateinit var floatingBtn: FloatingActionButton

    val adapter = NotesInfoAdapter()

    private val noteDatabase by lazy { NoteDatabase.getDatabase(this).noteDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            notesRv = notesRecyclerV
            floatingBtn = addNotesData
        }

        setContentView(binding.root)

        val tv = findViewById<TextView>(R.id.textV)

        tv.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
            ForegroundService.startService(this)
        }

        floatingBtn.setOnClickListener {
            // Go to next Activity
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }

        notesRv.layoutManager = LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        notesRv.adapter = adapter

        observeNotes()

    }

    private fun observeNotes() {
        lifecycleScope.launch {
            noteDatabase.getNotes().collect { notesList ->
                if (notesList.isNotEmpty()) {
                    adapter.loadList(notesList)
                }
            }
        }
    }

}