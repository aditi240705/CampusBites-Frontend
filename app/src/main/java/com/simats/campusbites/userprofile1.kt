package com.simats.campusbites

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.simats.campusbites.responses.ProfileResponse
import com.simats.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class userprofile1 : AppCompatActivity() {

    private var studentId: Int = -1

    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var ordersContainer: LinearLayout
    private lateinit var backButton: ImageView
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_userprofile1)

        // ✅ Apply insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Get userId from SharedPreferences
        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        studentId = sharedPref.getString("user_id", null)?.toIntOrNull() ?: -1

        // ✅ Bind views
        txtName = findViewById(R.id.name)
        txtEmail = findViewById(R.id.email)
        txtPhone = findViewById(R.id.phn)
        ordersContainer = findViewById(R.id.orders)
        backButton = findViewById(R.id.backbtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        // ✅ Back button
        backButton.setOnClickListener { finish() }

        // ✅ Logout button
        logoutBtn.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@userprofile1, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        if (studentId != -1) {
            fetchUserProfile(studentId)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchUserProfile(userId: Int) {
        retrofit.instance.getUserProfile(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()!!
                    displayUserData(profile)
                } else {
                    Toast.makeText(this@userprofile1, "Error fetching profile", Toast.LENGTH_SHORT).show()
                    Log.e("PROFILE_DEBUG", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@userprofile1, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("PROFILE_DEBUG", "Failure: ${t.message}", t)
            }
        })
    }

    private fun displayUserData(profile: ProfileResponse) {
        // ✅ Profile info
        txtName.text = profile.user.username
        txtEmail.text = profile.user.email
        txtPhone.text = profile.user.phone_number

        // ✅ Clear old orders
        ordersContainer.removeAllViews()

        // ✅ Populate orders dynamically
        for (order in profile.orders) {
            val orderLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 16, 0, 16)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val foodName = TextView(this).apply {
                text = order.food_name
                textSize = 15f
                setTextColor(resources.getColor(android.R.color.black))
            }

            val canteenName = TextView(this).apply {
                text = if (order.canteen_name.isEmpty()) "Unknown Canteen" else order.canteen_name
                textSize = 13f
                setTextColor(resources.getColor(android.R.color.darker_gray))
            }

            orderLayout.addView(foodName)
            orderLayout.addView(canteenName)

            ordersContainer.addView(orderLayout)
        }

        // ✅ If no orders
        if (profile.orders.isEmpty()) {
            val emptyMsg = TextView(this).apply {
                text = "No previous orders found"
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.darker_gray))
            }
            ordersContainer.addView(emptyMsg)
        }
    }
}
