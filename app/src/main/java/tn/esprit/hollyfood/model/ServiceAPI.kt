package tn.esprit.hollyfood.model

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import tn.esprit.hollyfood.model.entities.User

interface ServiceAPI {

    //USER API
    @POST("/users")
    suspend fun register(@Body user: User): Response<User>

    @POST("/users/login")
    suspend fun login(@Body user: User): Response<User>

    @POST("/users/forgotPassword")
    suspend fun forgotPassword(@Body user: User): Response<ResponseBody>

}