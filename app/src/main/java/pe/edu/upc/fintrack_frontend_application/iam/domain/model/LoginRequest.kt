package pe.edu.upc.fintrack_frontend_application.iam.domain.model

data class LoginRequest(
    val email: String,
    val password: String
)