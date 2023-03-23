package tn.esprit.hollyfood.util

import android.util.Patterns

fun validateFullName(fullname: String): RegisterValidation{
    if (fullname.isEmpty())
        return RegisterValidation.Failed("Fullname can't be empty")

    if (fullname.length < 6)
        return RegisterValidation.Failed("Fullname must contain at least 6 characters")

    return RegisterValidation.Success
}

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email can't be empty")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if (password.isEmpty())
        return RegisterValidation.Failed("Password can't be empty")

    if (password.length < 6)
        return RegisterValidation.Failed("Password must contain at least 6 characters")

    return RegisterValidation.Success
}

fun validatePhoneNumber(phone: String): RegisterValidation{
    if (phone == "-1")
        return RegisterValidation.Failed("The phone number can't be empty")

    if (phone.length < 8 || phone.length > 8)
        return RegisterValidation.Failed("The phone number must contain exactly 8 digits")

    return RegisterValidation.Success
}
