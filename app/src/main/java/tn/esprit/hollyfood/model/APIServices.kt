package tn.esprit.hollyfood.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
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

}