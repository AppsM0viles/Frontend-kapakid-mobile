package pe.edu.upc.fintrack_frontend_application.transportation.domain.model

data class TransportCard(
    val id: String,
    val type: String, // "Metropolitano" o "Línea 1"
    val balance: Double,
    val cardNumber: String,
    val lastRechargeDate: String
)