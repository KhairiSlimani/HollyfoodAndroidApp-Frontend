package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class Order (
    @SerializedName("_id")
    var id: String,
    @SerializedName("price")
    var price: Float,
    @SerializedName("address")
    var address: String,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("restaurantId")
    var restaurantId: String
)