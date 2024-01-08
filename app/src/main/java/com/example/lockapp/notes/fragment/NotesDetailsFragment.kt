package com.example.lockapp.notes.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.lockapp.R
import com.example.lockapp.databinding.FragmentNotesDetailsBinding
import com.example.lockapp.db.NoteDatabase
import com.example.lockapp.db.entity.Note
import com.example.lockapp.notes.NotesViewmodel
import com.example.lockapp.selectApps.data.AppListData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Calendar

class NotesDetailsFragment : Fragment() {

    private lateinit var notesDetailsBinding: FragmentNotesDetailsBinding

    // Views
    private lateinit var subject: EditText
    private lateinit var content: EditText
    private lateinit var selectApps: Button
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var appIcon: ImageView
    private lateinit var appName: TextView
    private lateinit var appDetailsLl: ConstraintLayout
    private lateinit var timeDateBtn: Button
    private lateinit var selectedTime: TextView

    private val notesVm: NotesViewmodel by activityViewModels()
    private var selectedApp = ""


    companion object {
        @JvmStatic
        fun getInstance(): NotesDetailsFragment {
            return NotesDetailsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        notesDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes_details, container, false
        )

        notesDetailsBinding.apply {
            subject = subjectEt
            content = contentEt
            selectApps = selectAppsBtn
            saveBtn = okBtn
            cancelBtn = cancelButton
            appIcon = appIconImageView
            appName = appTitle
            appDetailsLl = appDetailsLayout
            timeDateBtn = selectTimeDateBtn
            selectedTime = selectedTimeTv
        }

        val noteDatabase = NoteDatabase.getDatabase(requireContext()).noteDao()

        selectApps.setOnClickListener {
            notesVm.setNoteContent(content.text.toString())
            notesVm.setNoteSubject(subject.text.toString())
            replaceFragment(AppListFragment.getInstance())
        }

        cancelBtn.setOnClickListener {
            replaceFragment(NotesListFragment.getInstance())
        }

        timeDateBtn.setOnClickListener {
            pickDateTime()
        }

        saveBtn.setOnClickListener {

            if (subject.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Title is Empty", Toast.LENGTH_SHORT).show()
            } else if (content.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Content is Empty", Toast.LENGTH_SHORT).show()
            } else if (selectedApp.isEmpty()) {
                Toast.makeText(requireContext(), "No Apps are selected", Toast.LENGTH_SHORT).show()
            } else if(selectedTime.text == "") {
                Toast.makeText(requireContext(), "Select Date and Time", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    noteDatabase.addNote(
                        Note(
                            System.currentTimeMillis(),
                            subject.text.toString(),
                            content.text.toString(),
                            "14 Dec",
                            selectedApp,
                            notesVm.getSelectedTimeAndDate().day,
                            notesVm.getSelectedTimeAndDate().month,
                            notesVm.getSelectedTimeAndDate().year,
                            notesVm.getSelectedTimeAndDate().hour,
                            notesVm.getSelectedTimeAndDate().min,
                            notesVm.getSelectedTimeAndDate().millis,
                        )
                    )
                }
                appDetailsLl.visibility = View.GONE
                selectedApp = ""
                notesDetailsBinding.invalidateAll()
                replaceFragment(NotesListFragment.getInstance())
            }
        }

        return notesDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedApp()
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)


        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(year, month, day, hour, minute)
                    selectedTime.text = "$day/${month+1}/$year, $hour:$minute:00"

                    val calendar = Calendar.getInstance()
                    calendar[Calendar.HOUR_OF_DAY] = hour
                    calendar[Calendar.MINUTE] = minute
                    calendar[Calendar.SECOND] = 0
                    calendar[Calendar.MILLISECOND] = 0

                    val millis = calendar.timeInMillis
                    notesVm.setSelectedTimeAndDate(SelectedTimeAndDate(
                        hour,
                        minute,
                        year,
                        month+1,
                        day,
                        millis
                    ))

                },
                startHour,
                startMinute,
                false
            ).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun observeSelectedApp() {
        lifecycleScope.launch {
            notesVm.selectedApp.collect {
                appDetailsLl.visibility = View.VISIBLE
                subject.setText(notesVm.getNoteSubject())
                content.setText(notesVm.getNoteContent())
                selectedApp = listToJson(it)
                appName.text = it.appName
                Glide.with(requireContext()).load(it.icon).into(appIcon)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    private fun listToJson(value: AppListData): String {
        val gson = Gson()
        return gson.toJson(value)
    }
}

data class SelectedTimeAndDate(
    var hour: Int,
    var min: Int,
    var year: Int,
    var month: Int,
    var day: Int,
    var millis: Long
)
