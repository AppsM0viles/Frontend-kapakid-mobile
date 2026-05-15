package pe.edu.upc.fintrack_frontend_application.transportation.domain.model

data class Transaction(
    val id: String,
    val title: String,
    val date: String,
    val amount: Double,
    val isSuccess: Boolean,
    val type: String // "Metropolitano" o "Línea 1"
)