package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class Plate(
    @SerializedName("_id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("category")
    var category: String,
    @SerializedName("price")
    var price: Float,
    @SerializedName("image")
    var image: String,
    @SerializedName("restaurantId")
    var restaurantId: String,
)
