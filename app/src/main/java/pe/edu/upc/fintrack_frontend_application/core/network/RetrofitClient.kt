package pe.edu.upc.fintrack_frontend_application.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://backend-kapakid.onrender.com/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Asegúrate de que esta función esté aquí adentro
    fun <T> createService(serviceClass: Class<T>): T {
        return instance.create(serviceClass)
    }
}