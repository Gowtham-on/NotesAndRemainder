package com.example.lockapp.notes

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lockapp.R
import com.example.lockapp.databinding.ActivityMainBinding
import com.example.lockapp.notes.fragment.NotesListFragment
import com.example.lockapp.service.ForegroundService


class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Views
    private lateinit var frame: FrameLayout


    val notesVm: NotesViewmodel by lazy {
        ViewModelProvider(this).get(
            NotesViewmodel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            frame = frameLayout
        }

        setContentView(binding.root)


        val permission = getUsageStatsPermissionsStatus(this)
        if (permission == PermissionStatus.GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            ForegroundService.startService(this)
        } else if (permission == PermissionStatus.DENIED) {
            Toast.makeText(this, "Need Usage Access Permission", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

        replaceFragment(NotesListFragment.getInstance())
    }

    fun getUsageStatsPermissionsStatus(context: Context): PermissionStatus? {
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) return PermissionStatus.CANNOT_BE_GRANTED
        val appOps = context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        val granted =
            if (mode == AppOpsManager.MODE_DEFAULT) context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED else mode == AppOpsManager.MODE_ALLOWED
        return if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
    }


    enum class PermissionStatus {
        GRANTED,
        DENIED,
        CANNOT_BE_GRANTED
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


}