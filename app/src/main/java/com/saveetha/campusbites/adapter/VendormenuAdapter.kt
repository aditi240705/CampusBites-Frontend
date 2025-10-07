package com.saveetha.campusbites.adapter

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
import com.saveetha.campusbites.R
import com.saveetha.campusbites.responses.MenuItem
import com.saveetha.campusbites.retrofit.ApiService
import com.saveetha.campusbites.retrofit.retrofit
import com.saveetha.campusbites.retrofit.retrofit.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendormenuAdapter(
    private val context: Context,
    private val items: List<MenuItem>,
    private val userId: String
) : RecyclerView.Adapter<VendormenuAdapter.MenuViewHolder>() {

    // Track quantities for UI
    private val quantities = IntArray(items.size) { 0 }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.imgMasalaDosa)
        val foodName: TextView = itemView.findViewById(R.id.txtMasalaDosaName)
        val foodPrice: TextView = itemView.findViewById(R.id.txtMasalaDosaPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vendormenuitems, parent, false)
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


    }

    override fun getItemCount(): Int = items.size


}
