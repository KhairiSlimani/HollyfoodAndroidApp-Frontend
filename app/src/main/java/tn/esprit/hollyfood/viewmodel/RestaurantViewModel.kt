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
import okhttp3.MultipartBody
import tn.esprit.hollyfood.model.APIServices
import tn.esprit.hollyfood.model.Database
import tn.esprit.hollyfood.model.RepositoryImp
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.*
import java.io.IOException

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var restaurantsMutableLiveData = MutableLiveData<List<Restaurant>>()
    val restaurantsLiveData: LiveData<List<Restaurant>> get() = restaurantsMutableLiveData

    private var restaurantMutableLiveData = MutableLiveData<Restaurant>()
    val restaurantLiveData: LiveData<Restaurant> get() = restaurantMutableLiveData

    private var newRestaurantMutableLiveData = MutableLiveData<Restaurant>()
    val newRestaurantLiveData: LiveData<Restaurant> get() = newRestaurantMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData

    //StateFlow
    private val _restaurantValidation = Channel<RestaurantFieldsState>()
    val restaurantValidation = _restaurantValidation.receiveAsFlow()

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun addRestaurant(restaurant: Restaurant, image: MultipartBody.Part) = viewModelScope.launch {
        if (checkRestaurantValidation(restaurant)) {
            try {
                val name = MultipartBody.Part.createFormData("name", restaurant.name)
                val address = MultipartBody.Part.createFormData("address",restaurant.address)
                val phoneNumber = MultipartBody.Part.createFormData("phoneNumber", restaurant.phoneNumber.toString())
                val description = MultipartBody.Part.createFormData("description", restaurant.description)
                val userId = MultipartBody.Part.createFormData("userId", restaurant.userId)

                var response = repository.addRestaurant(name, address, phoneNumber, description, image, userId)

                if (response.isSuccessful) {
                    restaurantMutableLiveData.postValue(response.body())
                } else {
                    if (response.code() == 400) {
                        messageMutableLiveData.postValue("Invalid information.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = RestaurantFieldsState(
                validateRestaurantName(restaurant.name),
                validateRestaurantAddress(restaurant.address),
                validateRestaurantPhoneNumber(restaurant.phoneNumber.toString()),
                validateRestaurantDescription(restaurant.description)
            )
            _restaurantValidation.send(fieldsState)
        }
    }

    private fun checkRestaurantValidation(restaurant: Restaurant): Boolean {
        val nameValidation = validateRestaurantName(restaurant.name)
        val addressValidation = validateRestaurantAddress(restaurant.address)
        val phoneNumberValidation = validateRestaurantPhoneNumber(restaurant.phoneNumber.toString())
        val descriptionValidation = validateRestaurantDescription(restaurant.description)

        val check = nameValidation is Validation.Success &&
                addressValidation is Validation.Success &&
                phoneNumberValidation is Validation.Success &&
                descriptionValidation is Validation.Success

        return check
    }

    fun getRestaurantsByUser(userId: String) = viewModelScope.launch {
        try {
            val response = repository.getRestaurantsByUser(userId)

            if (response.isSuccessful) {
                restaurantsMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun getRestaurantById(id: String) = viewModelScope.launch {
        try {
            val response = repository.getRestaurantById(id)

            if (response.isSuccessful) {
                restaurantMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun getAllRestaurants() = viewModelScope.launch {
        try {
            val response = repository.getAllRestaurants()

            if (response.isSuccessful) {
                restaurantsMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun editRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        if (checkRestaurantValidation(restaurant)) {
            try {

                val name = MultipartBody.Part.createFormData("name", restaurant.name)
                val address = MultipartBody.Part.createFormData("address",restaurant.address)
                val phoneNumber = MultipartBody.Part.createFormData("phoneNumber", restaurant.phoneNumber.toString())
                val description = MultipartBody.Part.createFormData("description", restaurant.description)
                val userId = MultipartBody.Part.createFormData("userId", restaurant.userId)

                var response = repository.editRestaurant(restaurant.id, name, address, phoneNumber, description, userId)

                if (response.isSuccessful) {
                    messageMutableLiveData.postValue("Restaurant Edited Successfully.")
                } else {
                    if (response.code() == 400) {
                        messageMutableLiveData.postValue("Invalid information.")
                    } else {
                        messageMutableLiveData.postValue("Server error, please try again later.")
                    }
                }
            } catch (e: IOException) {
                messageMutableLiveData.postValue("Network error, please try again later.")
                Log.e("error", "IOException: ${e.message}")
            }
        } else {
            val fieldsState = RestaurantFieldsState(
                validateRestaurantName(restaurant.name),
                validateRestaurantAddress(restaurant.address),
                validateRestaurantPhoneNumber(restaurant.phoneNumber.toString()),
                validateRestaurantDescription(restaurant.description)
            )
            _restaurantValidation.send(fieldsState)
        }
    }

    fun deleteRestaurant(id: String) = viewModelScope.launch {
        try {
            val response = repository.deleteRestaurant(id)

            if (response.isSuccessful) {
                messageMutableLiveData.postValue("Restaurant deleted successfully.")
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

}