package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class Order (
    @SerializedName("_id")
    var id: String,
    @SerializedName("price")
    var price: Float,
    @SerializedName("address")
    var address: String,
    @SerializedName("phoneNumber")
    var phoneNumber: Int,
    @SerializedName("date")
    var date: String,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("restaurantId")
    var restaurantId: String,
    @SerializedName("restaurantName")
    var restaurantName: String

)