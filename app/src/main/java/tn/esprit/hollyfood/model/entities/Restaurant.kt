package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("_id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("phoneNumber")
    var phoneNumber: Int,
    @SerializedName("description")
    var description: String,
    @SerializedName("userId")
    var userId: String,
)

