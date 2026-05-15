package pe.edu.upc.fintrack_frontend_application.core.navigation

import kotlinx.serialization.Serializable

// Contexto IAM
@Serializable
object LoginRoute

@Serializable
object ProfileRoute

// Contexto Documents
@Serializable
object DocumentListRoute

@Serializable
data class DniDetailRoute(val documentId: String)

@Serializable
data class CarneDetailRoute(val documentId: String)

// Contexto Transportation
@Serializable
object TransportRoute

@Serializable
data class RechargeRoute(val cardId: String)

// Contexto Notifications
@Serializable
object InboxRoute