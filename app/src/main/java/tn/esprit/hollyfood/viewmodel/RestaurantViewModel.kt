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

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData


    //StateFlow
    private val _restaurantValidation = Channel<RestaurantFieldsState>()
    val restaurantValidation = _restaurantValidation.receiveAsFlow()

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun addRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        if (checkRestaurantValidation(restaurant)) {
            try {
                var response = repository.addRestaurant(restaurant)

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

}