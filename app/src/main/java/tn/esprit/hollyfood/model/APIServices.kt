package tn.esprit.hollyfood.model

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import tn.esprit.hollyfood.model.entities.EditProfileRequest
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.model.entities.User

interface APIServices {

    //USER API
    @POST("/users")
    suspend fun register(@Body user: User): Response<User>
    @POST("/users/login")
    suspend fun login(@Body user: User): Response<User>
    @POST("/users/forgotPassword")
    suspend fun forgotPassword(@Body user: User): Response<ResponseBody>
    @POST("/users/codeVerification")
    suspend fun codeVerification(@Body request: Map<String, String>): Response<ResponseBody>
    @POST("/users/resetPassword")
    suspend fun resetPassword(@Body request: Map<String, String>): Response<ResponseBody>
    @POST("/users/verifyAccount")
    suspend fun verifyAccount(@Body request: Map<String, String>): Response<ResponseBody>
    @POST("/users/getByEmail")
    suspend fun getByEmail(@Body request: Map<String, String>): Response<User>
    @PATCH("/users/{id}")
    suspend fun editProfile(@Path("id") id: String, @Body request: EditProfileRequest): Response<User>
    @POST("/users/changePassword")
    suspend fun changePassword(@Body request: Map<String, String>): Response<ResponseBody>

    @Multipart
    @POST("/restaurants")
    suspend fun addRestaurant(@Part name: MultipartBody.Part,
                              @Part address: MultipartBody.Part,
                              @Part phoneNumber: MultipartBody.Part,
                              @Part description: MultipartBody.Part,
                              @Part image: MultipartBody.Part,
                              @Part userId: MultipartBody.Part): Response<Restaurant>

    @GET("/restaurants/getByUser/{userId}")
    suspend fun getRestaurantsByUser(@Path("userId") userId: String): Response<List<Restaurant>>
    @GET("/restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: String): Response<Restaurant>
    @PUT("/restaurants/{id}")
    suspend fun editRestaurant(@Path("id") id: String, @Body restaurant: Restaurant): Response<Restaurant>
    @DELETE("/restaurants/{id}")
    suspend fun deleteRestaurant(@Path("id") id: String): Response<Restaurant>


}