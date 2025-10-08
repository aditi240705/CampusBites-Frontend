package com.simats.campusbites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Vcomplaintadapter(
    private val complaints: MutableList<Complaint>,
    private val onResolveClick: (Complaint, Int) -> Unit
) : RecyclerView.Adapter<Vcomplaintadapter.ComplaintViewHolder>() {

    class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvComplaintId: TextView = itemView.findViewById(R.id.tvComplaintId)
        val tvTransaction: TextView = itemView.findViewById(R.id.tvTransactionId)
        val tvIssueType: TextView = itemView.findViewById(R.id.tvIssueType)
        val tvDetails: TextView = itemView.findViewById(R.id.tvIssueDetails)
        val tvCanteen: TextView = itemView.findViewById(R.id.tvCanteenName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnResolve: Button = itemView.findViewById(R.id.btnResolve)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_complaint, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = complaints[position]
        holder.tvComplaintId.text = "Complaint ID: ${complaint.id}"
        holder.tvTransaction.text = "Txn: ${complaint.transaction_id}"
        holder.tvIssueType.text = "Issue: ${complaint.issue_type}"
        holder.tvDetails.text = "Details: ${complaint.issue_details}"
        holder.tvCanteen.text = "Canteen: ${complaint.canteen_name}"
        holder.tvStatus.text = "Filed on: ${complaint.created_at}"

        holder.btnResolve.setOnClickListener {
            onResolveClick(complaint, position)
        }
    }

    override fun getItemCount(): Int = complaints.size
}
