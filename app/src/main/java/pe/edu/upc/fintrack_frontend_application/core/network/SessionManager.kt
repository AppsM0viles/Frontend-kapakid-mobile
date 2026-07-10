// package pe.edu.upc.fintrack_frontend_application.core.network
package pe.edu.upc.fintrack_frontend_application.core.network

import androidx.compose.runtime.mutableStateListOf
import pe.edu.upc.fintrack_frontend_application.documents.domain.model.DigitalDocument
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.Alert
import pe.edu.upc.fintrack_frontend_application.transportation.domain.model.TransportCard

data class PaymentTransaction(val id: String, val title: String, val amount: Double, val date: String)

data class PaymentCard(
    val id: String,
    val fullNumber: String,
    val brand: String,
    var balance: Double,
    val expiryDate: String,
    val cvv: String,
    val transactions: MutableList<PaymentTransaction> = mutableListOf()
)

object SessionManager {
    var userId: String? = null
    var token: String? = null
    var userEmail: String? = null
    var userName: String? = null
    var userDni: String? = null
    var birthDate: String? = null
    var isStudent: Boolean = false

    val documents = mutableStateListOf<DigitalDocument>()
    val transportCards = mutableStateListOf<TransportCard>()
    val paymentCards = mutableStateListOf<PaymentCard>()
    val notifications = mutableStateListOf<Alert>()

    fun clearSession() {
        userId = null
        token = null
        userEmail = null
        userName = null
        userDni = null
        birthDate = null
        isStudent = false
        documents.clear()
        transportCards.clear()
        paymentCards.clear()
        notifications.clear()
    }
}