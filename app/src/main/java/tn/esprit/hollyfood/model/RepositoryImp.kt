package tn.esprit.hollyfood.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import tn.esprit.hollyfood.model.entities.User

class RepositoryImp(private val api: ServiceAPI) : Repository {

    //USER
    override suspend fun getAllUsers() = withContext(Dispatchers.IO) {
        api.getAllUsers()
    }

    override suspend fun getUser(id: String): Response<User> = withContext(Dispatchers.IO) {
        api.getUser(id)
    }

    override suspend fun register(user: User): Response<User> = withContext(Dispatchers.IO) {
        api.register(user)
    }

    override suspend fun login(user: User): Response<User> = withContext(Dispatchers.IO) {
        api.login(user)
    }

    override suspend fun updateUser(user: User, id: String): Response<User> = withContext(Dispatchers.IO) {
        api.updateUser(user, id)
    }

    override suspend fun deleteUser(id: String) = withContext(Dispatchers.IO) {
        api.deleteUser(id)
    }


}