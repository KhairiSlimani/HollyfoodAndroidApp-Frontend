package tn.esprit.hollyfood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
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

    private val _validation = Channel<FieldsState>()
    val validation = _validation.receiveAsFlow()

    private val messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData


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

        if (checkRegisterValidation(user)) {
            var result = repository.register(user)

            if (result.isSuccessful) {
                if (result.body() != null) {
                    userMutableLiveData.postValue(result.body())
                }
            } else {
                Log.i("error", result.message())
            }
        } else {
            val fieldsState = FieldsState(
                validateFullName(user.fullname),
                validateEmail(user.email),
                validatePassword(user.password),
                validatePhoneNumber(user.phone.toString())
            )
            _validation.send(fieldsState)
        }
    }

    private fun checkRegisterValidation(user: User): Boolean {
        val fullnameValidation = validateFullName(user.fullname)
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        val phoneValidation = validatePhoneNumber(user.phone.toString())

        val check = fullnameValidation is Validation.Success &&
                emailValidation is Validation.Success &&
                passwordValidation is Validation.Success &&
                phoneValidation is Validation.Success

        return check
    }

    fun login(email: String, password: String) = viewModelScope.launch {

        if (checkLoginValidation(email, password)) {
            var response = repository.login(User("","",email,password,0,"",""))

            if (response.isSuccessful) {
                userMutableLiveData.postValue(response.body())
            }
            else if (response.code() ==  404) {
                messageMutableLiveData.postValue("The email you entered isn’t connected to an account.")
            }
            else if (response.code() ==  434) {
                messageMutableLiveData.postValue("Your account has not yet been verified.")
            }
            else if (response.code() ==  401) {
                messageMutableLiveData.postValue("The password you’ve entered is incorrect.")
            }
            else {
                Log.i("error", response.message())
            }
        } else{
            val fieldsState = FieldsState(
                Validation.Success,
                validateEmail(email),
                validatePassword(password),
                Validation.Success
            )
            _validation.send(fieldsState)

        }
    }

    private fun checkLoginValidation(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        val check = emailValidation is Validation.Success &&
                passwordValidation is Validation.Success

        return check
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            repository.deleteUser(id)
        }
    }

}