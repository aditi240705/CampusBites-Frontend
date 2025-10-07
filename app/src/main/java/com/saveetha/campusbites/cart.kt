package com.saveetha.campusbites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.saveetha.campusbites.adapter.CartAdapter
import com.saveetha.campusbites.retrofit.ApiService
import com.saveetha.campusbites.retrofit.retrofit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class cart : AppCompatActivity(), PaymentResultListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalText: TextView
    private lateinit var continuePaymentBtn: Button
    private lateinit var timeSlotSpinner: Spinner
    private var adapter: CartAdapter? = null
    private var userId: String? = null
    private var grandTotal: Int = 0
    private var selectedPickupTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRecyclerView)
        totalText = findViewById(R.id.totalText)
        continuePaymentBtn = findViewById(R.id.continuePaymentBtn)
        timeSlotSpinner = findViewById(R.id.timeSlotSpinner)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupTimeSlots()

        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        userId = sharedPref.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchCart(userId!!)

        continuePaymentBtn.setOnClickListener {
            if (grandTotal > 0) {
                if (selectedPickupTime == null) {
                    Toast.makeText(this, "Please select a pickup time", Toast.LENGTH_SHORT).show()
                } else {
                    startPayment(grandTotal)
                }
            } else {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTimeSlots() {
        val timeSlots = listOf(
            "Select Pickup Time",
            "8:00","8:30","9:00","9:30","10:00","10:30","11:00",
            "11:30 AM", "12:00 PM", "12:30 PM", "01:00 PM", "01:30 PM",
            "02:00 PM", "02:30 PM", "03:00 PM"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            timeSlots
        )
        timeSlotSpinner.adapter = adapter

        timeSlotSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                selectedPickupTime = if (position == 0) null else parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedPickupTime = null
            }
        }
    }

    private fun fetchCart(userId: String) {
        retrofit.instance.getCart(userId).enqueue(object : Callback<ApiService.CartResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ApiService.CartResponse>,
                response: Response<ApiService.CartResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val cartResponse = response.body()!!

                    if (cartResponse.cart_items.isNotEmpty()) {
                        adapter = CartAdapter(cartResponse.cart_items.toMutableList()) { updatedCart ->
                            grandTotal = updatedCart.sumOf { it.total_price }.toInt()
                            totalText.text = "₹$grandTotal"
                        }
                        recyclerView.adapter = adapter

                        // ✅ calculate grand total locally
                        grandTotal = cartResponse.cart_items.sumOf { it.total_price }.toInt()
                        totalText.text = "₹$grandTotal"
                    } else {
                        totalText.text = "Your cart is empty"
                    }
                } else {
                    Log.e("CartActivity", "API Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@cart, "Failed to load cart", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiService.CartResponse>, t: Throwable) {
                Log.e("CartActivity", "Network Failed: ${t.message}")
                Toast.makeText(this@cart, "Network error, try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startPayment(amount: Int) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_TqVsYNPoGcZowj")

        try {
            val options = JSONObject()
            options.put("name", "CampusBite")
            options.put("description", "Food Order (Pickup at $selectedPickupTime)")
            options.put("currency", "INR")
            options.put("amount", amount * 100)

            val prefill = JSONObject()
            prefill.put("email", "testuser@example.com")
            prefill.put("contact", "9999999999")
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful! ID: $razorpayPaymentID", Toast.LENGTH_LONG).show()
        val intent = Intent(this, paymentsuccess1::class.java)
        intent.putExtra("transaction_id", razorpayPaymentID)
        intent.putExtra("amount", grandTotal)
        intent.putExtra("pickup_time", selectedPickupTime)
        intent.putExtra("method", "Razorpay (UPI/Card)")
        startActivity(intent)
        finish()
    }

    override fun onPaymentError(code: Int, description: String?) {
        Toast.makeText(this, "Payment failed: $description", Toast.LENGTH_LONG).show()
    }
}
