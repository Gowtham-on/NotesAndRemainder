package com.example.lockapp.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import com.example.lockapp.R
import com.example.lockapp.db.entity.Note
import com.example.lockapp.selectApps.data.AppListData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class NotesInfoAdapter() : RecyclerView.Adapter<NotesInfoAdapter.ViewHolder>() {

    private var notesInfoList = listOf<Note>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val subject: TextView = view.findViewById(R.id.notes_subject)
        val content: TextView = view.findViewById(R.id.notes_content)
        val time: TextView = view.findViewById(R.id.time_tv)
        val appName: TextView = view.findViewById(R.id.app_name_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_notes_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesInfoList.size
    }

    fun loadList(list: List<Note>) {
        notesInfoList = ArrayList()
        notesInfoList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notesInfoList[position]

        if (item.content.isEmpty()) {
            holder.content.text = "---"
        } else {
            holder.content.text = item.content
        }

        if (item.subject.isEmpty()) {
            holder.subject.text = "---"
        } else {
            holder.subject.text = item.subject
        }

        holder.time.text = "${item.day}/${item.month}/${item.year}, ${item.hour}:${item.min}:00"

        val jsonObject = JSONObject(item.selectedApps)
        val appName = jsonObject.getString("appName")
        holder.appName.text = holder.itemView.context.getString(R.string.selected_apps, appName)

    }

    @TypeConverter
    fun fromSelectedAppsJson(json: String): AppListData {
        return Gson().fromJson(
            json, object : TypeToken<AppListData>() {}.type
        )
    }
}