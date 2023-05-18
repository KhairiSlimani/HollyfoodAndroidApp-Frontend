package tn.esprit.hollyfood.model

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import tn.esprit.hollyfood.model.entities.*

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

    //Restaurant
    @Multipart
    @POST("/restaurants")
    suspend fun addRestaurant(@Part name: MultipartBody.Part,
                              @Part address: MultipartBody.Part,
                              @Part phoneNumber: MultipartBody.Part,
                              @Part description: MultipartBody.Part,
                              @Part image: MultipartBody.Part,
                              @Part rating: MultipartBody.Part,
                              @Part lat: MultipartBody.Part,
                              @Part long: MultipartBody.Part,
                              @Part userId: MultipartBody.Part): Response<Restaurant>
    @Multipart
    @PUT("/restaurants/{id}")
    suspend fun editRestaurant(@Path("id") id: String,
                               @Part name: MultipartBody.Part,
                               @Part address: MultipartBody.Part,
                               @Part phoneNumber: MultipartBody.Part,
                               @Part description: MultipartBody.Part,
                               @Part rating: MultipartBody.Part,
                               @Part lat: MultipartBody.Part,
                               @Part long: MultipartBody.Part,
                               @Part userId: MultipartBody.Part): Response<Restaurant>

    @GET("/restaurants/getByUser/{userId}")
    suspend fun getRestaurantsByUser(@Path("userId") userId: String): Response<List<Restaurant>>
    @GET("/restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurant>>
    @GET("/restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: String): Response<Restaurant>
    @DELETE("/restaurants/{id}")
    suspend fun deleteRestaurant(@Path("id") id: String): Response<Restaurant>

    //Plate
    @Multipart
    @POST("/plates")
    suspend fun addPlate(@Part name: MultipartBody.Part,
                              @Part category: MultipartBody.Part,
                              @Part price: MultipartBody.Part,
                              @Part image: MultipartBody.Part,
                              @Part restaurantId: MultipartBody.Part,
                              @Part userId: MultipartBody.Part): Response<Plate>
    @Multipart
    @PUT("/plates/{id}")
    suspend fun editPlate(@Path("id") id: String,
                               @Part name: MultipartBody.Part,
                               @Part category: MultipartBody.Part,
                               @Part price: MultipartBody.Part,
                               @Part restaurantId: MultipartBody.Part,
                               @Part userId: MultipartBody.Part): Response<Plate>

    @GET("/plates/getByRestaurant/{restaurantId}")
    suspend fun getPlatesByRestaurant(@Path("restaurantId") restaurantId: String): Response<List<Plate>>
    @GET("/plates/{id}")
    suspend fun getPlateById(@Path("id") id: String): Response<Plate>
    @DELETE("/plates/{id}")
    suspend fun deletePlate(@Path("id") id: String): Response<Plate>

    //Order
    @POST("/orders")
    suspend fun addOrder(@Body order: Order): Response<Order>
    @GET("/orders/getByRestaurant/{restaurantId}")
    suspend fun getOrdersByRestaurant(@Path("restaurantId") restaurantId: String): Response<List<Order>>
    @GET("/orders/getByUser/{userId}")
    suspend fun getOrdersByUser(@Path("userId") userId: String): Response<List<Order>>
    @GET("/orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<Order>
    @DELETE("/orders/{id}")
    suspend fun deleteOrder(@Path("id") id: String): Response<Order>

    //Orderline
    @POST("/orderlines")
    suspend fun addOrderline(@Body orderline: Orderline): Response<Orderline>
    @GET("/orderlines/getByOrder/{orderId}")
    suspend fun getOrderlinesByOrder(@Path("orderId") orderId: String): Response<List<Orderline>>

    //Rating
    @POST("/rating")
    suspend fun addOrUpdateRating(@Body rating: Rating): Response<Rating>

}