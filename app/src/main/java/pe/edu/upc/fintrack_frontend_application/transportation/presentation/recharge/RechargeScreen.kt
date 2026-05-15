@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upc.fintrack_frontend_application.transportation.presentation.recharge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue

@Composable
fun RechargeScreen(
    onBackClick: () -> Unit
) {
    var customAmount by remember { mutableStateOf("138") }
    val selectedOption = remember { mutableStateOf(20) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recargas", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Recargar Metropolitano", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Añade saldo a tu tarjeta Metropolitano", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Saldo actual", fontSize = 12.sp, color = Color.Gray)
                    Text(text = "S/ 12.50", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Tarjeta: 1234 1234 1234 1234", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Opciones rápidas", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RechargeOptionButton("S/ 10", isSelected = false, modifier = Modifier.weight(1f))
                RechargeOptionButton("S/ 20", isSelected = true, modifier = Modifier.weight(1f))
                RechargeOptionButton("S/ 50", isSelected = false, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Monto personalizado", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customAmount,
                onValueChange = { customAmount = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Pagar con Tarjeta", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Métodos aceptados: Visa, Mastercard, American Express.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun RechargeOptionButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Button(
        onClick = { },
        modifier = modifier.height(45.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PrimaryBlue else Color(0xFFE8EAF6)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text, color = if (isSelected) Color.White else Color.Black, fontSize = 14.sp)
    }
}