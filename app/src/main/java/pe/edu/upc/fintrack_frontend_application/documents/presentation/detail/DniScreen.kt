package pe.edu.upc.fintrack_frontend_application.documents.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DniScreen(documentId: String, onBackClick: () -> Unit) {
    val allDocuments = SessionManager.documents
    val document = allDocuments.find { it.id == documentId }
    val userName = SessionManager.userName ?: "Usuario"

    var showEditDialog by remember { mutableStateOf(false) }
    var additionalInfo by remember { mutableStateOf("") }

    val randomUbigeo = remember { (100000..999999).random().toString() }
    val randomCivilStatus = remember { listOf("SOLTERO(A)", "CASADO(A)", "DIVORCIADO(A)").random() }
    val randomSex = remember { listOf("M", "F").random() }
    val randomExpiration = remember { "${(1..28).random().toString().padStart(2, '0')}/${(1..12).random().toString().padStart(2, '0')}/${(2027..2034).random()}" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DNI Digital", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PrimaryBlue) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // === DEBUG INFO ===
            Text("ID buscado: $documentId", fontSize = 12.sp, color = Color.Gray)
            Text("Documentos en lista: ${allDocuments.size}", fontSize = 12.sp, color = Color.Gray)
            Text("IDs disponibles: ${allDocuments.map { it.id }}", fontSize = 10.sp, color = Color.Gray, maxLines = 2)
            Spacer(modifier = Modifier.height(16.dp))

            if (document == null) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❌ Documento no encontrado", color = Color.Red, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Vuelve a la lista e intenta de nuevo", color = Color.Gray)
                    }
                }
                return@Scaffold
            }

            // === CONTENIDO REAL DEL DNI ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "República del Perú", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 20.sp, modifier = Modifier.padding(bottom = 24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nombres Completos", fontSize = 12.sp, color = Color.Gray)
                            Text(userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("DNI", fontSize = 12.sp, color = Color.Gray)
                            Text(document.documentNumber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Fecha de Nacimiento", fontSize = 12.sp, color = Color.Gray)
                            Text(SessionManager.birthDate ?: "--/--/----", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sexo", fontSize = 12.sp, color = Color.Gray)
                            Text(randomSex, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Estado Civil", fontSize = 12.sp, color = Color.Gray)
                            Text(randomCivilStatus, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Ubigeo", fontSize = 12.sp, color = Color.Gray)
                            Text(randomUbigeo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Fecha de Caducidad", fontSize = 12.sp, color = Color.Gray)
                    Text(randomExpiration, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    if (document.isEdited && document.extraInfo.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Información Médica / Alergias", fontSize = 12.sp, color = Color.Gray)
                        Text(document.extraInfo, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (!document.isEdited) {
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir Información Médica", fontSize = 17.sp)
                }
            }
        }

        if (showEditDialog && document != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Información Adicional") },
                text = {
                    Column {
                        Text("Solo puedes editar esta información una vez.", fontSize = 13.sp, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = additionalInfo, onValueChange = { additionalInfo = it }, label = { Text("Datos médicos / Alergias") }, modifier = Modifier.fillMaxWidth())
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val updatedDoc = document.copy(extraInfo = additionalInfo, isEdited = true)
                        val index = SessionManager.documents.indexOfFirst { it.id == document.id }
                        if (index != -1) SessionManager.documents[index] = updatedDoc
                        showEditDialog = false
                    }) { Text("Guardar Definitivamente") }
                },
                dismissButton = { TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") } }
            )
        }
    }
}