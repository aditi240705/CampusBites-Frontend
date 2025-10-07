package com.saveetha.campusbites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.campusbites.adapter.MenuAdapter
import com.saveetha.campusbites.adapter.VendormenuAdapter
import com.saveetha.campusbites.responses.MenuItem
import com.saveetha.campusbites.responses.MenuResponse
import com.saveetha.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class vendormenu1 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: VendormenuAdapter? = null
    private var menuList: List<MenuItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendormenu1)

        recyclerView = findViewById(R.id.vendormenu_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ get userId from SharedPreferences
        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", null) ?: run {
            Log.e("MenuList1", "User not logged in")
            finish()
            return
        }

        val canteenName = intent.getStringExtra("canteenName") ?: ""

        // ✅ set canteen name in TextView
        val titleView = findViewById<TextView>(R.id.menuTitle)
        titleView.text = if (canteenName.isNotEmpty()) canteenName else "Menu"

        // ✅ Call API with selected canteen
        retrofit.instance.getMenu(canteenName).enqueue(object : Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    menuList = response.body()!!.menu
                    adapter = VendormenuAdapter(this@vendormenu1, menuList, userId)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("MenuList1", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failed: ${t.message}")
            }
        })

        // ✅ Cart button → Open cart activity
        val viewCartBtn = findViewById<Button>(R.id.viewCartBtn)
        viewCartBtn.setOnClickListener {
            startActivity(Intent(this, vendordashboard1::class.java))
        }
    }
}
