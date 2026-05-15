@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upc.fintrack_frontend_application.transportation.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit
) {
    // Datos simulados
    val transactions = listOf(
        "Recarga Metropolitano" to ("05 May 2026 - 10:30 AM" to "S/ 5.00"),
        "Recarga Línea 1" to ("03 May 2026 - 08:15 AM" to "S/ 4.00"),
        "Recarga Metropolitano" to ("28 Abr 2026 - 06:50 PM" to "S/ 9.00"),
        "Recarga Línea 1" to ("20 Abr 2026 - 11:05 AM" to "S/ 10.00"),
        "Recarga Línea 1" to ("15 Abr 2026 - 07:40 AM" to "S/ 6.00")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterTab("Este mes", isSelected = true)
                Spacer(modifier = Modifier.width(8.dp))
                FilterTab("Anteriores", isSelected = false)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(transactions) { (title, data) ->
                    val (date, amount) = data
                    HistoryItem(title, date, amount)
                }
            }
        }
    }
}

@Composable
fun FilterTab(text: String, isSelected: Boolean) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) PrimaryBlue else Color.Transparent,
        modifier = Modifier.height(36.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
            Text(text = text, color = if (isSelected) Color.White else Color.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun HistoryItem(title: String, date: String, amount: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (title.contains("Metropolitano")) Icons.Default.DirectionsBus else Icons.Default.Train,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(24.dp).background(Color(0xFFE8EAF6), RoundedCornerShape(4.dp)).padding(4.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = date, fontSize = 12.sp, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = amount, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = "Exitosa", fontSize = 11.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}