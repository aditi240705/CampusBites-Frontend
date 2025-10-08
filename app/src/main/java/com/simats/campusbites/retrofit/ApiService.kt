package com.simats.campusbites.retrofit

import com.simats.campusbites.*
import com.simats.campusbites.responses.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ✅ Login API
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // ✅ Signup API
    @FormUrlEncoded
    @POST("signup.php")
    fun signup(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("phone_number") phone: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<SignupResponse>

    // ✅ Profile API
    @FormUrlEncoded
    @POST("get_profile.php")
    fun getUserProfile(
        @Field("user_id") userId: Int
    ): Call<ProfileResponse>

    // ✅ Canteens API
    @GET("get_canteenname.php")
    fun getCanteens(): Call<CanteenResponse>

    // ✅ Menu API
    @FormUrlEncoded
    @POST("get_menu.php")
    fun getMenu(
        @Field("canteen_name") canteenName: String
    ): Call<MenuResponse>

    // ✅ Cart APIs
    @FormUrlEncoded
    @POST("cart.php")
    fun addToCart(
        @Field("user_id") userId: String,
        @Field("food_name") foodName: String,
        @Field("quantity") quantity: Int
    ): Call<AddCartResponse>

    data class AddCartResponse(
        val cart_items: List<CartItem>,
        val grand_total: Double
    )

    @FormUrlEncoded
    @POST("cart.php")
    fun getCart(
        @Field("user_id") userId: String
    ): Call<CartResponse>

    data class CartResponse(
        val cart_items: List<CartItem>,
        val grand_total: Double
    )

    data class CartItem(
        val food_name: String,
        var quantity: Int,
        val price: Double,
        var total_price: Double
    )

    // ✅ Vendor APIs
    @FormUrlEncoded
    @POST("add_food.php")
    fun addMenuItem(
        @Field("email") email: String,
        @Field("food_name") foodName: String,
        @Field("price") price: String
    ): Call<ApiResponse>

    // ✅ Generic API Response (used for multiple endpoints)
    data class ApiResponse(
        val success: Boolean? = null,
        val status: String? = null,
        val message: String? = null,
        val error: String? = null,
        val canteen_name: String? = null,
        val complaint_id: Int? = null,
        val decision: String? = null,
        val reason: String? = null
    )

    // ✅ Complaint APIs
    @FormUrlEncoded
    @POST("report_complaint.php")
    fun submitComplaint(
        @Field("user_id") userId: String,
        @Field("transaction_id") transactionId: String,
        @Field("issue_type") issueType: String,
        @Field("issue_details") issueDetails: String,
        @Field("canteen_name") canteenName: String
    ): Call<SubmitComplaintResponse>

    data class SubmitComplaintResponse(
        val message: String,
        val resolution_eta: String
    )

    @FormUrlEncoded
    @POST("get_complaints.php")
    fun getComplaints(
        @Field("user_id") userId: Int
    ): Call<ComplaintResponse>

    @FormUrlEncoded
    @POST("resolve_issue.php")
    fun resolveIssue(
        @Field("complaint_id") complaintId: Int,
        @Field("decision") decision: String,
        @Field("reason") reason: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("verify_otp.php")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<BasicResponse>

    @FormUrlEncoded
    @POST("forgot_password.php")  // your forgot password endpoint
    fun forgotPassword(
        @Field("email") email: String
    ): Call<BasicResponse>

    @FormUrlEncoded
    @POST("get_review.php")
    fun getReviews(
        @Field("food_name") foodName: String?,
        @Field("canteen_name") canteenName: String?
    ): Call<ReviewResponse>



}
