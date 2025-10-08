package com.simats.campusbites.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.simats.campusbites.R
import com.simats.campusbites.responses.MenuItem
import com.simats.campusbites.retrofit.ApiService
import com.simats.campusbites.retrofit.retrofit
import com.simats.campusbites.retrofit.retrofit.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Menu2Adapter(
    private val context: Context,
    private val items: List<MenuItem>,
    private val userId: String
) : RecyclerView.Adapter<Menu2Adapter.MenuViewHolder>() {

    // Track quantities for UI
    private val quantities = IntArray(items.size) { 0 }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.imgMasalaDosa)
        val foodName: TextView = itemView.findViewById(R.id.txtMasalaDosaName)
        val foodPrice: TextView = itemView.findViewById(R.id.txtMasalaDosaPrice)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlusMasalaDosa)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinusMasalaDosa)
        val qtyText: TextView = itemView.findViewById(R.id.qtyMasalaDosa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.canteenitems, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]

        holder.foodName.text = item.food_name
        holder.foodPrice.text = "₹${item.price}"

        val imageUrl = if (!item.image.isNullOrEmpty() && item.image.startsWith("http")) {
            item.image
        } else {
            BASE_URL + (item.image ?: "")
        }
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.landscape_placeholder)
            .error(R.drawable.landscape_placeholder)
            .into(holder.foodImage)

        // Show current quantity
        holder.qtyText.text = quantities[position].toString()

        // ✅ Increment → call API
        holder.btnPlus.setOnClickListener {
            val newQty = quantities[position] + 1
            updateCart(item.food_name, newQty) {
                quantities[position] = newQty
                holder.qtyText.text = newQty.toString()
            }
        }

        // ✅ Decrement → call API
        holder.btnMinus.setOnClickListener {
            if (quantities[position] > 0) {
                val newQty = quantities[position] - 1
                updateCart(item.food_name, newQty) {
                    quantities[position] = newQty
                    holder.qtyText.text = newQty.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // ✅ Call backend to update cart
    private fun updateCart(foodName: String, quantity: Int, onSuccess: () -> Unit) {
        retrofit.instance.addToCart(userId, foodName, quantity)
            .enqueue(object : Callback<ApiService.AddCartResponse> {
                override fun onResponse(
                    call: Call<ApiService.AddCartResponse>,
                    response: Response<ApiService.AddCartResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess()
                        Toast.makeText(context, "$foodName added to cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update cart", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiService.AddCartResponse>, t: Throwable) {
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
