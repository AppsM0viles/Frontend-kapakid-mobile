package pe.edu.upc.fintrack_frontend_application.notifications.domain.model

data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val actionText: String,
    val type: AlertType
)

enum class AlertType {
    WARNING, // Vencimientos, saldo bajo
    INFO,    // Renovaciones, comunicados
    ERROR    // Recargas fallidas
}