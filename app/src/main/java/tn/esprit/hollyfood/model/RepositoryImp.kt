package tn.esprit.hollyfood.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import tn.esprit.hollyfood.model.entities.User

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

}