package tn.esprit.hollyfood.model

import retrofit2.Response
import tn.esprit.hollyfood.model.entities.User

interface Repository {

    //USER
    suspend fun getAllUsers(): Response<List<User>>
    suspend fun getUser(id: String): Response<User>
    suspend fun register(user: User): Response<User>
    suspend fun login(email: String, password: String): Response<User>
    suspend fun updateUser(user: User, id: String): Response<User>
    suspend fun deleteUser(id: String)

}