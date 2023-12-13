package com.example.lockapp.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lockapp.R
import com.example.lockapp.notes.data.NotesInfo

class NotesInfoAdapter() : RecyclerView.Adapter<NotesInfoAdapter.ViewHolder>() {

    private lateinit var notesInfoList: List<NotesInfo>

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val subject: TextView = view.findViewById(R.id.notes_subject)
        val content: TextView = view.findViewById(R.id.notes_content)
        val time: TextView = view.findViewById(R.id.time_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_notes_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesInfoList.size
    }

    fun loadList(list: ArrayList<NotesInfo>) {
        notesInfoList = ArrayList()
        notesInfoList = list
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notesInfoList[position]

        holder.subject.text = item.subject
        holder.content.text = item.content
        holder.time.text = item.time

    }
}