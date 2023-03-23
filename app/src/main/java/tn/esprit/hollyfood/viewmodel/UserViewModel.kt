package tn.esprit.hollyfood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tn.esprit.hollyfood.model.RepositoryImp
import tn.esprit.hollyfood.model.RetroBuilder
import tn.esprit.hollyfood.model.ServiceAPI
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.*

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: RepositoryImp

    private var usersMutableLiveData = MutableLiveData<List<User>>()
    val usersLiveData: LiveData<List<User>> get() = usersMutableLiveData

    private var userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = userMutableLiveData

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()


    init {
        var serviceInstance = RetroBuilder.getRetroBuilder().create(ServiceAPI::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun getAllUsers() = viewModelScope.launch {
        var result = repository.getAllUsers()

        if (result.isSuccessful) {
            if (result.body() != null) {
                usersMutableLiveData.postValue(result.body())
            }
        } else {
            Log.i("error", result.message())
        }
    }

    fun getUser(id: String) = viewModelScope.launch {
        var result = repository.getUser(id)

        if (result.isSuccessful) {
            if (result.body() != null) {
                userMutableLiveData.postValue(result.body())
            }
        } else {
            Log.i("error", result.message())
        }
    }

    fun register(user: User) = viewModelScope.launch {

        if (checkValidation(user)) {
            var result = repository.register(user)

            if (result.isSuccessful) {
                if (result.body() != null) {
                    userMutableLiveData.postValue(result.body())
                }
            } else {
                Log.i("error", result.message())
            }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateFullName(user.fullname),
                validateEmail(user.email),
                validatePassword(user.password),
                validatePhoneNumber(user.phone.toString())
            )
            _validation.send(registerFieldsState)
        }
    }

    private fun checkValidation(user: User): Boolean {
        val fullnameValidation = validateFullName(user.fullname)
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        val phoneValidation = validatePhoneNumber(user.phone.toString())

        val check = fullnameValidation is RegisterValidation.Success &&
                emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success &&
                phoneValidation is RegisterValidation.Success

        return check
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        var result = repository.login(email, password)

        if (result.isSuccessful) {
            if (result.body() != null) {
                userMutableLiveData.postValue(result.body())
            }
        } else {
            Log.i("error", result.message())
        }
    }

    fun deleteUserAPI(id: String) {
        viewModelScope.launch {
            repository.deleteUser(id)
        }
    }

}