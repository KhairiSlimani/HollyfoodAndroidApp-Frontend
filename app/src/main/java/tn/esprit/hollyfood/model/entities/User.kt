package tn.esprit.hollyfood.model.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    var id: String,
    @SerializedName("fullname")
    var fullname: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("phone")
    var phone: Int,
    @SerializedName("role")
    var role: String,
    @SerializedName("image")
    var image: String,

    )

data class EditProfileRequest(
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: Int
)
