package com.saveetha.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class review1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_review1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ UI elements
        val canteenInput = findViewById<AutoCompleteTextView>(R.id.canteen_input)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        val reviewInput = findViewById<EditText>(R.id.review_input)
        val submitButton = findViewById<Button>(R.id.submit_button)

        // ✅ Debug: Show selected stars immediately
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this, "You selected $rating stars", Toast.LENGTH_SHORT).show()
        }

        submitButton.setOnClickListener {
            val canteenName = canteenInput.text.toString().trim()
            val rating = ratingBar.rating.toInt()
            val reviewText = reviewInput.text.toString().trim()

            // For now, dummy food_name (PHP requires it)
            val foodName = "Sample Food"

            if (canteenName.isEmpty() || rating == 0) {
                Toast.makeText(this, "Please enter canteen and rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Send data to PHP
            val url = "https://qstwc6s9-80.inc1.devtunnels.ms/campusbite/add_review.php"

            val request = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    try {
                        val json = JSONObject(response)
                        val status = json.getString("status")
                        val message = json.getString("message")

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                        if (status == "success") {
                            val intent = Intent(this, reviewsubmitted1::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Parse error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["food_name"] = foodName
                    params["canteen_name"] = canteenName
                    params["rating"] = rating.toString()
                    params["review"] = reviewText
                    return params
                }
            }

            Volley.newRequestQueue(this).add(request)
        }
    }
}
