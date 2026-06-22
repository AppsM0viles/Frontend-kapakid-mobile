@file:OptIn(ExperimentalMaterial3Api::class)
package pe.edu.upc.fintrack_frontend_application.transportation.presentation.recharge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBus
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
import pe.edu.upc.fintrack_frontend_application.core.network.PaymentCard
import pe.edu.upc.fintrack_frontend_application.core.network.PaymentTransaction
import pe.edu.upc.fintrack_frontend_application.core.network.RetrofitClient
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.network.UpdatePaymentBalanceRequest
import pe.edu.upc.fintrack_frontend_application.core.network.UpdateTransportBalanceRequest
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.Alert
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.AlertType
import pe.edu.upc.fintrack_frontend_application.transportation.domain.model.TransportCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun RechargeScreen(
    onBackClick: () -> Unit
) {
    val fareAmount = if (SessionManager.isStudent) 1.60 else 3.20
    val fareLabel = if (SessionManager.isStudent) "Universitario (S/ 1.60)" else "General (S/ 3.20)"

    val appApi = remember { RetrofitClient.createService(AppApiService::class.java) }
    val coroutineScope = rememberCoroutineScope()

    var showTravelDialog by remember { mutableStateOf<TransportCard?>(null) }
    var showRechargeDialog by remember { mutableStateOf<TransportCard?>(null) }

    var rechargeAmount by remember { mutableStateOf("") }
    var selectedBankCard by remember { mutableStateOf<PaymentCard?>(null) }
    var actionError by remember { mutableStateOf("") }
    var actionSuccess by remember { mutableStateOf("") }

    val transportCards = SessionManager.transportCards
    val paymentCards = SessionManager.paymentCards

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Transporte", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = PrimaryBlue) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(paddingValues).padding(16.dp)
        ) {
            if (actionSuccess.isNotEmpty()) {
                Text(actionSuccess, color = Color(0xFF4CAF50), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
            }

            if (transportCards.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes tarjetas de transporte.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    items(transportCards) { card ->
                        TransportActionCard(
                            card = card,
                            onUseClick = { showTravelDialog = card; actionSuccess = ""; actionError = "" },
                            onRechargeClick = { showRechargeDialog = card; actionSuccess = ""; actionError = "" }
                        )
                    }
                }
            }
        }

        showTravelDialog?.let { card ->
            AlertDialog(
                onDismissRequest = { showTravelDialog = null },
                title = { Text("Pagar Pasaje") },
                text = {
                    Column {
                        Text("Tarjeta: ${card.type}", fontWeight = FontWeight.Bold)
                        Text("Saldo actual: S/ ${"%.2f".format(card.balance)}", color = PrimaryBlue)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Tarifa a cobrar: $fareLabel")

                        if (actionError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(actionError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (card.balance >= fareAmount) {
                            coroutineScope.launch {
                                try {
                                    val newBalance = card.balance - fareAmount
                                    appApi.updateTransportBalance(UpdateTransportBalanceRequest(card.id, newBalance))

                                    val index = SessionManager.transportCards.indexOfFirst { it.id == card.id }
                                    if (index != -1) {
                                        SessionManager.transportCards[index] = card.copy(balance = newBalance)
                                        val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                                        SessionManager.notifications.add(Alert(UUID.randomUUID().toString(), "Viaje Pagado", "Se descontó S/ ${"%.2f".format(fareAmount)} de tu tarjeta ${card.type} el $dateStr.", "Ver historial", AlertType.INFO))
                                        actionSuccess = "Pasaje cobrado con éxito."
                                        showTravelDialog = null
                                    }
                                } catch (e: Exception) {
                                    actionError = "Error al conectar con el servidor."
                                }
                            }
                        } else {
                            actionError = "Saldo insuficiente. Por favor recarga tu tarjeta."
                        }
                    }) { Text("Pagar S/ ${"%.2f".format(fareAmount)}") }
                },
                dismissButton = { TextButton(onClick = { showTravelDialog = null }) { Text("Cancelar") } }
            )
        }

        showRechargeDialog?.let { card ->
            AlertDialog(
                onDismissRequest = { showRechargeDialog = null; rechargeAmount = ""; selectedBankCard = null },
                title = { Text("Recargar Tarjeta") },
                text = {
                    Column {
                        Text("Destino: ${card.type}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = rechargeAmount,
                            onValueChange = { rechargeAmount = it },
                            label = { Text("Monto a recargar (S/)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (paymentCards.isEmpty()) {
                            Text("No tienes tarjetas bancarias vinculadas.", color = Color.Red, fontSize = 12.sp)
                        } else {
                            Text("Selecciona método de pago:", fontSize = 12.sp, color = Color.Gray)
                            paymentCards.forEach { bankCard ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedBankCard?.id == bankCard.id,
                                        onClick = { selectedBankCard = bankCard }
                                    )
                                    Text("${bankCard.brand} ****${bankCard.fullNumber.takeLast(4)} (S/${"%.2f".format(bankCard.balance)})", fontSize = 12.sp)
                                }
                            }
                        }

                        if (actionError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(actionError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amount = rechargeAmount.toDoubleOrNull() ?: 0.0
                            if (amount <= 0) { actionError = "Ingresa un monto válido."; return@Button }
                            if (selectedBankCard == null) { actionError = "Selecciona una tarjeta bancaria."; return@Button }
                            if (selectedBankCard!!.balance < amount) { actionError = "Tu tarjeta ${selectedBankCard!!.brand} no tiene fondos suficientes."; return@Button }

                            coroutineScope.launch {
                                try {
                                    val newBankBalance = selectedBankCard!!.balance - amount
                                    val newTransportBalance = card.balance + amount

                                    appApi.updatePaymentBalance(UpdatePaymentBalanceRequest(selectedBankCard!!.id, newBankBalance))
                                    appApi.updateTransportBalance(UpdateTransportBalanceRequest(card.id, newTransportBalance))

                                    val bIndex = SessionManager.paymentCards.indexOfFirst { it.id == selectedBankCard!!.id }
                                    val tIndex = SessionManager.transportCards.indexOfFirst { it.id == card.id }

                                    if (bIndex != -1 && tIndex != -1) {
                                        val currentDate = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())
                                        SessionManager.paymentCards[bIndex].balance = newBankBalance
                                        SessionManager.paymentCards[bIndex].transactions.add(0, PaymentTransaction(UUID.randomUUID().toString(), "Recarga ${card.type}", amount, currentDate))
                                        SessionManager.transportCards[tIndex] = card.copy(balance = newTransportBalance, lastRechargeDate = currentDate)
                                        SessionManager.notifications.add(Alert(UUID.randomUUID().toString(), "Recarga Exitosa", "Recargaste S/ ${"%.2f".format(amount)} a tu ${card.type}.", "Ver saldo", AlertType.INFO))

                                        actionSuccess = "Recarga de S/ ${"%.2f".format(amount)} completada."
                                        showRechargeDialog = null
                                        rechargeAmount = ""
                                        selectedBankCard = null
                                    }
                                } catch (e: Exception) {
                                    actionError = "Error al procesar la recarga en el servidor."
                                }
                            }
                        },
                        enabled = paymentCards.isNotEmpty()
                    ) { Text("Confirmar Recarga") }
                },
                dismissButton = { TextButton(onClick = { showRechargeDialog = null; rechargeAmount = ""; selectedBankCard = null }) { Text("Cancelar") } }
            )
        }
    }
}

@Composable
fun TransportActionCard(card: TransportCard, onUseClick: () -> Unit, onRechargeClick: () -> Unit) {
    val bgColor = when (card.type) {
        "Metropolitano" -> Color(0xFFFBC02D)
        "Línea 1" -> Color(0xFF2E7D32)
        "Línea 2" -> Color(0xFFC62828)
        "Corredor" -> Color(0xFF1565C0)
        else -> Color(0xFF424242)
    }

    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1.6f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = card.type, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color.White)
            }

            Column {
                Text(text = "Saldo Actual", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text(text = "S/ ${"%.2f".format(card.balance)}", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = onUseClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Usar (Viajar)", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onRechargeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.3f), contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Recargar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}