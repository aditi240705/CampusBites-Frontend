import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.campusbites.R

class ReviewAdapter(private val reviewList: List<ReviewModel1>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // ✅ Inflate the row layout (item_review.xml)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.tvCanteenName.text = review.canteenName
        holder.tvFoodName.text = review.foodName
        holder.tvReview.text = review.review
        holder.tvReviewDate.text = review.reviewDate
    }

    override fun getItemCount(): Int = reviewList.size

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCanteenName: TextView = itemView.findViewById(R.id.tvCanteenName)
        val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        val tvReview: TextView = itemView.findViewById(R.id.tvReview)
        val tvReviewDate: TextView = itemView.findViewById(R.id.tvReviewDate)
    }
}
