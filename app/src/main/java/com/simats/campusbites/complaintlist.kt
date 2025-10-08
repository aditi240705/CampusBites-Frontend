package com.simats.campusbites

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class complaintlist : AppCompatActivity() {

    private lateinit var complaintRecycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaintlist)

        complaintRecycler = findViewById(R.id.complaintRecycler)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.emptyView)

        complaintRecycler.layoutManager = LinearLayoutManager(this)


        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        val userIdStr = sharedPref.getString("user_id", "0")
        val userId = userIdStr?.toIntOrNull() ?: 0
        if (userId == 0) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            fetchComplaints(userId)
        }
    }



    private fun fetchComplaints(userId: Int) {
        Log.d("ComplaintList", "Fetching complaints for userId: $userId")
        progressBar.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        complaintRecycler.visibility = View.GONE

        retrofit.instance.getComplaints(userId)
            .enqueue(object : Callback<ComplaintResponse> {
                override fun onResponse(
                    call: Call<ComplaintResponse>,
                    response: Response<ComplaintResponse>
                ) {
                    progressBar.visibility = View.GONE
                    Log.d("ComplaintList", "Retrofit response: $response")

                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.d("ComplaintList", "Response body: $res")

                        if (res != null && res.complaints.isNotEmpty()) {
                            Log.d("ComplaintList", "Complaints fetched: ${res.complaints.size}")
                            complaintRecycler.adapter = ComplaintAdapter(res.complaints.toMutableList())
                            complaintRecycler.visibility = View.VISIBLE
                            emptyView.visibility = View.GONE
                        } else {
                            Log.d("ComplaintList", "No complaints found")
                            emptyView.text = "No complaints found"
                            emptyView.visibility = View.VISIBLE
                            complaintRecycler.visibility = View.GONE
                        }
                    } else {
                        val errorStr = response.errorBody()?.string()
                        Log.e("ComplaintList", "Response failed: $errorStr")
                        emptyView.text = "❌ Failed: ${response.message()}"
                        emptyView.visibility = View.VISIBLE
                        complaintRecycler.visibility = View.GONE
                        Toast.makeText(
                            this@complaintlist,
                            "❌ Failed: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ComplaintResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    emptyView.text = "⚠️ Error: ${t.message}"
                    emptyView.visibility = View.VISIBLE
                    complaintRecycler.visibility = View.GONE
                    Log.e("ComplaintList", "Retrofit onFailure: ${t.message}", t)
                    Toast.makeText(
                        this@complaintlist,
                        "⚠️ Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
