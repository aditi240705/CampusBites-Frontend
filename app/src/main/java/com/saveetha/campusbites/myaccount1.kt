package com.saveetha.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class myaccount1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_myaccount1)

        // ✅ Back button → navigates to activity_welcome1
        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, welcome1::class.java)
            startActivity(intent)
            finish() // optional → prevents returning to this screen on back press
        }

        // ✅ Report Issue button
        val reportIssueButton: Button = findViewById(R.id.report_issue_button)
        reportIssueButton.setOnClickListener {
            val intent = Intent(this, reportcomplaint2::class.java)
            startActivity(intent)
        }

        // ✅ View Complaints button → opens complaintlist
        val viewComplaintsButton: Button = findViewById(R.id.view_complaints_button)
        viewComplaintsButton.setOnClickListener {
            val intent = Intent(this, complaintlist::class.java)
            startActivity(intent)
        }

        // ✅ Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
