package pe.edu.upc.fintrack_frontend_application.iam.data.network

import pe.edu.upc.fintrack_frontend_application.iam.domain.model.LoginRequest
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.LoginResponse
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.RegisterRequest
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface IamApiService {
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}