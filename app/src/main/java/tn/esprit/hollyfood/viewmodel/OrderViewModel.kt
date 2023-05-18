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
import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.*
import java.io.IOException

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    //Repository
    private var repository: RepositoryImp

    //LiveData
    private var ordersMutableLiveData = MutableLiveData<List<Order>>()
    val ordersLiveData: LiveData<List<Order>> get() = ordersMutableLiveData

    private var orderMutableLiveData = MutableLiveData<Order>()
    val orderLiveData: LiveData<Order> get() = orderMutableLiveData

    private var messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = messageMutableLiveData

    //StateFlow
    private val _orderValidation = Channel<OrderFieldsState>()
    val orderValidation = _orderValidation.receiveAsFlow()

    init {
        var serviceInstance = Database.getRetroBuilder().create(APIServices::class.java)
        repository = RepositoryImp(serviceInstance)
    }

    fun addOrder(order: Order) = viewModelScope.launch {
        if (checkOrderValidation(order)) {
            try {
                var response = repository.addOrder(order)

                if (response.isSuccessful) {
                    orderMutableLiveData.postValue(response.body())
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
            val fieldsState = OrderFieldsState(
                validateOrderAddress(order.address),
                validateOrderPhoneNumber(order.phoneNumber.toString())
            )
            _orderValidation.send(fieldsState)
        }
    }

    private fun checkOrderValidation(order: Order): Boolean {
        val addressValidation = validateOrderAddress(order.address)
        val phoneNumberValidation = validateOrderPhoneNumber(order.phoneNumber.toString())

        val check = addressValidation is Validation.Success && phoneNumberValidation is Validation.Success

        return check
    }

    fun getOrdersByRestaurant(restaurantId: String) = viewModelScope.launch {
        try {
            val response = repository.getOrdersByRestaurant(restaurantId)

            if (response.isSuccessful) {
                ordersMutableLiveData.postValue(response.body())
                Log.d("Orders", "DONE")

            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun getOrdersByUser(userId: String) = viewModelScope.launch {
        try {
            val response = repository.getOrdersByUser(userId)

            if (response.isSuccessful) {
                ordersMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun getOrderById(id: String) = viewModelScope.launch {
        try {
            val response = repository.getOrderById(id)

            if (response.isSuccessful) {
                orderMutableLiveData.postValue(response.body())
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }

    fun deleteOrder(id: String) = viewModelScope.launch {
        try {
            val response = repository.deleteOrder(id)

            if (response.isSuccessful) {
                messageMutableLiveData.postValue("Order deleted successfully.")
            } else {
                messageMutableLiveData.postValue("Server error, please try again later.")
            }
        } catch (e: IOException) {
            messageMutableLiveData.postValue("Network error, please try again later.")
            Log.e("error", "IOException: ${e.message}")
        }
    }


}