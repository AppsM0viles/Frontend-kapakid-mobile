package pe.edu.upc.fintrack_frontend_application.iam.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.iam.data.repository.IamRepository
import pe.edu.upc.fintrack_frontend_application.iam.presentation.login.AuthState

class RegisterViewModel : ViewModel() {
    private val repository = IamRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun register(name: String, lastName: String, dni: String, birthDate: String, email: String, pass: String, confirmPass: String, isStudent: Boolean, termsAccepted: Boolean) {
        if (!termsAccepted) {
            _authState.value = AuthState.Error("Debes aceptar los Términos y Condiciones")
            return
        }
        if (name.isBlank() || lastName.isBlank() || dni.isBlank() || birthDate.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Completa todos los campos")
            return
        }

        // Validación estricta de contraseña: Min 8 chars, 1 mayúscula, 1 carácter especial
        val passwordPattern = "^(?=.*[A-Z])(?=.*[@#\$%^&+=!_\\-]).{8,}$".toRegex()
        if (!passwordPattern.matches(pass)) {
            _authState.value = AuthState.Error("La contraseña debe tener mín. 8 caracteres, una mayúscula y un carácter especial.")
            return
        }

        if (pass != confirmPass) {
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val fullName = "$name $lastName"
            val result = repository.register(fullName, email.trim(), pass)

            if (result != null) {
                SessionManager.token = result.token
                SessionManager.userEmail = email.trim()
                SessionManager.userName = name
                SessionManager.userDni = dni
                SessionManager.birthDate = birthDate // Guardamos la fecha
                SessionManager.isStudent = isStudent

                SessionManager.documents.clear()
                SessionManager.transportCards.clear()
                SessionManager.paymentCards.clear()
                SessionManager.notifications.clear()

                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Error al registrar en el servidor.")
            }
        }
    }
    fun resetState() { _authState.value = AuthState.Idle }
}