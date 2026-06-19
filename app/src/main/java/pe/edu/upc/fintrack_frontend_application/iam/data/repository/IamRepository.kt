package pe.edu.upc.fintrack_frontend_application.iam.data.repository

import android.util.Log
import pe.edu.upc.fintrack_frontend_application.core.network.LoginResponse
import pe.edu.upc.fintrack_frontend_application.core.network.RegisterResponse
import pe.edu.upc.fintrack_frontend_application.core.network.RetrofitClient
import pe.edu.upc.fintrack_frontend_application.iam.data.network.IamApiService
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.LoginRequest
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.RegisterRequest

class IamRepository {
    private val api = RetrofitClient.createService(IamApiService::class.java)

    suspend fun login(email: String, password: String): LoginResponse? {
        return try {
            api.login(LoginRequest(email, password))
        } catch (e: Exception) {
            Log.e("IamRepository", "Error en Login: ${e.message}")
            null
        }
    }

    suspend fun register(
        nombre: String,
        email: String,
        password: String,
        dni: String,
        fechaNacimiento: String,
        esUniversitario: Boolean
    ): RegisterResponse? {
        return try {
            api.register(RegisterRequest(nombre, email, password, dni, fechaNacimiento, esUniversitario))
        } catch (e: Exception) {
            Log.e("IamRepository", "Error en Registro: ${e.message}")
            null
        }
    }
}