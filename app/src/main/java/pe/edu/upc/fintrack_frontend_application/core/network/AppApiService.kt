package pe.edu.upc.fintrack_frontend_application.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class CreateDocumentRequest(
    val userId: String,
    val documentNumber: String,
    val fullName: String,
    val type: Int,
    val issueDate: String,
    val expirationDate: String?,
    val filePath: String?
)

data class CreatePaymentCardRequest(
    val userId: String,
    val fullNumber: String,
    val brand: String,
    val balance: Double,
    val expiryDate: String,
    val cvv: String
)

data class CreateTransportCardRequest(
    val userId: String,
    val type: String,
    val balance: Double,
    val cardNumber: String
)

data class CreateCardResponse(
    val id: String
)

interface AppApiService {
    @GET("api/Documents/user/{userId}")
    suspend fun getDocuments(@Path("userId") userId: String): List<DocumentDto>

    @POST("api/Documents")
    suspend fun createDocument(@Body request: CreateDocumentRequest): DocumentDto

    @GET("api/Wallet/payment-cards/user/{userId}")
    suspend fun getPaymentCards(@Path("userId") userId: String): List<PaymentCardDto>

    @POST("api/Wallet/payment-cards")
    suspend fun createPaymentCard(@Body request: CreatePaymentCardRequest): CreateCardResponse

    @GET("api/Wallet/transport-cards/user/{userId}")
    suspend fun getTransportCards(@Path("userId") userId: String): List<TransportCardDto>

    @POST("api/Wallet/transport-cards")
    suspend fun createTransportCard(@Body request: CreateTransportCardRequest): CreateCardResponse

    @GET("api/Notifications/user/{userId}")
    suspend fun getNotifications(@Path("userId") userId: String): List<NotificationDto>
}