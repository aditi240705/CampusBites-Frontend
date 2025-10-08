package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class vendordashboard1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vendordashboard1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Buttons
        val addMenuBtn = findViewById<Button>(R.id.addmenu)
        val viewOrdersBtn = findViewById<Button>(R.id.vieworders)
        val profileBtn = findViewById<Button>(R.id.profile)

        // Navigation
        addMenuBtn.setOnClickListener {
            startActivity(Intent(this, addmenu1::class.java))
        }

        viewOrdersBtn.setOnClickListener {
            startActivity(Intent(this, liveorders1::class.java))
        }

        profileBtn.setOnClickListener {
            startActivity(Intent(this, vendorprofile1::class.java))
        }
    }
}
