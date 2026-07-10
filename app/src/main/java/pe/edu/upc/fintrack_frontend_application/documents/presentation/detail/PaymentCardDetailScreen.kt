package pe.edu.upc.fintrack_frontend_application.documents.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import pe.edu.upc.fintrack_frontend_application.core.network.PaymentCard
import pe.edu.upc.fintrack_frontend_application.core.network.PaymentTransaction
import pe.edu.upc.fintrack_frontend_application.core.network.RetrofitClient
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.network.UpdatePaymentBalanceRequest
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.Alert
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.AlertType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCardDetailScreen(cardId: String, onBackClick: () -> Unit) {
    val card = SessionManager.paymentCards.find { it.id == cardId }
    var showDetails by remember { mutableStateOf(false) }
    var showPayService by remember { mutableStateOf(false) }
    var showTransfer by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Pair<String, Int>?>(null) }
    var transferAmount by remember { mutableStateOf("") }
    var selectedTargetCard by remember { mutableStateOf<PaymentCard?>(null) }

    val appApi = remember { RetrofitClient.createService(pe.edu.upc.fintrack_frontend_application.core.network.AppApiService::class.java) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarjeta", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = PrimaryBlue) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(paddingValues).padding(16.dp)) {
            if (card == null) {
                Text("Tarjeta no encontrada", color = Color.Red)
                return@Scaffold
            }

            val bgColor = when (card.brand) {
                "VISA" -> Color(0xFF1A1F71)
                "MASTERCARD" -> Color(0xFFEB001B)
                "AMEX" -> Color(0xFF2196F3)
                else -> Color(0xFF37474F)
            }

            Card(
                modifier = Modifier.fillMaxWidth().aspectRatio(1.58f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = card.brand, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showDetails = !showDetails }) {
                            Icon(if (showDetails) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null, tint = Color.White)
                        }
                    }
                    Column {
                        Text(text = "Saldo Disponible", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        Text(text = if (showDetails) "S/ ${"%.2f".format(card.balance)}" else "S/ ****", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = if (showDetails) card.fullNumber.chunked(4).joinToString(" ") else "**** **** **** ${card.fullNumber.takeLast(4)}", color = Color.White, fontSize = 16.sp, letterSpacing = 2.sp)
                        Text(text = if (showDetails) card.expiryDate else "**/**", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Movimientos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (card.transactions.isEmpty()) {
                Text("No hay movimientos recientes.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(card.transactions) { tx ->
                        val isPositive = tx.title.contains("Recarga") || tx.title.contains("Transferencia recibida")
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(tx.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(tx.date, color = Color.Gray, fontSize = 12.sp)
                                }
                                Text(
                                    text = if (isPositive) "+ S/ ${"%.2f".format(tx.amount)}" else "- S/ ${"%.2f".format(tx.amount)}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPositive) Color(0xFF22C55E) else Color.Red
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showPayService = true }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(12.dp)) {
                Text("Pagar Servicios", fontSize = 17.sp)
            }

            Button(onClick = { showTransfer = true }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)), shape = RoundedCornerShape(12.dp)) {
                Text("Transferir a otra tarjeta", fontSize = 17.sp)
            }
        }
    }

    if (showPayService && card != null) {
        val services = listOf("Luz" to Random.nextInt(25, 150), "Agua" to Random.nextInt(15, 90), "Internet" to Random.nextInt(35, 120), "Teléfono" to Random.nextInt(20, 80))
        AlertDialog(
            onDismissRequest = { showPayService = false; selectedService = null },
            title = { Text("Seleccionar Servicio") },
            text = {
                LazyColumn {
                    items(services) { service ->
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { selectedService = service }, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(service.first, fontSize = 16.sp)
                            Text("S/ ${service.second}", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    selectedService?.let { service ->
                        coroutineScope.launch {
                            try {
                                val amount = service.second.toDouble()
                                val newBalance = card.balance - amount
                                appApi.updatePaymentBalance(UpdatePaymentBalanceRequest(card.id, newBalance))
                                val index = SessionManager.paymentCards.indexOfFirst { it.id == card.id }
                                if (index != -1) {
                                    SessionManager.paymentCards[index].balance = newBalance
                                    SessionManager.paymentCards[index].transactions.add(0, PaymentTransaction(UUID.randomUUID().toString(), "Pago ${service.first}", amount, SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())))
                                    SessionManager.notifications.add(Alert(UUID.randomUUID().toString(), "Pago Realizado", "Se pagó S/ $amount por ${service.first}", "Ver detalle", AlertType.INFO))
                                }
                            } catch (e: Exception) {}
                        }
                    }
                    showPayService = false
                    selectedService = null
                }, enabled = selectedService != null) { Text("Pagar ${selectedService?.first ?: ""}") }
            },
            dismissButton = { TextButton(onClick = { showPayService = false }) { Text("Cancelar") } }
        )
    }

    if (showTransfer && card != null) {
        AlertDialog(
            onDismissRequest = { showTransfer = false },
            title = { Text("Transferir Saldo") },
            text = {
                Column {
                    OutlinedTextField(
                        value = transferAmount,
                        onValueChange = { transferAmount = it },
                        label = { Text("Monto a transferir") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Seleccionar tarjeta destino:")
                    SessionManager.paymentCards.filter { it.id != card.id }.forEach { target ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedTargetCard?.id == target.id, onClick = { selectedTargetCard = target })
                            Text("${target.brand} ****${target.fullNumber.takeLast(4)}")
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val amount = transferAmount.toDoubleOrNull() ?: 0.0
                    if (amount > 0 && selectedTargetCard != null && card.balance >= amount) {
                        coroutineScope.launch {
                            try {
                                val newOriginBalance = card.balance - amount
                                val newTargetBalance = selectedTargetCard!!.balance + amount

                                appApi.updatePaymentBalance(UpdatePaymentBalanceRequest(card.id, newOriginBalance))
                                appApi.updatePaymentBalance(UpdatePaymentBalanceRequest(selectedTargetCard!!.id, newTargetBalance))

                                val originIndex = SessionManager.paymentCards.indexOfFirst { it.id == card.id }
                                val targetIndex = SessionManager.paymentCards.indexOfFirst { it.id == selectedTargetCard!!.id }

                                if (originIndex != -1) {
                                    SessionManager.paymentCards[originIndex].balance = newOriginBalance
                                    SessionManager.paymentCards[originIndex].transactions.add(0, PaymentTransaction(UUID.randomUUID().toString(), "Transferencia enviada", amount, SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())))
                                }
                                if (targetIndex != -1) {
                                    SessionManager.paymentCards[targetIndex].balance = newTargetBalance
                                    SessionManager.paymentCards[targetIndex].transactions.add(0, PaymentTransaction(UUID.randomUUID().toString(), "Transferencia recibida", amount, SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())))
                                }

                                showTransfer = false
                                transferAmount = ""
                                selectedTargetCard = null
                            } catch (e: Exception) {}
                        }
                    }
                }) { Text("Transferir") }
            },
            dismissButton = { TextButton(onClick = { showTransfer = false }) { Text("Cancelar") } }
        )
    }
}