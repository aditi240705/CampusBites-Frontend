package com.saveetha.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class welcome1 : AppCompatActivity() {

    private var studentId: Int = -1  // store user_id
    private lateinit var profileButton: Button
    private lateinit var orderButton: Button
    private lateinit var accountButton: Button
    private lateinit var viewReviews: LinearLayout  // LinearLayout for view reviews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome1)

        // Receive user_id from previous activity
        studentId = intent.getIntExtra("studentId", -1)

        // Safety check
        if (studentId == -1) {
            // Handle missing user ID (optional)
        }

        // Profile button click
        profileButton = findViewById(R.id.profilebtn)
        profileButton.setOnClickListener {
            val intent = Intent(this, userprofile1::class.java)
            intent.putExtra("studentId", studentId)
            startActivity(intent)
        }

        // Order button click
        orderButton = findViewById(R.id.order)
        orderButton.setOnClickListener {
            val intent = Intent(this, CanteenListActivity::class.java)
            intent.putExtra("studentId", studentId)
            startActivity(intent)
        }

        // Account button click
        accountButton = findViewById(R.id.MYACCOUNT)
        accountButton.setOnClickListener {
            val intent = Intent(this, myaccount1::class.java)
            intent.putExtra("studentId", studentId)
            startActivity(intent)
        }

        // View reviews click
        viewReviews = findViewById(R.id.viewreviews)
        viewReviews.setOnClickListener {
            val intent = Intent(this, canteenreview1::class.java)
            intent.putExtra("studentId", studentId) // pass user_id
            startActivity(intent)
        }

        // Handle system bar insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
