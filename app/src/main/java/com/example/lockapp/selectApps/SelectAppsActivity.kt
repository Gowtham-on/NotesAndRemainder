package com.example.lockapp.selectApps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lockapp.R
import com.example.lockapp.databinding.ActivitySelectAppsBinding
import com.example.lockapp.selectApps.adapter.AppListAdapter
import com.example.lockapp.selectApps.data.AppListData


class SelectAppsActivity : AppCompatActivity() {

    private lateinit var selectAppsBinding: ActivitySelectAppsBinding

    // Views
    private lateinit var appsListRv: RecyclerView
    private lateinit var cancelBtn: Button
    private lateinit var selectBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectAppsBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_apps)

        selectAppsBinding.apply {
            appsListRv = appsRecyclerV
            cancelBtn = cancel
            selectBtn = select
        }

        setContentView(selectAppsBinding.root)

        cancelBtn.setOnClickListener {
            finish()
        }

        selectBtn.setOnClickListener {

        }

        val packageManager = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val appList = packageManager.queryIntentActivities(intent, 0)

        val data = ArrayList<AppListData>()

        for (resolveInfo in appList) {
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val packageName = resolveInfo.activityInfo.packageName

            val appIcon = resolveInfo.loadIcon(packageManager)

            // Process the app
            data.add(
                AppListData(
                    appName, packageName, appIcon
                )
            )
        }

        appsListRv.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        val adapter = AppListAdapter()
        adapter.loadList(data)

        // Setting the Adapter with the recyclerview
        appsListRv.adapter = adapter

    }
}