package com.saveetha.campusbites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.campusbites.retrofit.ApiService
import com.saveetha.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class addmenu1 : AppCompatActivity() {

    private val TAG = "AddMenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmenu1)

        val etFoodName = findViewById<EditText>(R.id.etFoodName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // ✅ Back button navigation
        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this, vendordashboard1::class.java)
            startActivity(intent)
            finish()
        }

        // ✅ Fetch email from SharedPreferences
        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        if (email == null) {
            Toast.makeText(this, "Error: No email found. Please log in again.", Toast.LENGTH_LONG).show()
            Log.e(TAG, "Email not found in SharedPreferences")
            return
        }

        btnSubmit.setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val price = etPrice.text.toString().trim()

            if (foodName.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Validation failed: foodName or price is empty")
                return@setOnClickListener
            }

            Log.d(TAG, "Sending request → email=$email, foodName=$foodName, price=$price")

            retrofit.instance.addMenuItem(email, foodName, price)
                .enqueue(object : Callback<ApiService.ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiService.ApiResponse>,
                        response: Response<ApiService.ApiResponse>
                    ) {
                        Log.d(TAG, "Raw response: $response")
                        val body = response.body()
                        if (body != null && body.status == "success") {
                            Log.i(TAG, "Success: ${body.message}")
                            Toast.makeText(applicationContext, body.message, Toast.LENGTH_SHORT).show()
                            etFoodName.text.clear()
                            etPrice.text.clear()

                            val intent = Intent(this@addmenu1, vendordashboard1::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e(TAG, "Error response: ${body?.error ?: "Unknown error"}")
                            Toast.makeText(applicationContext, body?.error ?: "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiService.ApiResponse>, t: Throwable) {
                        Log.e(TAG, "Network failure: ${t.message}", t)
                        Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    // ✅ Also override device back button to go to vendordashboard1
    override fun onBackPressed() {
        val intent = Intent(this, vendordashboard1::class.java)
        startActivity(intent)
        finish()
    }
}
