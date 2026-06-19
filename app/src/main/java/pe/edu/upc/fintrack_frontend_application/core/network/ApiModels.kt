package pe.edu.upc.fintrack_frontend_application.core.network

data class UserDto(
    val id: String,
    val nombre: String,
    val email: String,
    val dni: String,
    val fechaNacimiento: String,
    val esUniversitario: Boolean,
    val premium: Boolean
)

data class LoginResponse(
    val user: UserDto,
    val token: String
)

data class RegisterResponse(
    val id: String
)

data class DocumentDto(
    val id: String,
    val userId: String,
    val documentNumber: String,
    val fullName: String,
    val type: Int,
    val status: Int,
    val issueDate: String,
    val expirationDate: String?,
    val filePath: String?
)

data class PaymentCardDto(
    val id: String,
    val userId: String,
    val fullNumber: String,
    val brand: String,
    val balance: Double,
    val expiryDate: String,
    val cvv: String
)

data class TransportCardDto(
    val id: String,
    val userId: String,
    val type: String,
    val balance: Double,
    val cardNumber: String,
    val lastRechargeDate: String
)

data class NotificationDto(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: Int,
    val status: Int,
    val createdAt: String
)