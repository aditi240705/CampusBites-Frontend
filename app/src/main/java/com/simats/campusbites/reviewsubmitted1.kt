package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class reviewsubmitted1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reviewsubmitted1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Find the done button
        val doneButton = findViewById<Button>(R.id.done_button)

        // ✅ Set click listener to navigate to activity_welcome1
        doneButton.setOnClickListener {
            val intent = Intent(this, welcome1::class.java)
            startActivity(intent)
            finish() // optional → prevents returning to this screen on back press
        }
    }
}
