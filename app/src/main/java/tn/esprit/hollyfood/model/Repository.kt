package tn.esprit.hollyfood.model

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import tn.esprit.hollyfood.model.entities.EditProfileRequest
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.model.entities.User

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
    suspend fun addRestaurant(restaurant: Restaurant): Response<Restaurant>
    suspend fun getRestaurantsByUser(userId: String): Response<List<Restaurant>>


}