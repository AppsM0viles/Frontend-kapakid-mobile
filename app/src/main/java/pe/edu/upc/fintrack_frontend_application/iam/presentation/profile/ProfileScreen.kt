package pe.edu.upc.fintrack_frontend_application.iam.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.SuccessGreen

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit
) {
    var currentName by remember { mutableStateOf(SessionManager.userName ?: "Usuario") }
    var currentEmail by remember { mutableStateOf(SessionManager.userEmail ?: "") }
    var currentBirth by remember { mutableStateOf(SessionManager.birthDate ?: "") }

    var showEditProfile by remember { mutableStateOf(false) }
    var showSecurity by remember { mutableStateOf(false) }
    var showPaymentMethods by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }
    var showHelp by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                modifier = Modifier.size(88.dp).clip(CircleShape).background(Color(0xFFE0E0E0)).padding(16.dp),
                tint = Color.Gray
            )
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).size(32.dp).background(PrimaryBlue, CircleShape).clickable { showEditProfile = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = currentName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = currentEmail, fontSize = 14.sp, color = Color.Gray)

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 12.dp)) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Verificada", tint = SuccessGreen, modifier = Modifier.size(18.dp))
            Text(text = "Identidad Verificada (DNI: ${SessionManager.userDni})", fontSize = 13.sp, color = SuccessGreen, modifier = Modifier.padding(start = 6.dp))
        }

        Spacer(modifier = Modifier.height(48.dp))

        ProfileMenuButton("Seguridad y Biometría", onClick = { showSecurity = true })
        ProfileMenuButton("Mis Tarjetas Bancarias", onClick = { showPaymentMethods = true })
        ProfileMenuButton("Términos y Privacidad", onClick = { showTerms = true })
        ProfileMenuButton("Centro de Ayuda", onClick = { showHelp = true })

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = { SessionManager.clearSession(); onLogoutClick() }) {
            Text("Cerrar Sesión", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }

    if (showEditProfile) {
        var editName by remember { mutableStateOf(currentName) }
        var editEmail by remember { mutableStateOf(currentEmail) }
        var editBirth by remember { mutableStateOf(currentBirth) }

        AlertDialog(
            onDismissRequest = { showEditProfile = false },
            title = { Text("Editar Perfil") },
            text = {
                Column {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = editEmail, onValueChange = { editEmail = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = editBirth, onValueChange = { editBirth = it }, label = { Text("Fecha de Nacimiento") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(value = SessionManager.userDni ?: "", onValueChange = { }, label = { Text("Documento de Identidad") }, enabled = false, modifier = Modifier.fillMaxWidth())
                    Text("Por motivos de seguridad, el DNI no puede modificarse.", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                }
            },
            confirmButton = {
                Button(onClick = {
                    SessionManager.userName = editName
                    SessionManager.userEmail = editEmail
                    SessionManager.birthDate = editBirth
                    currentName = editName
                    currentEmail = editEmail
                    currentBirth = editBirth
                    showEditProfile = false
                }) { Text("Guardar Cambios") }
            },
            dismissButton = { TextButton(onClick = { showEditProfile = false }) { Text("Cancelar") } }
        )
    }

    if (showSecurity) {
        var useFingerprint by remember { mutableStateOf(true) }
        var useFaceId by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { showSecurity = false },
            title = { Text("Seguridad y Biometría") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Acceso con Huella Dactilar", modifier = Modifier.weight(1f), fontSize = 15.sp)
                        Switch(checked = useFingerprint, onCheckedChange = { useFingerprint = it }, colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Acceso con FaceID", modifier = Modifier.weight(1f), fontSize = 15.sp)
                        Switch(checked = useFaceId, onCheckedChange = { useFaceId = it }, colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue))
                    }
                }
            },
            confirmButton = { Button(onClick = { showSecurity = false }) { Text("Guardar") } }
        )
    }

    if (showPaymentMethods) {
        AlertDialog(
            onDismissRequest = { showPaymentMethods = false },
            title = { Text("Tarjetas Bancarias") },
            text = {
                if (SessionManager.paymentCards.isEmpty()) {
                    Text("No tienes tarjetas registradas.", color = Color.Gray)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(SessionManager.paymentCards) { card ->
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("${card.brand} ****${card.fullNumber.takeLast(4)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Fondos disponibles: S/ ${"%.2f".format(card.balance)}", color = PrimaryBlue, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { Button(onClick = { showPaymentMethods = false }) { Text("Cerrar") } }
        )
    }

    if (showTerms) {
        AlertDialog(
            onDismissRequest = { showTerms = false },
            title = { Text("Términos y Privacidad") },
            text = { Text("KapakID protege tus datos mediante cifrado de extremo a extremo. Tu DNI y métodos de pago no son compartidos con terceros sin tu consentimiento explícito. Al usar la aplicación, aceptas nuestros acuerdos de servicio y políticas de uso de datos personales según la ley peruana.", lineHeight = 20.sp) },
            confirmButton = { Button(onClick = { showTerms = false }) { Text("Aceptar") } }
        )
    }

    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            title = { Text("Centro de Ayuda") },
            text = { Text("Si tienes problemas con una recarga o validación de identidad, contáctanos a soporte@kapakid.pe o llama al 01-800-KAPAK. Nuestro horario de atención es de Lunes a Viernes de 8:00 AM a 6:00 PM.", lineHeight = 20.sp) },
            confirmButton = { Button(onClick = { showHelp = false }) { Text("Entendido") } }
        )
    }
}

@Composable
fun ProfileMenuButton(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)).background(Color.White, RoundedCornerShape(12.dp)).clickable { onClick() }.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}