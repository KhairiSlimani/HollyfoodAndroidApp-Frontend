package tn.esprit.hollyfood.model

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
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


}