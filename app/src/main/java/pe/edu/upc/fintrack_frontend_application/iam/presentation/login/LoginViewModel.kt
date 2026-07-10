package pe.edu.upc.fintrack_frontend_application.iam.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.fintrack_frontend_application.core.network.AppApiService
import pe.edu.upc.fintrack_frontend_application.core.network.PaymentCard
import pe.edu.upc.fintrack_frontend_application.core.network.RetrofitClient
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.documents.domain.model.DigitalDocument
import pe.edu.upc.fintrack_frontend_application.documents.domain.model.DocumentType
import pe.edu.upc.fintrack_frontend_application.iam.data.repository.IamRepository
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.Alert
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.AlertType
import pe.edu.upc.fintrack_frontend_application.transportation.domain.model.TransportCard

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class LoginViewModel : ViewModel() {
    private val repository = IamRepository()
    private val appApi = RetrofitClient.createService(AppApiService::class.java)

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
                SessionManager.userId = response.user.id
                SessionManager.userEmail = response.user.email
                SessionManager.userName = response.user.nombre
                SessionManager.userDni = response.user.dni
                SessionManager.birthDate = response.user.fechaNacimiento
                SessionManager.isStudent = response.user.esUniversitario

                SessionManager.documents.clear()
                SessionManager.paymentCards.clear()
                SessionManager.transportCards.clear()
                SessionManager.notifications.clear()

                try {



                    val docs = appApi.getDocuments(response.user.id)

                    println("USER ID = ${response.user.id}")
                    println("DOCUMENTOS = ${docs.size}")

                    docs.forEach {
                        println(it)
                    }



                    docs.forEach { doc ->
                        SessionManager.documents.add(
                            DigitalDocument(
                                id = doc.id,
                                type = if (doc.type == 1) DocumentType.DNI else DocumentType.CARNE_UNIVERSITARIO,
                                ownerName = doc.fullName,
                                documentNumber = doc.documentNumber,
                                expirationDate = doc.expirationDate ?: "N/A",
                                institution = if (doc.type == 1) "RENIEC" else "Universidad",
                                isVerified = true,
                                extraInfo = "",
                                isEdited = false
                            )
                        )
                    }
                } catch (e: Exception) {}

                try {
                    val pCards = appApi.getPaymentCards(response.user.id)
                    pCards.forEach { card ->
                        SessionManager.paymentCards.add(
                            PaymentCard(
                                id = card.id,
                                fullNumber = card.fullNumber,
                                brand = card.brand,
                                balance = card.balance,
                                expiryDate = card.expiryDate,
                                cvv = card.cvv
                            )
                        )
                    }
                } catch (e: Exception) {}

                try {
                    val tCards = appApi.getTransportCards(response.user.id)
                    tCards.forEach { card ->
                        SessionManager.transportCards.add(
                            TransportCard(
                                id = card.id,
                                type = card.type,
                                balance = card.balance,
                                cardNumber = card.cardNumber,
                                lastRechargeDate = card.lastRechargeDate
                            )
                        )
                    }
                } catch (e: Exception) {}

                try {
                    val notifs = appApi.getNotifications(response.user.id)
                    notifs.forEach { notif ->
                        SessionManager.notifications.add(
                            Alert(
                                id = notif.id,
                                title = notif.title,
                                description = notif.message,
                                actionText = "Ver detalle",
                                type = when (notif.type) {
                                    2 -> AlertType.WARNING
                                    3 -> AlertType.ERROR
                                    else -> AlertType.INFO
                                }
                            )
                        )
                    }
                } catch (e: Exception) {}

                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Correo o contraseña incorrectos")
            }
        }
    }

    fun resetState() { _authState.value = AuthState.Idle }
}