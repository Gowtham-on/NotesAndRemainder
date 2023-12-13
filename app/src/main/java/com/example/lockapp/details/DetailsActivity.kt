package com.example.lockapp.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.lockapp.R
import com.example.lockapp.databinding.ActivityDetailsBinding
import com.example.lockapp.selectApps.SelectAppsActivity

class DetailsActivity : AppCompatActivity() {

    private lateinit var detailsBinding: ActivityDetailsBinding

    // Views
    private lateinit var subject: EditText
    private lateinit var content: EditText
    private lateinit var selectApps: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)


        detailsBinding.apply {
            subject = subjectEt
            content = contentEt
            selectApps = selectAppsBtn
        }

        setContentView(detailsBinding.root)


        selectApps.setOnClickListener {
            val intent = Intent(this, SelectAppsActivity::class.java)
            startActivity(intent)
        }
    }
}