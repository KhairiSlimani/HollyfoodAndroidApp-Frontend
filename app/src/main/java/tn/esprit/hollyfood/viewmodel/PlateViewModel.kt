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
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.PlateFieldsState
import tn.esprit.hollyfood.util.RestaurantFieldsState
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


}