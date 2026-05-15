package pe.edu.upc.fintrack_frontend_application.documents.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.ui.components.BottomNavBar
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue

@Composable
fun DocumentListScreen(
    onNavigateToDni: (String) -> Unit,
    onNavigateToCarne: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRecharges: () -> Unit,
    onNavigateToInbox: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "DocumentListRoute",
                onNavigateToHome = { },
                onNavigateToInbox = onNavigateToInbox,
                onNavigateToRecharges = onNavigateToRecharges,
                onNavigateToProfile = onNavigateToProfile
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hola, Ana María", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Bienvenida a KapakID", fontSize = 14.sp, color = Color.Gray)
                }
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
                        .padding(8.dp),
                    tint = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Documentos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Miniatura DNI
                DocumentCardMiniature(
                    title = "DNI Digital",
                    status = "Verificado",
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToDni("dni_123") }
                )

                // Miniatura Carné
                DocumentCardMiniature(
                    title = "Carné Universitario",
                    status = "Activo",
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToCarne("carne_123") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Mi Transporte", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Secciones de Transporte (Mocks rápidos para el Home)
            TransportBalanceCard("Saldo Metropolitano", "S/ 12.50", "10 mar 2026")
            Spacer(modifier = Modifier.height(12.dp))
            TransportBalanceCard("Saldo Línea 1", "S/ 2.50", "9 mar 2026")
        }
    }
}

@Composable
fun DocumentCardMiniature(title: String, status: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = status, fontSize = 12.sp, color = Color(0xFF4CAF50))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Ver", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TransportBalanceCard(title: String, balance: String, lastRecharge: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontSize = 14.sp, color = Color.Gray)
                Text(text = balance, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Última recarga: $lastRecharge", fontSize = 12.sp, color = Color.Gray)
            }
            Button(
                onClick = { /* Navegar a recargas */ },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Recargar")
            }
        }
    }
}