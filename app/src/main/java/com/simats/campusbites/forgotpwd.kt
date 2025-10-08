package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.campusbites.retrofit.retrofit
import com.simats.campusbites.responses.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPwd : AppCompatActivity() {  // ✅ Class name updated

    private lateinit var emailInput: EditText
    private lateinit var sendOtpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpwd)

        emailInput = findViewById(R.id.emailInput)
        sendOtpButton = findViewById(R.id.continueButton)

        sendOtpButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
            } else {
                requestOtp(email)
            }
        }
    }

    private fun requestOtp(email: String) {
        retrofit.instance.forgotPassword(email)
            .enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!
                        Toast.makeText(this@ForgotPwd, result.message, Toast.LENGTH_LONG).show()

                        if (result.status == "success") {
                            val intent = Intent(this@ForgotPwd, emailverification2::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@ForgotPwd, "Server error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Toast.makeText(this@ForgotPwd, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
