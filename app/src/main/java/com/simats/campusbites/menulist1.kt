package com.simats.campusbites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.campusbites.adapter.Menu2Adapter
import com.simats.campusbites.adapter.MenuAdapter
import com.simats.campusbites.responses.MenuItem
import com.simats.campusbites.responses.MenuResponse
import com.simats.campusbites.retrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class menulist1 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView

    private var adapter: MenuAdapter? = null
    private var adapter2: Menu2Adapter? = null
    private var menuList: List<MenuItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menulist1)

        recyclerView = findViewById(R.id.menu_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView2 = findViewById(R.id.menu_frequent_recycler)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // ✅ Get userId from SharedPreferences
        val sharedPref = getSharedPreferences("CampusBitePrefs", MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", null) ?: run {
            Log.e("MenuList1", "User not logged in")
            finish()
            return
        }

        val canteenName = intent.getStringExtra("canteenName") ?: ""

        // ✅ Set canteen name in TextView
        val titleView = findViewById<TextView>(R.id.menuTitle)
        titleView.text = if (canteenName.isNotEmpty()) canteenName else "Menu"

        // ✅ Call API to get menu
        retrofit.instance.getMenu(canteenName).enqueue(object : Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    menuList = response.body()!!.menu

                    // --- Reorder main menu: Masala Dosa, Pizza, Chapathi first ---
                    val priorityItems = listOf("Masala Dosa", "Pizza", "Chapathi")
                    val sortedMenu = menuList.sortedWith(compareBy { item ->
                        val index = priorityItems.indexOf(item.food_name)
                        if (index == -1) Int.MAX_VALUE else index
                    })

                    adapter = MenuAdapter(this@menulist1, sortedMenu, userId)
                    recyclerView.adapter = adapter

                    // --- Frequently ordered: pick top 2, prioritizing Masala Dosa, Pizza, Chapathi ---
                    val frequentList = menuList
                        .sortedWith(compareBy { item ->
                            val index = priorityItems.indexOf(item.food_name)
                            if (index == -1) Int.MAX_VALUE else index
                        })
                        .take(2)

                    adapter2 = Menu2Adapter(this@menulist1, frequentList, userId)
                    recyclerView2.adapter = adapter2

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
            startActivity(Intent(this, cart::class.java))
        }
    }
}
