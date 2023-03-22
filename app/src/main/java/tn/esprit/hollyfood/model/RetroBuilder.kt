package tn.esprit.hollyfood.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroBuilder {
    companion object {

        private const val BaseURL:String = "http://localhost:9090"

        fun getRetroBuilder() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}