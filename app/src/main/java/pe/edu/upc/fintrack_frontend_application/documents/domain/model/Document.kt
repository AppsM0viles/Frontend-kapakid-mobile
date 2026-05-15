package pe.edu.upc.fintrack_frontend_application.documents.domain.model

enum class DocumentType {
    DNI, CARNE_UNIVERSITARIO
}

data class DigitalDocument(
    val id: String,
    val type: DocumentType,
    val ownerName: String,
    val documentNumber: String,
    val expirationDate: String,
    val institution: String,
    val isVerified: Boolean
)