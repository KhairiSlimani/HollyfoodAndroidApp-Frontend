package tn.esprit.hollyfood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.hollyfood.model.RepositoryImp
import tn.esprit.hollyfood.model.RetroBuilder
import tn.esprit.hollyfood.model.ServiceAPI
import tn.esprit.hollyfood.model.entities.User

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: RepositoryImp

    private var usersMutableLiveData = MutableLiveData<List<User>>()
    val usersLiveData: LiveData<List<User>> get() = usersMutableLiveData

    private var userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = userMutableLiveData

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
        var result = repository.register(user)

        if (result.isSuccessful) {
            if (result.body() != null) {
                userMutableLiveData.postValue(result.body())
            }
        } else {
            Log.i("error", result.message())
        }
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