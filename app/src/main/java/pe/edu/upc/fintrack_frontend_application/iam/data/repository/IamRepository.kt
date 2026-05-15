package pe.edu.upc.fintrack_frontend_application.iam.data.repository

import android.util.Log
import pe.edu.upc.fintrack_frontend_application.core.network.RetrofitClient
import pe.edu.upc.fintrack_frontend_application.iam.data.network.IamApiService
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.User

class IamRepository {
    private val api = RetrofitClient.createService(IamApiService::class.java)

    suspend fun login(email: String, password: String): User? {
        return try {
            val response = api.login(email, password)
            response.firstOrNull()
        } catch (e: Exception) {
            Log.e("IamRepository", "Error de red en Login: ${e.message}")
            null
        }
    }

    suspend fun register(user: User): User? {
        return try {
            // Json-server devolverá el usuario creado con status 201
            api.registerUser(user)
        } catch (e: Exception) {
            Log.e("IamRepository", "Error de red en Registro: ${e.message}")
            null
        }
    }
}