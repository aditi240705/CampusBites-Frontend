package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.campusbites.retrofit.retrofit
import com.simats.campusbites.responses.LoginResponse

class LoginActivity : AppCompatActivity() {

    private lateinit var loginbtn: Button
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signupField: TextView
    private lateinit var forgotPasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user already logged in
        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val role = sharedPref.getString("role", null)

        if (isLoggedIn && role != null) {
            when (role.lowercase()) {
                "vendor" -> startActivity(Intent(this, vendordashboard1::class.java))
                "admin" -> startActivity(Intent(this, Admindashboard::class.java))
                else -> startActivity(Intent(this, welcome1::class.java))
            }
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        loginbtn = findViewById(R.id.loginButton)
        emailField = findViewById(R.id.emailEditText)
        passwordField = findViewById(R.id.passwordEditText)
        signupField = findViewById(R.id.signupText)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)

        // Signup redirect
        signupField.setOnClickListener {
            startActivity(Intent(this, AccountcreationActivity::class.java))
        }

        // Forgot password redirect
        forgotPasswordText.setOnClickListener {
            startActivity(Intent(this, ForgotPwd::class.java))
        }

        // Login button
        loginbtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                checkLogin(email, password)
            }
        }
    }

    private fun checkLogin(email: String, password: String) {
        retrofit.instance.login(email, password)
            .enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                    Log.d("LOGIN_DEBUG", "Raw response: ${response.raw()}")

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Log.d("LOGIN_DEBUG", "Successful Body: $loginResponse")

                        if (loginResponse != null) {
                            if (loginResponse.status == "success" && loginResponse.user != null) {
                                val user = loginResponse.user
                                val role = user.role
                                val email = user.email

                                Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                                saveUserToPreferences(user.user_id.toString(), email, role)

                                when (role.lowercase()) {
                                    "vendor" -> startActivity(Intent(this@LoginActivity, vendordashboard1::class.java))
                                    "admin" -> startActivity(Intent(this@LoginActivity, complaintlist::class.java))
                                    else -> startActivity(Intent(this@LoginActivity, welcome1::class.java))
                                }
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("LOGIN_DEBUG", "Response body is null despite being successful.")
                            Toast.makeText(this@LoginActivity, "No response from server", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorBodyString = response.errorBody()?.string()
                        Log.e("LOGIN_DEBUG", "Unsuccessful response code: ${response.code()}")
                        Log.e("LOGIN_DEBUG", "Raw error body: $errorBodyString")
                        Toast.makeText(this@LoginActivity, "Server error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                    Log.e("LOGIN_DEBUG", "Network or conversion error: ${t.message}", t)
                    Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveUserToPreferences(userId: String, email: String, role: String) {
        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", userId)
            putString("email", email)
            putString("role", role)
            putBoolean("is_logged_in", true)
            apply()
        }
    }
}
