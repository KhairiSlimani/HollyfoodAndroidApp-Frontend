package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class Orderline (
    @SerializedName("_id")
    var id: String,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("price")
    var price: Float,
    @SerializedName("plateId")
    var plateId: String,
    @SerializedName("orderId")
    var orderId: String,
    @SerializedName("plateName")
    var plateName: String,
    @SerializedName("plateCategory")
    var plateCategory: String,
    @SerializedName("plateImage")
    var plateImage: String,
    var unitPrice: Float
)