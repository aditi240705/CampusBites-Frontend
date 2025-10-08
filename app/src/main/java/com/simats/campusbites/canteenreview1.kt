package com.simats.campusbites

import ReviewAdapter
import ReviewModel1
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class canteenreview1 : AppCompatActivity() {

    private var studentId: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewList: ArrayList<ReviewModel1>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_canteenreview1)

        // ✅ Get studentId from previous activity
        studentId = intent.getIntExtra("studentId", -1)

        // ✅ Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerReviews)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reviewList = ArrayList()
        reviewAdapter = ReviewAdapter(reviewList)
        recyclerView.adapter = reviewAdapter

        // ✅ Load reviews from backend
        fetchReviews()

        // ✅ Back to Home button setup
        findViewById<Button>(R.id.back).setOnClickListener {
            navigateToHome()
        }

        // ✅ Handle system bars properly
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // ✅ Fetch reviews from PHP API
    private fun fetchReviews() {
        val url = "http://192.168.136.30/campusbite/get_review.php" // 🔗 change IP if testing on real device

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    if (response.getString("status") == "success") {
                        val dataArray = response.getJSONArray("data")
                        reviewList.clear()

                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val canteenName = obj.getString("canteen_name")
                            val foodName = obj.getString("food_name")
                            val review = obj.getString("review")
                            val reviewDate = obj.getString("review_date")

                            reviewList.add(
                                ReviewModel1(canteenName, foodName, review, reviewDate)
                            )
                        }

                        reviewAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, "No reviews found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Parsing error: ${e.message}")
                    Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("API_ERROR", "Volley error: ${error.message}")
                Toast.makeText(this, "Failed to fetch reviews", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    // ✅ Single function for navigation
    private fun navigateToHome() {
        val intent = Intent(this, welcome1::class.java).apply {
            putExtra("studentId", studentId)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }
}
