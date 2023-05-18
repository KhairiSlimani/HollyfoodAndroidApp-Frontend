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
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.*
import java.io.IOException

class PlateViewModel(application: Application) : AndroidViewModel(application) {
    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var platesMutableLiveData = MutableLiveData<List<Plate>>()
    val platesLiveData: LiveData<List<Plate>> get() = platesMutableLiveData

    private var plateMutableLiveData = MutableLiveData<Plate>()
    val plateLiveData: LiveData<Plate> get() = plateMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData

    //StateFlow
    private val _plateValidation = Channel<PlateFieldsState>()
    val plateValidation = _plateValidation.receiveAsFlow()

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun addPlate(plate: Plate, image: MultipartBody.Part) = viewModelScope.launch {
        if (checkPlateValidation(plate)) {
            try {
                val name = MultipartBody.Part.createFormData("name", plate.name)
                val category = MultipartBody.Part.createFormData("category",plate.category)
                val price = MultipartBody.Part.createFormData("price", plate.price.toString())
                val restaurantId = MultipartBody.Part.createFormData("restaurantId", plate.restaurantId)
                val userId = MultipartBody.Part.createFormData("userId", plate.userId)

                var response = repository.addPlate(name, category, price, image, restaurantId, userId)

                if (response.isSuccessful) {
                    plateMutableLiveData.postValue(response.body())
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
            val fieldsState = PlateFieldsState(
                validatePlateName(plate.name),
                validatePlatePrice(plate.price.toString()),
            )
            _plateValidation.send(fieldsState)
        }
    }

    private fun checkPlateValidation(plate: Plate): Boolean {
        val nameValidation = validatePlateName(plate.name)
        val priceValidation = validatePlatePrice(plate.price.toString())

        val check = nameValidation is Validation.Success &&
                priceValidation is Validation.Success

        return check
    }

    fun editPlate(plate: Plate) = viewModelScope.launch {
        if (checkPlateValidation(plate)) {
            try {
                val name = MultipartBody.Part.createFormData("name", plate.name)
                val category = MultipartBody.Part.createFormData("category",plate.category)
                val price = MultipartBody.Part.createFormData("price", plate.price.toString())
                val restaurantId = MultipartBody.Part.createFormData("restaurantId", plate.restaurantId)
                val userId = MultipartBody.Part.createFormData("userId", plate.userId)

                var response = repository.editPlate(plate.id, name, category, price, restaurantId, userId)

                if (response.isSuccessful) {
                    messageMutableLiveData.postValue("Plate edited successfully.")
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
            val fieldsState = PlateFieldsState(
                validatePlateName(plate.name),
                validatePlatePrice(plate.price.toString()),
            )
            _plateValidation.send(fieldsState)
        }
    }

    fun getPlatesByRestaurant(restaurantId: String) = viewModelScope.launch {
        try {
            val response = repository.getPlatesByRestaurant(restaurantId)

            if (response.isSuccessful) {
                platesMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun getPlateById(plateId: String) = viewModelScope.launch {
        try {
            val response = repository.getPlateById(plateId)

            if (response.isSuccessful) {
                plateMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun deletePlate(id: String) = viewModelScope.launch {
        try {
            val response = repository.deletePlate(id)

            if (response.isSuccessful) {
                messageMutableLiveData.postValue("Plate deleted successfully.")
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }


}