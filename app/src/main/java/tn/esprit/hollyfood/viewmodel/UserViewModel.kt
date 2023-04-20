package tn.esprit.hollyfood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tn.esprit.hollyfood.model.RepositoryImp
import tn.esprit.hollyfood.model.Database
import tn.esprit.hollyfood.model.APIServices
import tn.esprit.hollyfood.model.entities.EditProfileRequest
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.*
import java.io.IOException
import java.util.Map

class UserViewModel(application: Application) : AndroidViewModel(application) {

    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = userMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData

    //StateFlow
    private val _userValidation = Channel<UserFieldsState>()
    val userValidation = _userValidation.receiveAsFlow()

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun register(user: User, confirmPassword: String) = viewModelScope.launch {
        if (checkRegisterValidation(user, confirmPassword)) {
            try {
                var response = repository.register(user)

                if (response.isSuccessful) {
                    userMutableLiveData.postValue(response.body())
                } else {
                    if (response.code() == 409) {
                        messageMutableLiveData.postValue("Email already exist.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = UserFieldsState(
                validateUserFullName(user.fullname),
                validateUserEmail(user.email),
                validateUserPassword(user.password, confirmPassword),
                validateUserPhoneNumber(user.phone.toString())
            )
            _userValidation.send(fieldsState)
        }
    }

    private fun checkRegisterValidation(user: User, confirmPassword: String): Boolean {
        val fullnameValidation = validateUserFullName(user.fullname)
        val emailValidation = validateUserEmail(user.email)
        val passwordValidation = validateUserPassword(user.password, confirmPassword)
        val phoneValidation = validateUserPhoneNumber(user.phone.toString())

        val check = fullnameValidation is Validation.Success &&
                emailValidation is Validation.Success &&
                passwordValidation is Validation.Success &&
                phoneValidation is Validation.Success

        return check
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        if (validateUserEmail(email) is Validation.Success && validateUserPassword(password, password) is Validation.Success) {
            try {
                var response = repository.login(User("", "", email, password, 0, "", ""))

                if (response.isSuccessful) {
                    userMutableLiveData.postValue(response.body())
                } else {
                    if (response.code() == 404) {
                        messageMutableLiveData.postValue("The email you entered isn’t connected to an account.")
                    } else if (response.code() == 401) {
                        messageMutableLiveData.postValue("The password you’ve entered is incorrect.")
                    } else if (response.code() == 434) {
                        messageMutableLiveData.postValue("Your account has not yet been verified.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = UserFieldsState(
                Validation.Success,
                validateUserEmail(email),
                validateUserPassword(password, password),
                Validation.Success
            )
            _userValidation.send(fieldsState)
        }
    }

    fun getByEmail(email: String) = viewModelScope.launch {
        try {
            val request = mapOf("email" to email)
            val response = repository.getByEmail(request)

            if (response.isSuccessful) {
                userMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        if (validateUserEmail(email) is Validation.Success) {
            try {
                val response = repository.forgotPassword(User("", "", email, "", 0, "", ""))

                if (response.isSuccessful) {
                    messageMutableLiveData.postValue("Reset password code sent successfully.")
                } else {
                    if (response.code() == 404) {
                        messageMutableLiveData.postValue("The email you entered isn’t connected to an account.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }

            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = UserFieldsState(
                Validation.Success,
                validateUserEmail(email),
                Validation.Success,
                Validation.Success
            )
            _userValidation.send(fieldsState)
        }
    }

    fun codeVerification(code: String) = viewModelScope.launch {
        try {
            val request = mapOf("code" to code)
            val response = repository.codeVerification(request)

            if (response.isSuccessful) {
                messageMutableLiveData.postValue("Valid code.")
            } else {
                if (response.code() == 404) {
                    messageMutableLiveData.postValue("The code you entered doesn't match your code. Please try again.")
                } else {
                    messageMutableLiveData.postValue("Server error, please try again later.")
                }
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun resetPassword(email: String, password: String, confirmPassword: String) = viewModelScope.launch {
        Log.e("==== email, password: ", "${email} ${password} ${confirmPassword}")

        if (validateUserEmail(email) is Validation.Success && validateUserPassword(password, confirmPassword) is Validation.Success) {
            try {
                val request = mapOf(
                    "email" to email,
                    "password" to password
                )

                val response = repository.resetPassword(request)

                if (response.isSuccessful) {
                    messageMutableLiveData.postValue("Password reset successfully.")
                } else {
                    messageMutableLiveData.postValue("Server error, please try again later.")
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = UserFieldsState(
                Validation.Success,
                validateUserEmail(email),
                validateUserPassword(password, confirmPassword),
                Validation.Success
            )
            _userValidation.send(fieldsState)
        }
    }

    fun clearMessage() {
        messageMutableLiveData.postValue("")
    }

    fun verifyAccount(email: String, code: String) = viewModelScope.launch {
        try {
            val request = mapOf(
                "email" to email,
                "code" to code
            )

            val response = repository.verifyAccount(request)

            if (response.isSuccessful) {
                messageMutableLiveData.postValue("User verified successfully.")
            } else {
                if (response.code() == 404) {
                    messageMutableLiveData.postValue("The code you entered doesn't match your code. Please try again.")
                } else {
                    messageMutableLiveData.postValue("Server error, please try again later.")
                }
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun editProfile(id: String, fullname: String, email: String, phoneNumber: Int) = viewModelScope.launch {
        if (checkEditProfileValidation(fullname, email, phoneNumber)) {
            try {
                val request = EditProfileRequest(fullname, email, phoneNumber)

                var response = repository.editProfile(id, request)

                if (response.isSuccessful) {
                    userMutableLiveData.postValue(response.body())
                } else {
                    if (response.code() == 409) {
                        messageMutableLiveData.postValue("Email already exist.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = UserFieldsState(
                validateUserFullName(fullname),
                validateUserEmail(email),
                Validation.Success,
                validateUserPhoneNumber(phoneNumber.toString())
            )
            _userValidation.send(fieldsState)
        }
    }

    private fun checkEditProfileValidation(fullname: String, email: String, phoneNumber: Int): Boolean {
        val fullnameValidation = validateUserFullName(fullname)
        val emailValidation = validateUserEmail(email)
        val phoneValidation = validateUserPhoneNumber(phoneNumber.toString())

        val check = fullnameValidation is Validation.Success &&
                emailValidation is Validation.Success &&
                phoneValidation is Validation.Success

        return check
    }

    fun changePassword(email: String,oldPassword: String, newPassword: String, confirmPassword: String) = viewModelScope.launch {
        if (validateUserPassword(newPassword, confirmPassword) is Validation.Success) {
            try {
                val request = mapOf(
                    "email" to email,
                    "oldPassword" to oldPassword,
                    "newPassword" to newPassword
                )

                val response = repository.changePassword(request)

                if (response.isSuccessful) {
                    messageMutableLiveData.postValue("Password changed successfully.")
                } else {
                    if (response.code() == 401) {
                        messageMutableLiveData.postValue("Wrong password.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = UserFieldsState(
                Validation.Success,
                validateUserEmail(email),
                validateUserPassword(newPassword, confirmPassword),
                Validation.Success
            )
            _userValidation.send(fieldsState)
        }
    }


}