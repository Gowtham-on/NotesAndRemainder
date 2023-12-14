package com.example.lockapp.details

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.lockapp.R
import com.example.lockapp.databinding.ActivityDetailsBinding
import com.example.lockapp.db.NoteDatabase
import com.example.lockapp.db.entity.Note
import com.example.lockapp.selectApps.SelectAppsActivity
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var detailsBinding: ActivityDetailsBinding

    // Views
    private lateinit var subject: EditText
    private lateinit var content: EditText
    private lateinit var selectApps: Button
    private lateinit var saveBtn: Button

    private val noteDatabase by lazy { NoteDatabase.getDatabase(this).noteDao() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)


        detailsBinding.apply {
            subject = subjectEt
            content = contentEt
            selectApps = selectAppsBtn
            saveBtn = okBtn
        }

        setContentView(detailsBinding.root)

        selectApps.setOnClickListener {
            val intent = Intent(this, SelectAppsActivity::class.java)
            startActivity(intent)
        }

        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                noteDatabase.addNote(
                    Note(
                        System.currentTimeMillis(),
                        subject.text.toString(),
                        content.text.toString(),
                        "14 Dec"
                    )
                )
            }
            finish()
        }
    }
}