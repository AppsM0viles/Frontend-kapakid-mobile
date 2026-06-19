package pe.edu.upc.fintrack_frontend_application.iam.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.fintrack_frontend_application.iam.data.repository.IamRepository
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import java.util.Locale

// ¡Esta es la clase que faltaba y que soluciona todos los errores rojos!
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class LoginViewModel : ViewModel() {
    private val repository = IamRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun attemptLogin(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Completa todos los campos")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val response = repository.login(email.trim(), pass)
            if (response != null) {
                SessionManager.token = response.token
                SessionManager.userEmail = email.trim()

                // Estrategia temporal: Extrae el identificador del correo como nombre dinámico
                val parsedName = email.substringBefore("@")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                SessionManager.userName = parsedName

                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Correo o contraseña incorrectos")
            }
        }
    }

    fun resetState() { _authState.value = AuthState.Idle }
}