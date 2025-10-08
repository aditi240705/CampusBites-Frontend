package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Admindashboard : AppCompatActivity() {

    private lateinit var tvUserIdValue: TextView
    private lateinit var tvCanteenNameValue: TextView
    private lateinit var tvIssueSummaryValue: TextView
    private lateinit var rgDecision: RadioGroup
    private lateinit var etReason: EditText
    private lateinit var btnSubmitResolution: Button
    private lateinit var btnLogout: Button   // 🔹 Added logout button

    private var complaintId: Int = 0  // will come from ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admindashboard)

        // Bind views
        tvUserIdValue = findViewById(R.id.tvUserIdValue)
        tvCanteenNameValue = findViewById(R.id.tvVendorIdValue)   // reuse VendorId TextView for Canteen Name
        tvIssueSummaryValue = findViewById(R.id.tvIssueSummaryValue)
        rgDecision = findViewById(R.id.rgDecision)
        etReason = findViewById(R.id.etReason)
        btnSubmitResolution = findViewById(R.id.btnSubmitResolution)
        btnLogout = findViewById(R.id.btnLogout) // 🔹 Initialize logout button

        // Get extras from intent (sent from ComplaintAdapter)
        complaintId = intent.getIntExtra("complaint_id", 0)
        val userId = intent.getStringExtra("user_id") ?: "--"
        val canteenName = intent.getStringExtra("canteen_name") ?: "--"
        val issueSummary = intent.getStringExtra("issue_summary") ?: "--"

        // Fill UI
        tvUserIdValue.text = userId
        tvCanteenNameValue.text = canteenName
        tvIssueSummaryValue.text = issueSummary

        // Handle submit
        btnSubmitResolution.setOnClickListener {
            submitResolution()
        }

        // 🔹 Handle Logout
        btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()  // Clear saved login/session data

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun submitResolution() {
        val selectedId = rgDecision.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Please select Solve or Deny", Toast.LENGTH_SHORT).show()
            return
        }

        val decision = if (selectedId == R.id.rbSolve) "solve" else "deny"
        val reason = etReason.text.toString().trim()

        if (reason.isEmpty()) {
            Toast.makeText(this, "Please enter a reason", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.137.1:8080/campusbite/resolve_issue.php" // replace with your system IP

        Log.d("Admindashboard", "Submitting resolution: complaintId=$complaintId, decision=$decision, reason=$reason")

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Log.d("Admindashboard", "Server Response: $response")
                try {
                    val json = JSONObject(response)
                    if (json.has("success") && json.getBoolean("success")) {
                        Toast.makeText(this, "Resolution submitted", Toast.LENGTH_LONG).show()
                        finish() // close screen and go back to complaints list
                    } else {
                        Toast.makeText(this, json.optString("error", "Failed to submit"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e("Admindashboard", "JSON Parsing Error: ${e.message}")
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                val errorMsg = error?.message ?: "Unknown error"
                Log.e("Admindashboard", "Volley Error: $errorMsg", error)
                Toast.makeText(this, "Network error: $errorMsg", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["complaint_id"] = complaintId.toString()  // send complaint id to backend
                params["decision"] = decision
                params["reason"] = reason
                Log.d("Admindashboard", "POST Params: $params")
                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
