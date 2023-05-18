package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("_id")
    var id: String,
    @SerializedName("rating")
    var rating: Int,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("restaurantId")
    var restaurantId: String
)
