package com.example.lockapp.notes

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lockapp.R
import com.example.lockapp.databinding.ActivityMainBinding
import com.example.lockapp.notes.fragment.NotesListFragment

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

//        val tv = findViewById<TextView>(R.id.textV)
//
//        tv.setOnClickListener {
//            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//            startActivity(intent)
//            ForegroundService.startService(this)
//        }

        replaceFragment(NotesListFragment.getInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


}