public class ReviewModel1 {
    private String canteenName;
    private String foodName;
    private String review;
    private String reviewDate;

    public ReviewModel1(String canteenName, String foodName, String review, String reviewDate) {
        this.canteenName = canteenName;
        this.foodName = foodName;
        this.review = review;
        this.reviewDate = reviewDate;
    }

    public String getCanteenName() { return canteenName; }
    public String getFoodName() { return foodName; }
    public String getReview() { return review; }
    public String getReviewDate() { return reviewDate; }
}
