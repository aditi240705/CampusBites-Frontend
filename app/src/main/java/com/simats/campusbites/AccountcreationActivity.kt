package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.simats.campusbites.retrofit.retrofit
import com.simats.campusbites.responses.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountcreationActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var roleGroup: RadioGroup
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountcreation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind Views
        fullNameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        mobileEditText = findViewById(R.id.mobileEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        roleGroup = findViewById(R.id.roleGroup)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            performSignup()
        }
    }

    private fun performSignup() {
        val username = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = mobileEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        val selectedRoleId = roleGroup.checkedRadioButtonId
        val role = when (selectedRoleId) {
            R.id.rbStudent -> "user"
            R.id.rbFaculty -> "vendor"
            R.id.rbAdmin -> "admin"   // ✅ Added Admin role support
            else -> ""
        }

        // Basic validation
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Log request details
        Log.d("SignupRequest", """
            Sending signup request:
            name: $username
            email: $email
            phone_number: $phone
            password: $password
            role: $role
        """.trimIndent())

        // Send request
        retrofit.instance.signup(
            name = username,
            email = email,
            phone = phone,
            password = password,
            role = role
        )
            .enqueue(object : Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                ) {
                    Log.d("SignupResponse", "HTTP Code: ${response.code()}")

                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()
                        Log.d("SignupResponse", "Response Body: $body")

                        val message = body?.message ?: "Unknown response"
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

                        if (message.contains("successful", ignoreCase = true)) {
                            startActivity(Intent(this@AccountcreationActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("SignupResponse", "Error Body: $errorBody")
                        Toast.makeText(applicationContext, "Signup failed. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Log.e("SignupError", "Network error: ${t.message}", t)
                    Toast.makeText(applicationContext, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}