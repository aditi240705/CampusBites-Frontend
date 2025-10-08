package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.simats.campusbites.retrofit.ApiService
import com.simats.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class reportcomplaint2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportcomplaint2)

        val transactionIdInput = findViewById<EditText>(R.id.edit_transaction_id)
        val radioGroup = findViewById<RadioGroup>(R.id.issue_radio_group)
        val issueDetailsInput = findViewById<EditText>(R.id.edit_additional_details)
        val canteenNameInput = findViewById<EditText>(R.id.CanteenName)
        val submitButton = findViewById<Button>(R.id.submit_complaint_button)

        submitButton.setOnClickListener {
            val transactionId = transactionIdInput.text.toString().trim()
            val selectedIssueId = radioGroup.checkedRadioButtonId
            val issueType = if (selectedIssueId != -1) {
                findViewById<RadioButton>(selectedIssueId).text.toString()
            } else {
                ""
            }
            val issueDetails = issueDetailsInput.text.toString().trim()
            val canteenName = canteenNameInput.text.toString().trim()

            // ✅ fetch user_id from SharedPreferences
            val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
            val userId = sharedPref.getString("user_id", "") ?: ""

            if (transactionId.isEmpty() || issueType.isEmpty() || canteenName.isEmpty() || userId.isEmpty()) {
                Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call API with all fields
            retrofit.instance.submitComplaint(
                userId,
                transactionId,
                issueType,
                issueDetails,
                canteenName
            ).enqueue(object : Callback<ApiService.SubmitComplaintResponse> {
                override fun onResponse(
                    call: Call<ApiService.SubmitComplaintResponse>,
                    response: Response<ApiService.SubmitComplaintResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!
                        Toast.makeText(
                            this@reportcomplaint2,
                            "✅ ${res.message}\nETA: ${res.resolution_eta ?: "N/A"}",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@reportcomplaint2, complaintsubmitted1::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@reportcomplaint2,
                            "❌ Failed: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiService.SubmitComplaintResponse>, t: Throwable) {
                    Toast.makeText(
                        this@reportcomplaint2,
                        "⚠️ Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}
