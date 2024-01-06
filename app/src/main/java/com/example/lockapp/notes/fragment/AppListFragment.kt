package com.example.lockapp.notes.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lockapp.R
import com.example.lockapp.databinding.FragmentAppListBinding
import com.example.lockapp.notes.NotesViewmodel
import com.example.lockapp.selectApps.adapter.AppListAdapter
import com.example.lockapp.selectApps.data.AppListData

class AppListFragment : Fragment() {

    private lateinit var appListBinding: FragmentAppListBinding

    // Views
    private lateinit var appsListRv: RecyclerView
    private lateinit var cancelBtn: Button
    private lateinit var selectBtn: Button

    private val notesVm: NotesViewmodel by activityViewModels()
    val adapter = AppListAdapter()

    companion object {
        @JvmStatic
        fun getInstance(): AppListFragment {
            return AppListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        appListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_app_list, container, false
        )

        appListBinding.apply {
            appsListRv = appsRecyclerV
            cancelBtn = cancel
            selectBtn = select
        }

        cancelBtn.setOnClickListener {
            replaceFragment(NotesDetailsFragment.getInstance())
        }

        selectBtn.setOnClickListener {
            Log.e("gowtham", "observeSelectedApp: $notesVm", )
            notesVm.setSelectedAppsList(adapter.getSelectedAppsList())
            replaceFragment(NotesDetailsFragment.getInstance())
        }

        getInstalledApps()

        return appListBinding.root
    }

    private fun getInstalledApps() {
        val packageManager = requireActivity().packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val appList = packageManager.queryIntentActivities(intent, 0)

        val data = ArrayList<AppListData>()

        for (resolveInfo in appList) {
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val packageName = resolveInfo.activityInfo.packageName

            val appIcon = resolveInfo.loadIcon(packageManager)

            data.add(
                AppListData(
                    appName, packageName, appIcon
                )
            )
        }
        appsListRv.layoutManager = LinearLayoutManager(requireContext())

        adapter.loadList(data)
        appsListRv.adapter = adapter
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }
}