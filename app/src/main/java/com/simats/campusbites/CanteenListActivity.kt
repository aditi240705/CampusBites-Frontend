package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.campusbites.responses.CanteenResponse
import com.simats.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CanteenListActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioMain: RadioButton
    private lateinit var radioEngineering: RadioButton
    private lateinit var radioScience: RadioButton
    private lateinit var radioLibrary: RadioButton
    private lateinit var continueButton: androidx.appcompat.widget.AppCompatButton

    private var studentId: String? = null  // from previous page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteenlist)

        // bind views
        radioGroup = findViewById(R.id.canteenRadioGroup)
        radioMain = findViewById(R.id.radio_main)
        radioEngineering = findViewById(R.id.radio_engineering)
        radioScience = findViewById(R.id.radio_science)
        radioLibrary = findViewById(R.id.radio_library)
        continueButton = findViewById(R.id.continueButton)

        // ✅ Back button
        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this, welcome1::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        // get studentId passed from login or signup
        studentId = intent.getStringExtra("studentId")

        // fetch from API
        loadCanteens()

        // handle continue click
        continueButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a canteen", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadio = findViewById<RadioButton>(selectedId)
                val canteenName = selectedRadio.text.toString()

                Log.d("CanteenlistActivity", "StudentId: $studentId, Canteen: $canteenName")

                // navigate to MenuList1Activity
                val intent = Intent(this, menulist1::class.java)
                intent.putExtra("studentId", studentId)
                intent.putExtra("canteenName", canteenName)
                startActivity(intent)
            }
        }
    }

    private fun loadCanteens() {
        retrofit.instance.getCanteens().enqueue(object : Callback<CanteenResponse> {
            override fun onResponse(call: Call<CanteenResponse>, response: Response<CanteenResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val canteens = response.body()!!.canteens

                    if (canteens.size > 0) radioMain.text = canteens[0].canteen_name
                    if (canteens.size > 1) radioEngineering.text = canteens[1].canteen_name
                    if (canteens.size > 2) radioScience.text = canteens[2].canteen_name
                    if (canteens.size > 3) radioLibrary.text = canteens[3].canteen_name
                } else {
                    Toast.makeText(this@CanteenListActivity, "No canteens found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CanteenResponse>, t: Throwable) {
                Toast.makeText(this@CanteenListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}