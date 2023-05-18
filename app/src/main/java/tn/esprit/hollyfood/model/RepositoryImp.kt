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
    override suspend fun addRestaurant(
        name: MultipartBody.Part,
        address: MultipartBody.Part,
        phoneNumber: MultipartBody.Part,
        description: MultipartBody.Part,
        image: MultipartBody.Part,
        rating: MultipartBody.Part,
        lat: MultipartBody.Part,
        long: MultipartBody.Part,
        userId: MultipartBody.Part
    ): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.addRestaurant(name, address, phoneNumber, description, image, rating, lat, long, userId)
    }

    override suspend fun getRestaurantsByUser(userId: String): Response<List<Restaurant>> = withContext(Dispatchers.IO) {
        api.getRestaurantsByUser(userId)
    }

    override suspend fun getRestaurantById(id: String): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.getRestaurantById(id)
    }

    override suspend fun getAllRestaurants(): Response<List<Restaurant>> = withContext(Dispatchers.IO) {
        api.getAllRestaurants()
    }

    override suspend fun editRestaurant(
        id: String,
        name: MultipartBody.Part,
        address: MultipartBody.Part,
        phoneNumber: MultipartBody.Part,
        description: MultipartBody.Part,
        rating: MultipartBody.Part,
        lat: MultipartBody.Part,
        long: MultipartBody.Part,
        userId: MultipartBody.Part
    ): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.editRestaurant(id, name, address, phoneNumber, description, rating, lat, long, userId)
    }

    override suspend fun deleteRestaurant(id: String): Response<Restaurant> = withContext(Dispatchers.IO) {
        api.deleteRestaurant(id)
    }

    //PLATE
    override suspend fun addPlate(
        name: MultipartBody.Part,
        category: MultipartBody.Part,
        price: MultipartBody.Part,
        image: MultipartBody.Part,
        restaurantId: MultipartBody.Part,
        userId: MultipartBody.Part
    ): Response<Plate> = withContext(Dispatchers.IO) {
        api.addPlate(name, category, price, image, restaurantId, userId)
    }

    override suspend fun editPlate(
        id: String,
        name: MultipartBody.Part,
        category: MultipartBody.Part,
        price: MultipartBody.Part,
        restaurantId: MultipartBody.Part,
        userId: MultipartBody.Part
    ): Response<Plate> = withContext(Dispatchers.IO) {
        api.editPlate(id, name, category, price, restaurantId, userId)
    }

    override suspend fun getPlatesByRestaurant(restaurantId: String): Response<List<Plate>> = withContext(Dispatchers.IO) {
        api.getPlatesByRestaurant(restaurantId)
    }

    override suspend fun getPlateById(id: String): Response<Plate> = withContext(Dispatchers.IO) {
        api.getPlateById(id)
    }

    override suspend fun deletePlate(id: String): Response<Plate> = withContext(Dispatchers.IO) {
        api.deletePlate(id)
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

    override suspend fun getOrderById(id: String): Response<Order> = withContext(Dispatchers.IO) {
        api.getOrderById(id)
    }

    //ORDERLINE
    override suspend fun addOrderline(orderline: Orderline): Response<Orderline> = withContext(Dispatchers.IO) {
        api.addOrderline(orderline)
    }

    override suspend fun getOrderlinesByOrder(orderId: String): Response<List<Orderline>> = withContext(Dispatchers.IO) {
        api.getOrderlinesByOrder(orderId)
    }

    //RATING
    override suspend fun addOrUpdateRating(rating: Rating): Response<Rating> = withContext(Dispatchers.IO) {
        api.addOrUpdateRating(rating)
    }


}