package com.saveetha.campusbites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.campusbites.R
import com.saveetha.campusbites.retrofit.ApiService

class CartAdapter(
    private val items: MutableList<ApiService.CartItem>, // mutable to update qty
    private val onCartUpdated: (List<ApiService.CartItem>) -> Unit // callback
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.cartItemName)
        val qty: TextView = itemView.findViewById(R.id.cartItemQuantity)
        val price: TextView = itemView.findViewById(R.id.cartItemPrice)
        val btnPlus: TextView = itemView.findViewById(R.id.btnPlus)
        val btnMinus: TextView = itemView.findViewById(R.id.btnMinus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.food_name
        holder.qty.text = item.quantity.toString()
        holder.price.text = "₹${item.total_price}"

        // Handle +
        holder.btnPlus.setOnClickListener {
            item.quantity += 1
            item.total_price = item.price * item.quantity // assuming you have `price` field
            notifyItemChanged(position)
            onCartUpdated(items)
        }

        // Handle –
        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity -= 1
                item.total_price = item.price * item.quantity
                notifyItemChanged(position)
                onCartUpdated(items)
            } else {
                // Optionally remove item if qty = 0
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)
                onCartUpdated(items)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
