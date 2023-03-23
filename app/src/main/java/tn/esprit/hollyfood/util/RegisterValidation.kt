package tn.esprit.hollyfood.util

sealed class RegisterValidation(){
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldsState(
    val fullname: RegisterValidation,
    val email: RegisterValidation,
    val password: RegisterValidation,
    val phone: RegisterValidation
)