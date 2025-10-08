package com.simats.campusbites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VendorComplaintAdapter(
    private var complaints: MutableList<Complaint>,
    private val onResolveClick: (Complaint, Int) -> Unit
) : RecyclerView.Adapter<VendorComplaintAdapter.ComplaintViewHolder>() {

    class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTransaction: TextView = itemView.findViewById(R.id.tvTransactionId)
        val tvIssueType: TextView = itemView.findViewById(R.id.tvIssueType)
        val tvDetails: TextView = itemView.findViewById(R.id.tvIssueDetails)
        val tvCanteen: TextView = itemView.findViewById(R.id.tvCanteenName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnResolve: Button = itemView.findViewById(R.id.btnResolve)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_vendor_complaint, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = complaints[position]
        holder.tvTransaction.text = "Txn: ${complaint.transaction_id}"
        holder.tvIssueType.text = "Issue: ${complaint.issue_type}"
        holder.tvDetails.text = complaint.issue_details
        holder.tvCanteen.text = "Canteen: ${complaint.canteen_name}"
        holder.tvStatus.text = "Status: ${complaint.status}"

        holder.btnResolve.setOnClickListener {
            onResolveClick(complaint, position)
        }
    }

    override fun getItemCount(): Int = complaints.size

    fun setData(newData: List<Complaint>) {
        complaints.clear()
        complaints.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        complaints.removeAt(position)
        notifyItemRemoved(position)
    }
}
