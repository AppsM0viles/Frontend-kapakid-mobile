// package pe.edu.upc.fintrack_frontend_application.core.navigation
package pe.edu.upc.fintrack_frontend_application.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object ProfileRoute

@Serializable
object DocumentListRoute

@Serializable
data class DniDetailRoute(val documentId: String)

@Serializable
data class CarneDetailRoute(val documentId: String)

@Serializable
object TransportRoute

@Serializable
object InboxRoute

@Serializable
data class PaymentCardDetailRoute(val cardId: String)