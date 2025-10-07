package com.saveetha.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class paymentsuccess1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paymentsuccess1)

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Get data from Intent
        val transactionId = intent.getStringExtra("transaction_id") ?: "N/A"
        val amount = intent.getIntExtra("amount", 0)
        val method = intent.getStringExtra("method") ?: "Unknown"
        val pickupTime = intent.getStringExtra("pickup_time") ?: "N/A"

        // ✅ Update UI dynamically
        findViewById<TextView>(R.id.orderId).text = "#$transactionId"
        findViewById<TextView>(R.id.paidAmount).text = "₹$amount"
        findViewById<TextView>(R.id.amount).text = "₹$amount"
        findViewById<TextView>(R.id.paymentMethod).text = method


        // ✅ Home button -> welcome1 (clear back stack so user can’t go back to payment screen)
        findViewById<Button>(R.id.home).setOnClickListener {
            val intent = Intent(this, welcome1::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // ✅ Review button -> review1 with order details
        findViewById<Button>(R.id.review).setOnClickListener {
            val intent = Intent(this, review1::class.java)
            intent.putExtra("transaction_id", transactionId)
            intent.putExtra("amount", amount)
            startActivity(intent)
        }
    }
}
