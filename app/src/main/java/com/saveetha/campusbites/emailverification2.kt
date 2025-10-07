package com.saveetha.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.campusbites.responses.BasicResponse
import com.saveetha.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class emailverification2 : AppCompatActivity() {

    private lateinit var otpField: EditText
    private lateinit var verifyButton: Button
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailverification2)

        otpField = findViewById(R.id.otpField)
        verifyButton = findViewById(R.id.verifyButton)

        // Get email from previous activity
        email = intent.getStringExtra("email")
        val emailText = findViewById<TextView>(R.id.emailText)
        emailText.text = email ?: "example@college.edu"

        verifyButton.setOnClickListener {
            val otp = otpField.text.toString().trim()

            if (otp.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            } else if (otp.length != 6) { // Assuming OTP is 6 digits
                Toast.makeText(this, "Enter a 6-digit OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyOtp(email!!, otp)
            }
        }
    }

    private fun verifyOtp(email: String, otp: String) {
        retrofit.instance.verifyOtp(email, otp)
            .enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!
                        Toast.makeText(this@emailverification2, result.message, Toast.LENGTH_LONG).show()

                        // ✅ Check boolean true instead of "success"
                        if (result.status == true || result.status.toString() == "success") {
                            val intent = Intent(this@emailverification2, createnewpwd1::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@emailverification2, "Server error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Toast.makeText(this@emailverification2, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
