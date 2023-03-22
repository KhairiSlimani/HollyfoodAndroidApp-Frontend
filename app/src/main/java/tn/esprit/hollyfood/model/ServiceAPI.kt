package tn.esprit.hollyfood.model

import retrofit2.Response
import retrofit2.http.*
import tn.esprit.hollyfood.model.entities.User

interface ServiceAPI {

    //USER API
    @GET("/users")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<User>

    @POST("/users")
    suspend fun register(@Body user: User): Response<User>

    @POST("/users/login")
    suspend fun login(@Body email: String, @Body password: String): Response<User>

    @PATCH("/users/{id}")
    suspend fun updateUser(@Body user: User, @Path("id") id: String): Response<User>

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") id: String)


}