package pe.edu.upc.fintrack_frontend_application.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // New URL based on image_8.png, but set as Base URL
    private const val BASE_URL = "https://backend-kapakid-7bu6.onrender.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Expose a public createService function to be called like RetrofitClient.createService()
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}