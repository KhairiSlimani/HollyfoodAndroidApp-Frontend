package tn.esprit.hollyfood.util

sealed class Validation() {
    object Success : Validation()
    data class Failed(val message: String) : Validation()
}
data class UserFieldsState(
    val fullname: Validation,
    val email: Validation,
    val password: Validation,
    val phone: Validation
)

data class RestaurantFieldsState(
    val name: Validation,
    val address: Validation,
    val phoneNumber: Validation,
    val description: Validation
)

data class PlateFieldsState(
    val name: Validation,
    val price: Validation
)

data class OrderFieldsState(
    val address: Validation,
    val phoneNumber: Validation
)

