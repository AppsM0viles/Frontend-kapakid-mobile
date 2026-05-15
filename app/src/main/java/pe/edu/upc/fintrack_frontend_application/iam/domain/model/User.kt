package pe.edu.upc.fintrack_frontend_application.iam.domain.model

data class User(
    val id: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val isVerified: Boolean,
    val avatarUrl: String? = null
)