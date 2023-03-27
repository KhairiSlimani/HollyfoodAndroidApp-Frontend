package tn.esprit.hollyfood.util

import android.util.Patterns

fun validateFullName(fullname: String): Validation {
    if (fullname.isEmpty())
        return Validation.Failed("Fullname can't be empty.")

    if (fullname.length < 6)
        return Validation.Failed("Use 6 characters or more for your full name.")

    return Validation.Success
}

fun validateEmail(email: String): Validation {
    if (email.isEmpty())
        return Validation.Failed("Email can't be empty.")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return Validation.Failed("Wrong email format.")

    return Validation.Success
}

fun validatePassword(password: String, confirmPassword: String): Validation {
    if (password.isEmpty()) {
        return Validation.Failed("Password can't be empty.")
    } else {
        if (password.length < 6)
            return Validation.Failed("Use 6 characters or more for your password")
        else if (confirmPassword.isEmpty()) {
            return Validation.Failed("Confirm your password.")
        }
        else if (password != confirmPassword){
            return Validation.Failed("Passwords do not match.")
        }
    }

    return Validation.Success
}

fun validatePhoneNumber(phone: String): Validation {
    if (phone == "-1")
        return Validation.Failed("The phone number can't be empty")

    if (phone.length < 8 || phone.length > 8)
        return Validation.Failed("The phone number must contain exactly 8 digits")

    return Validation.Success
}
