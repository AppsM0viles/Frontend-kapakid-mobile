package pe.edu.upc.fintrack_frontend_application.iam.domain.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val isVerified: Boolean = false,
    val avatarUrl: String? = null
)