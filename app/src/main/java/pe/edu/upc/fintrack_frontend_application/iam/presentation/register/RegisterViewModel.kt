package pe.edu.upc.fintrack_frontend_application.iam.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.fintrack_frontend_application.iam.data.repository.IamRepository
import pe.edu.upc.fintrack_frontend_application.iam.domain.model.User
import pe.edu.upc.fintrack_frontend_application.iam.presentation.login.AuthState

class RegisterViewModel : ViewModel() {
    private val repository = IamRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun register(name: String, lastName: String, email: String, pass: String, confirmPass: String) {
        if (name.isBlank() || lastName.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Completa todos los campos")
            return
        }
        if (pass != confirmPass) {
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val newUser = User(email = email.trim(), password = pass, firstName = name, lastName = lastName)
            val result = repository.register(newUser)
            if (result != null) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Error al registrar el usuario. Revisa tu red.")
            }
        }
    }
    fun resetState() { _authState.value = AuthState.Idle }
}