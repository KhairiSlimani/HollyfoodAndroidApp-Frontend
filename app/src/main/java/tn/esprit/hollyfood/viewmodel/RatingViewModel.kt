package tn.esprit.hollyfood.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.hollyfood.model.APIServices
import tn.esprit.hollyfood.model.Database
import tn.esprit.hollyfood.model.RepositoryImp
import tn.esprit.hollyfood.model.entities.Rating
import java.io.IOException

class RatingViewModel(application: Application) : AndroidViewModel(application) {
    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var ratingMutableLiveData = MutableLiveData<Rating>()
    val ratingLiveData: LiveData<Rating> get() = ratingMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun addOrUpdateRating(restaurantId: String) = viewModelScope.launch {
        try {
            val response = repository.addOrUpdateRating(restaurantId)

            if (response.isSuccessful) {
                ratingMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

}