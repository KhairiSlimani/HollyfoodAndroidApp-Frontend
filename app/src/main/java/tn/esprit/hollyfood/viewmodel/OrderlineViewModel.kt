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
import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.Orderline
import java.io.IOException

class OrderlineViewModel(application: Application) : AndroidViewModel(application) {
    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var orderlinesMutableLiveData = MutableLiveData<List<Orderline>>()
    val orderlinesLiveData: LiveData<List<Orderline>> get() = orderlinesMutableLiveData

    private var orderlineMutableLiveData = MutableLiveData<Orderline>()
    val orderlineLiveData: LiveData<Orderline> get() = orderlineMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData


    companion object {
        var cart = mutableListOf<Orderline>()
        private var totalMutableLiveData = MutableLiveData<Float>()
        val totalLiveData: LiveData<Float> get() = totalMutableLiveData

        fun calculateTotal() {
            var totalPrice = 0f
            for (item in cart) {
                totalPrice += item.price
            }
            totalMutableLiveData.postValue(totalPrice)
        }

        fun clear() {
            cart.clear()
            totalMutableLiveData.postValue(0F)
        }
    }

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun addOrderlines(order: Order) = viewModelScope.launch {
        try {
            for (orderline in cart) {
                orderline.orderId = order.id

                var response = repository.addOrderline(orderline)

                if (response.code() == 400) {
                    messageMutableLiveData.postValue("Invalid information.")
                } else {
                    messageMutableLiveData.postValue("Server error, please try again later.")
                }
            }
            clear()
            messageMutableLiveData.postValue("Success.")
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun getOrderlinesByOrder(orderId: String) = viewModelScope.launch {
        try {
            val response = repository.getOrderlinesByOrder(orderId)

            if (response.isSuccessful) {
                orderlinesMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }


}