package pe.edu.upc.fintrack_frontend_application.documents.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarneScreen(documentId: String, onBackClick: () -> Unit) {
    val document = SessionManager.documents.find { it.id == documentId }
    val userName = SessionManager.userName ?: "Usuario"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carné Universitario", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (document == null) {
                Text("Documento no encontrado", color = Color.Red)
                return@Scaffold
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "SUNEDU - Carné Universitario", fontWeight = FontWeight.Bold, color = PrimaryBlue, modifier = Modifier.padding(bottom = 16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.size(80.dp, 100.dp).background(Color.LightGray, RoundedCornerShape(8.dp)))

                        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                            Text(document.institution, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))

                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(text = "Estudiante", fontSize = 10.sp, color = Color.Gray)
                                Text(text = userName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            }
                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(text = "Código / Documento", fontSize = 10.sp, color = Color.Gray)
                                Text(text = document.documentNumber, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Vigencia: ", fontSize = 12.sp, color = Color.Gray)
                        Text(text = document.expirationDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}