package tn.esprit.hollyfood.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import tn.esprit.hollyfood.model.entities.User

class RepositoryImp(private val api: ServiceAPI) : Repository {

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


}