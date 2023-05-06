package tn.esprit.hollyfood.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import tn.esprit.hollyfood.model.entities.*

class RepositoryImp(private val api: APIServices) : Repository {

    //USER
    override suspend fun register(user: User): Response<User> = withContext(Dispatchers.IO) {
        api.register(user)
    }

    override suspend fun login(user: User): Response<User> = withContext(Dispatchers.IO) {
        api.login(user)
    }

    override suspend fun forgotPassword(user: User): Response<ResponseBody> = withContext(Dispatchers.IO) {
        api.forgotPassword(user)
    }

    override suspend fun codeVerification(request: Map<String, String>): Response<ResponseBody> = withContext(Dispatchers.IO) {
        api.codeVerification(request)
    }

    override suspend fun resetPassword(request: Map<String, String>): Response<ResponseBody> = withContext(Dispatchers.IO) {
        api.resetPassword(request)
    }

    override suspend fun verifyAccount(request: Map<String, String>): Response<ResponseBody> = withContext(Dispatchers.IO) {
        api.verifyAccount(request)
    }

    override suspend fun getByEmail(request: Map<String, String>): Response<User> = withContext(Dispatchers.IO) {
        api.getByEmail(request)
    }

    override suspend fun editProfile(id:String, request: EditProfileRequest): Response<User> = withContext(Dispatchers.IO) {
        api.editProfile(id, request)
    }

    override suspend fun changePassword(request: Map<String, String>): Response<ResponseBody> = withContext(Dispatchers.IO) {
        api.changePassword(request)
    }

    //RESTAURANT
    override suspend fun addRestaurant(name: MultipartBody.Part, address: MultipartBody.Part, phoneNumber: MultipartBody.Part, description: MultipartBody.Part, image: MultipartBody.Part, userId: MultipartBody.Part): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.addRestaurant(name, address, phoneNumber, description, image, userId)
    }

    override suspend fun getRestaurantsByUser(userId: String): Response<List<Restaurant>> = withContext(Dispatchers.IO) {
        api.getRestaurantsByUser(userId)
    }

    override suspend fun getRestaurantById(id: String): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.getRestaurantById(id)
    }

    override suspend fun editRestaurant(id: String, name: MultipartBody.Part, address: MultipartBody.Part, phoneNumber: MultipartBody.Part, description: MultipartBody.Part, userId: MultipartBody.Part): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.editRestaurant(id, name, address, phoneNumber, description, userId)
    }

    override suspend fun deleteRestaurant(id: String): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.deleteRestaurant(id)
    }

    override suspend fun getPlatesByRestaurant(restaurantId: String): Response<List<Plate>> = withContext(Dispatchers.IO) {
        api.getPlatesByRestaurant(restaurantId)
    }

    //ORDER
    override suspend fun addOrder(order: Order): Response<Order> = withContext(Dispatchers.IO) {
        api.addOrder(order)
    }

    override suspend fun getOrdersByRestaurant(restaurantId: String): Response<List<Order>> = withContext(Dispatchers.IO) {
        api.getOrdersByRestaurant(restaurantId)
    }

    override suspend fun getOrdersByUser(userId: String): Response<List<Order>> = withContext(Dispatchers.IO) {
        api.getOrdersByUser(userId)
    }

    override suspend fun deleteOrder(id: String): Response<Order> = withContext(Dispatchers.IO) {
        api.deleteOrder(id)
    }

    //ORDERLINE
    override suspend fun addOrderline(orderline: Orderline): Response<Orderline> = withContext(Dispatchers.IO) {
        api.addOrderline(orderline)
    }

    override suspend fun getOrderlinesByOrder(orderId: String): Response<List<Orderline>> = withContext(Dispatchers.IO) {
        api.getOrderlinesByOrder(orderId)
    }


}