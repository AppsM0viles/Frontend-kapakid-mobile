package pe.edu.upc.fintrack_frontend_application.iam.data.network

import pe.edu.upc.fintrack_frontend_application.iam.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IamApiService {
    @GET("users")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): List<User>

    @POST("users")
    suspend fun registerUser(@Body user: User): User
}