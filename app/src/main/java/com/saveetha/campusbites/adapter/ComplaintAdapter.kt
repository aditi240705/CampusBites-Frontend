package com.saveetha.campusbites

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComplaintAdapter(private val complaints: List<Complaint>) :
    RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvComplaintId: TextView = itemView.findViewById(R.id.tvComplaintId)
        val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        val tvIssueType: TextView = itemView.findViewById(R.id.tvIssueType)
        val tvIssueDetails: TextView = itemView.findViewById(R.id.tvIssueDetails)
        val tvCanteenName: TextView = itemView.findViewById(R.id.tvCanteenName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnResolve: Button = itemView.findViewById(R.id.btnResolve)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_complaint_item, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val item = complaints[position]
        holder.tvComplaintId.text = "Complaint ID: ${item.id}"
        holder.tvTransactionId.text = "Transaction ID: ${item.transaction_id}"
        holder.tvIssueType.text = "Issue: ${item.issue_type}"
        holder.tvIssueDetails.text = "Details: ${item.issue_details}"
        holder.tvCanteenName.text = "Canteen: ${item.canteen_name}"
        holder.tvStatus.text = "Status: ${item.status}"

        holder.btnResolve.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, Admindashboard::class.java).apply {
                putExtra("complaint_id", item.id)
                putExtra("user_id", item.user_id)                 // send user ID
                putExtra("canteen_name", item.canteen_name)       // send canteen name
                putExtra("issue_summary", item.issue_details)     // send issue details
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = complaints.size
}
