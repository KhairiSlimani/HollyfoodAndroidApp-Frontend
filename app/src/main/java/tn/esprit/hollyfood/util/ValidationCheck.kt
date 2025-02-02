package tn.esprit.hollyfood.util
import android.util.Patterns

//USER
fun validateUserFullName(fullname: String): Validation {
    if (fullname.isEmpty())
        return Validation.Failed("Fullname can't be empty.")

    if (fullname.length < 6)
        return Validation.Failed("Use 6 characters or more for your full name.")

    return Validation.Success
}

fun validateUserEmail(email: String): Validation {
    if (email.isEmpty())
        return Validation.Failed("Email can't be empty.")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return Validation.Failed("Wrong email format.")

    return Validation.Success
}

fun validateUserPassword(password: String, confirmPassword: String): Validation {
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

fun validateUserPhoneNumber(phone: String): Validation {
    if (phone == "-1")
        return Validation.Failed("The phone number can't be empty")

    if (phone.length < 8 || phone.length > 8)
        return Validation.Failed("The phone number must contain exactly 8 digits")

    return Validation.Success
}

//RESTAURANT
fun validateRestaurantName(name: String): Validation {
    if (name.isEmpty())
        return Validation.Failed("Restaurant name can't be empty.")

    if (name.length < 4)
        return Validation.Failed("Use 4 characters or more for restaurant name.")

    return Validation.Success
}

fun validateRestaurantAddress(name: String): Validation {
    if (name.isEmpty())
        return Validation.Failed("Restaurant address can't be empty.")

    if (name.length < 4)
        return Validation.Failed("Use 4 characters or more for restaurant address.")

    return Validation.Success
}

fun validateRestaurantPhoneNumber(phone: String): Validation {
    if (phone == "-1")
        return Validation.Failed("Restaurant phone number can't be empty")

    if (phone.length < 8 || phone.length > 8)
        return Validation.Failed("Restaurant phone number must contain exactly 8 digits")

    return Validation.Success
}

fun validateRestaurantDescription(name: String): Validation {
    if (name.isEmpty())
        return Validation.Failed("Restaurant description can't be empty.")

    if (name.length < 4)
        return Validation.Failed("Use 4 characters or more for restaurant description.")

    return Validation.Success
}

fun validateLocalisation(lat: String, long: String): Validation {
    if (lat == "-1" || long == "-1")
        return Validation.Failed("You have to enter the location of your restaurant.")

    return Validation.Success
}

//PLATE
fun validatePlateName(name: String): Validation {
    if (name.isEmpty())
        return Validation.Failed("Plate name can't be empty.")

    if (name.length < 4)
        return Validation.Failed("Use 4 characters or more for plate name.")

    return Validation.Success
}

fun validatePlatePrice(price: String): Validation {
    if (price == "-1")
        return Validation.Failed("Plate price can't be empty.")

    return Validation.Success
}

//ORDER
fun validateOrderAddress(address: String): Validation {
    if (address.isEmpty())
        return Validation.Failed("Address can't be empty.")

    if (address.length < 4)
        return Validation.Failed("Use 4 characters or more for the address.")

    return Validation.Success
}

fun validateOrderPhoneNumber(phone: String): Validation {
    if (phone == "-1")
        return Validation.Failed("The phone number can't be empty")

    if (phone.length < 8 || phone.length > 8)
        return Validation.Failed("The phone number must contain exactly 8 digits")

    return Validation.Success
}

