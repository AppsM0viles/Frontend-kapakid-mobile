@file:OptIn(ExperimentalMaterial3Api::class)
package pe.edu.upc.fintrack_frontend_application.documents.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue

@Composable
fun PaymentCardDetailScreen(cardId: String, onBackClick: () -> Unit) {
    val card = SessionManager.paymentCards.find { it.id == cardId }
    var showDetails by remember { mutableStateOf(false) }

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
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(tx.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(tx.date, color = Color.Gray, fontSize = 12.sp)
                                }
                                Text("- S/ ${"%.2f".format(tx.amount)}", fontWeight = FontWeight.Bold, color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}