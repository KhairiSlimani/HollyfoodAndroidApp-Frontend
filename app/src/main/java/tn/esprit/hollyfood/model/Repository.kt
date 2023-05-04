package tn.esprit.hollyfood.model

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import tn.esprit.hollyfood.model.entities.*

interface Repository {

    //USER
    suspend fun register(user: User): Response<User>
    suspend fun login(user: User): Response<User>
    suspend fun forgotPassword(user: User): Response<ResponseBody>
    suspend fun codeVerification(request: Map<String, String>): Response<ResponseBody>
    suspend fun resetPassword(request: Map<String, String>): Response<ResponseBody>
    suspend fun verifyAccount(request: Map<String, String>): Response<ResponseBody>
    suspend fun getByEmail(request: Map<String, String>): Response<User>
    suspend fun editProfile(id:String, request: EditProfileRequest): Response<User>
    suspend fun changePassword(request: Map<String, String>): Response<ResponseBody>

    //Restaurant
    suspend fun addRestaurant(name: MultipartBody.Part, address: MultipartBody.Part, phoneNumber: MultipartBody.Part, description: MultipartBody.Part, image: MultipartBody.Part, userId: MultipartBody.Part): Response<Restaurant>
    suspend fun getRestaurantsByUser(userId: String): Response<List<Restaurant>>
    suspend fun getRestaurantById(id: String): Response<Restaurant>
    suspend fun editRestaurant(id:String, name: MultipartBody.Part, address: MultipartBody.Part, phoneNumber: MultipartBody.Part, description: MultipartBody.Part, userId: MultipartBody.Part): Response<Restaurant>
    suspend fun deleteRestaurant(id: String): Response<Restaurant>

    //Plate
    suspend fun getPlatesByRestaurant(restaurantId: String): Response<List<Plate>>

    //Order
    suspend fun addOrder(order: Order): Response<Order>
    suspend fun getOrdersByRestaurant(restaurantId: String): Response<List<Order>>
    suspend fun getOrdersByUser(userId: String): Response<List<Order>>
    suspend fun deleteOrder(id: String): Response<Order>

}