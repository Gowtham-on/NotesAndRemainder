package com.example.lockapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lockapp.databinding.ActivityMainBinding
import com.example.lockapp.service.ForegroundService
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Views
    private lateinit var notesRv: RecyclerView
    private lateinit var floatingBtn: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setContentView(binding.root)

        val tv = findViewById<TextView>(R.id.textV)

        tv.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
            ForegroundService.startService(this)
        }

        floatingBtn.setOnClickListener {
            // Go to next Activity

        }


    }


}