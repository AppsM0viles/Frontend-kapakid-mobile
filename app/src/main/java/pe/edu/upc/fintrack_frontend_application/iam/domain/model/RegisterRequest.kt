package pe.edu.upc.fintrack_frontend_application.iam.domain.model

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)