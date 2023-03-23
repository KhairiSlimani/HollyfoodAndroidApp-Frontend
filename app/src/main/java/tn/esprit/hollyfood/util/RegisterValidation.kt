package tn.esprit.hollyfood.util

sealed class Validation() {
    object Success : Validation()
    data class Failed(val message: String) : Validation()
}
data class FieldsState(
    val fullname: Validation,
    val email: Validation,
    val password: Validation,
    val phone: Validation
)


