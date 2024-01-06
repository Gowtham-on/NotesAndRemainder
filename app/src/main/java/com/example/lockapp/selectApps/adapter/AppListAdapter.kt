package com.example.lockapp.selectApps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lockapp.R
import com.example.lockapp.selectApps.data.AppListData

class AppListAdapter : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    private lateinit var notesInfoList: List<AppListData>

    private lateinit var selectedApps: AppListData

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val appName: TextView = view.findViewById(R.id.app_title)
        val packageName: TextView = view.findViewById(R.id.package_name)
        val checkBox: CheckBox = view.findViewById(R.id.cbLabel_single)
        val icon: ImageView = view.findViewById(R.id.appIconImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_app_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesInfoList.size
    }

    fun loadList(list: ArrayList<AppListData>) {
        notesInfoList = ArrayList()
        notesInfoList = list
    }

    fun getSelectedAppsList(): AppListData {
        return selectedApps
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notesInfoList[position]

        holder.appName.text = item.appName
        holder.packageName.text = item.packageName

        holder.checkBox.isSelected = false
        holder.checkBox.isChecked = false

        if (this::selectedApps.isInitialized && item.packageName == selectedApps.packageName) {
            holder.checkBox.isSelected = true
            holder.checkBox.isChecked = true
        }

        holder.itemView.setOnClickListener {
            holder.checkBox.isSelected = !holder.checkBox.isChecked
            holder.checkBox.isChecked = !holder.checkBox.isChecked
            if (holder.checkBox.isSelected) {
                selectedApps = item
            }
            notifyDataSetChanged()

        }
        Glide.with(holder.itemView.context).load(item.icon).into(holder.icon)
    }
}