package pe.edu.upc.fintrack_frontend_application.documents.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.edu.upc.fintrack_frontend_application.core.network.AppApiService
import pe.edu.upc.fintrack_frontend_application.core.network.CreateDocumentRequest
import pe.edu.upc.fintrack_frontend_application.core.network.CreatePaymentCardRequest
import pe.edu.upc.fintrack_frontend_application.core.network.CreateTransportCardRequest
import pe.edu.upc.fintrack_frontend_application.core.network.PaymentCard
import pe.edu.upc.fintrack_frontend_application.core.network.RetrofitClient
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.ui.components.BottomNavBar
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.documents.domain.model.DigitalDocument
import pe.edu.upc.fintrack_frontend_application.documents.domain.model.DocumentType
import pe.edu.upc.fintrack_frontend_application.transportation.domain.model.TransportCard
import kotlin.random.Random

@Composable
fun DocumentListScreen(
    onNavigateToDni: (String) -> Unit,
    onNavigateToCarne: (String) -> Unit,
    onNavigateToPaymentCard: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRecharges: () -> Unit,
    onNavigateToInbox: () -> Unit
) {
    var dynamicName by remember { mutableStateOf(SessionManager.userName ?: "Usuario") }
    val appApi = remember { RetrofitClient.createService(AppApiService::class.java) }
    val coroutineScope = rememberCoroutineScope()

    var showAddMenu by remember { mutableStateOf(false) }
    var showDocumentDialog by remember { mutableStateOf(false) }
    var showTransportDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(SessionManager.userName) {
        dynamicName = SessionManager.userName ?: "Usuario"
    }

    Scaffold(
        containerColor = BackgroundWhite,
        bottomBar = { BottomNavBar("DocumentListRoute", { }, onNavigateToInbox, onNavigateToRecharges, onNavigateToProfile) },
        floatingActionButton = {
            Box {
                FloatingActionButton(onClick = { showAddMenu = true }, containerColor = PrimaryBlue, shape = RoundedCornerShape(16.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.White)
                }
                DropdownMenu(expanded = showAddMenu, onDismissRequest = { showAddMenu = false }) {
                    DropdownMenuItem(text = { Text("Añadir Credencial") }, onClick = { showAddMenu = false; showDocumentDialog = true })
                    DropdownMenuItem(text = { Text("Añadir Tarjeta Bancaria") }, onClick = { showAddMenu = false; showPaymentDialog = true })
                    DropdownMenuItem(text = { Text("Añadir Tarjeta Transporte") }, onClick = { showAddMenu = false; showTransportDialog = true })
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(paddingValues).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "Hola, $dynamicName", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = "Tu billetera digital KapakID", fontSize = 14.sp, color = Color.Gray)
                }
                Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.size(44.dp).background(Color(0xFFE8EAF6), RoundedCornerShape(22.dp)).padding(10.dp), tint = PrimaryBlue)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Credenciales", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            if (SessionManager.documents.isEmpty()) {
                Text("No tienes documentos vinculados.", color = Color.Gray, fontSize = 14.sp)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(SessionManager.documents) { doc ->
                        val bgColor = if (doc.type == DocumentType.DNI) Color(0xFF0D47A1) else Color(0xFFB71C1C)
                        WalletCardTemplate(
                            title = if (doc.type == DocumentType.DNI) "DNI" else "Carné Universitario",
                            number = doc.documentNumber,
                            backgroundColor = bgColor,
                            icon = Icons.Default.Person,
                            onClick = { if (doc.type == DocumentType.DNI) onNavigateToDni(doc.id) else onNavigateToCarne(doc.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Billetera", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            if (SessionManager.paymentCards.isEmpty()) {
                Text("Agrega tarjetas bancarias para recargar.", color = Color.Gray, fontSize = 14.sp)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(SessionManager.paymentCards) { pCard ->
                        val bgColor = when (pCard.brand) {
                            "VISA" -> Color(0xFF1A1F71)
                            "MASTERCARD" -> Color(0xFFEB001B)
                            "AMEX" -> Color(0xFF2196F3)
                            else -> Color(0xFF37474F)
                        }
                        WalletCardTemplate(
                            title = pCard.brand,
                            number = "**** **** **** ${pCard.fullNumber.takeLast(4)}",
                            backgroundColor = bgColor,
                            icon = Icons.Default.CreditCard,
                            onClick = { onNavigateToPaymentCard(pCard.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Transporte", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            if (SessionManager.transportCards.isEmpty()) {
                Text("Agrega tarjetas de transporte.", color = Color.Gray, fontSize = 14.sp)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(SessionManager.transportCards) { card ->
                        val bgColor = when (card.type) {
                            "Metropolitano" -> Color(0xFFFBC02D)
                            "Línea 1" -> Color(0xFF2E7D32)
                            else -> Color(0xFF424242)
                        }
                        WalletCardTemplate(
                            title = card.type,
                            number = "S/ ${"%.2f".format(card.balance)}",
                            backgroundColor = bgColor,
                            icon = Icons.Default.DirectionsBus,
                            onClick = { onNavigateToRecharges() }
                        )
                    }
                }
            }
        }

        if (showDocumentDialog) {
            var selectedType by remember { mutableStateOf(DocumentType.DNI) }
            AlertDialog(
                onDismissRequest = { showDocumentDialog = false; errorMessage = "" },
                title = { Text("Vincular Credencial") },
                text = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedType == DocumentType.DNI, onClick = { selectedType = DocumentType.DNI })
                            Text("DNI Digital")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedType == DocumentType.CARNE_UNIVERSITARIO, onClick = { selectedType = DocumentType.CARNE_UNIVERSITARIO })
                            Text("Carné Universitario")
                        }
                        if (errorMessage.isNotEmpty()) Text(errorMessage, color = Color.Red, fontSize = 12.sp)
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (selectedType == DocumentType.DNI && SessionManager.documents.any { it.type == DocumentType.DNI }) {
                            errorMessage = "Ya tienes un DNI registrado."
                        } else if (selectedType == DocumentType.CARNE_UNIVERSITARIO && SessionManager.documents.any { it.type == DocumentType.CARNE_UNIVERSITARIO }) {
                            errorMessage = "Ya tienes un Carné registrado."
                        } else {
                            coroutineScope.launch {
                                try {
                                    val responseDoc = appApi.createDocument(
                                        CreateDocumentRequest(
                                            userId = SessionManager.userId ?: "",
                                            documentNumber = SessionManager.userDni ?: "",
                                            fullName = SessionManager.userName ?: "",
                                            type = if (selectedType == DocumentType.DNI) 1 else 4,
                                            issueDate = "2026-06-19T00:00:00Z",
                                            expirationDate = "2034-06-19T00:00:00Z",
                                            filePath = ""
                                        )
                                    )
                                    SessionManager.documents.add(
                                        DigitalDocument(
                                            id = responseDoc.id,
                                            type = if (responseDoc.type == 1) DocumentType.DNI else DocumentType.CARNE_UNIVERSITARIO,
                                            ownerName = responseDoc.fullName,
                                            documentNumber = responseDoc.documentNumber,
                                            expirationDate = "12/12/2030",
                                            institution = if (responseDoc.type == 1) "RENIEC" else "Universidad",
                                            isVerified = true,
                                            extraInfo = "",
                                            isEdited = false
                                        )
                                    )
                                    showDocumentDialog = false
                                    errorMessage = ""
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                }
                            }
                        }
                    }) { Text("Vincular") }
                },
                dismissButton = { TextButton(onClick = { showDocumentDialog = false }) { Text("Cancelar") } }
            )
        }

        if (showPaymentDialog) {
            var cardNumber by remember { mutableStateOf("") }
            var expiry by remember { mutableStateOf("") }
            var cvv by remember { mutableStateOf("") }

            val cardBrand by derivedStateOf {
                val clean = cardNumber.replace("-", "")
                when {
                    clean.startsWith("4") -> "VISA"
                    clean.matches("^5[1-5].*".toRegex()) -> "MASTERCARD"
                    clean.matches("^3[47].*".toRegex()) -> "AMEX"
                    clean.length > 1 -> "Desconocido"
                    else -> "Bancaria"
                }
            }

            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                title = { Text("Añadir Tarjeta Bancaria") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { newValue ->
                                val clean = newValue.replace("-", "").take(16)
                                cardNumber = clean.chunked(4).joinToString("-")
                            },
                            label = { Text("Número de Tarjeta") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = expiry,
                                onValueChange = { newValue ->
                                    val clean = newValue.replace("/", "").take(4)
                                    expiry = if (clean.length > 2) clean.substring(0, 2) + "/" + clean.substring(2) else clean
                                },
                                label = { Text("MM/AA") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { if (it.length <= 4) cvv = it },
                                label = { Text("CVV") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(cardBrand, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val cleanNumber = cardNumber.replace("-", "")
                            if (cleanNumber.length >= 14 && cardBrand != "Desconocido" && expiry.length == 5 && cvv.length >= 3) {
                                coroutineScope.launch {
                                    try {
                                        val randomBalance = 100.0 + (2000.0 - 100.0) * Random.nextDouble()
                                        val responseCard = appApi.createPaymentCard(
                                            CreatePaymentCardRequest(
                                                userId = SessionManager.userId ?: "",
                                                fullNumber = cleanNumber,
                                                brand = cardBrand,
                                                balance = randomBalance,
                                                expiryDate = expiry,
                                                cvv = cvv
                                            )
                                        )
                                        SessionManager.paymentCards.add(
                                            PaymentCard(
                                                id = responseCard.id,
                                                fullNumber = cleanNumber,
                                                brand = cardBrand,
                                                balance = randomBalance,
                                                expiryDate = expiry,
                                                cvv = cvv
                                            )
                                        )
                                        showPaymentDialog = false
                                    } catch (e: Exception) {
                                        errorMessage = "Error al guardar la tarjeta: ${e.message}"
                                    }
                                }
                            }
                        },
                        enabled = cardNumber.replace("-", "").length >= 14 && expiry.length == 5 && cvv.length >= 3
                    ) { Text("Guardar") }
                },
                dismissButton = { TextButton(onClick = { showPaymentDialog = false }) { Text("Cancelar") } }
            )
        }

        if (showTransportDialog) {
            var selectedTransport by remember { mutableStateOf("Metropolitano") }
            var cardNumber by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showTransportDialog = false },
                title = { Text("Tarjeta de Transporte") },
                text = {
                    Column {
                        listOf("Metropolitano", "Línea 1", "Línea 2", "Corredor").forEach { transport ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedTransport == transport, onClick = { selectedTransport = transport })
                                Text(transport)
                            }
                        }
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { newValue ->
                                val clean = newValue.replace("-", "").take(16)
                                cardNumber = clean.chunked(4).joinToString("-")
                            },
                            label = { Text("Número de Tarjeta") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val cleanNumber = cardNumber.replace("-", "")
                        if (cleanNumber.isNotBlank()) {
                            coroutineScope.launch {
                                try {
                                    val responseCard = appApi.createTransportCard(
                                        CreateTransportCardRequest(
                                            userId = SessionManager.userId ?: "",
                                            type = selectedTransport,
                                            balance = 0.0,
                                            cardNumber = cleanNumber
                                        )
                                    )
                                    SessionManager.transportCards.add(
                                        TransportCard(
                                            id = responseCard.id,
                                            type = selectedTransport,
                                            balance = 0.0,
                                            cardNumber = cleanNumber,
                                            lastRechargeDate = "Hoy"
                                        )
                                    )
                                    showTransportDialog = false
                                } catch (e: Exception) {
                                    errorMessage = "Error al guardar la tarjeta de transporte"
                                }
                            }
                        }
                    }) { Text("Añadir") }
                },
                dismissButton = { TextButton(onClick = { showTransportDialog = false }) { Text("Cancelar") } }
            )
        }
    }
}

@Composable
fun WalletCardTemplate(title: String, number: String, backgroundColor: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(260.dp).aspectRatio(1.58f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Text(text = number, color = Color.White, fontSize = 18.sp, letterSpacing = 2.sp)
        }
    }
}