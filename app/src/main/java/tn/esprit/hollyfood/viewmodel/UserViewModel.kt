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
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.*
import java.io.IOException

class UserViewModel(application: Application) : AndroidViewModel(application) {

    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = userMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData

    //StateFlow
    private val _validation = Channel<FieldsState>()
    val validation = _validation.receiveAsFlow()

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
            val fieldsState = FieldsState(
                validateFullName(user.fullname),
                validateEmail(user.email),
                validatePassword(user.password, confirmPassword),
                validatePhoneNumber(user.phone.toString())
            )
            _validation.send(fieldsState)
        }
    }

    private fun checkRegisterValidation(user: User, confirmPassword: String): Boolean {
        val fullnameValidation = validateFullName(user.fullname)
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password, confirmPassword)
        val phoneValidation = validatePhoneNumber(user.phone.toString())

        val check = fullnameValidation is Validation.Success &&
                emailValidation is Validation.Success &&
                passwordValidation is Validation.Success &&
                phoneValidation is Validation.Success

        return check
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        if (validateEmail(email) is Validation.Success && validatePassword(password, password) is Validation.Success) {
            try {
                var response = repository.login(User("", "", email, password, 0, "", ""))

                if (response.isSuccessful) {
                    userMutableLiveData.postValue(response.body())
                } else {
                    if (response.code() == 404) {
                        messageMutableLiveData.postValue("The email you entered isn’t connected to an account.")
                    } else if (response.code() == 434) {
                        messageMutableLiveData.postValue("Your account has not yet been verified.")
                    } else if (response.code() == 401) {
                        messageMutableLiveData.postValue("The password you’ve entered is incorrect.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = FieldsState(
                Validation.Success,
                validateEmail(email),
                validatePassword(password, password),
                Validation.Success
            )
            _validation.send(fieldsState)
        }
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        if (validateEmail(email) is Validation.Success) {
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
            val fieldsState = FieldsState(
                Validation.Success,
                validateEmail(email),
                Validation.Success,
                Validation.Success
            )
            _validation.send(fieldsState)
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

        if (validateEmail(email) is Validation.Success && validatePassword(password, confirmPassword) is Validation.Success) {
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
            val fieldsState = FieldsState(
                Validation.Success,
                validateEmail(email),
                validatePassword(password, confirmPassword),
                Validation.Success
            )
            _validation.send(fieldsState)
        }
    }

    fun clearMessage() {
        messageMutableLiveData.postValue("")
    }

}