package pe.edu.upc.fintrack_frontend_application.iam.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.TextGray

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("+51 ") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp),
            tint = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Acceso seguro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Usa tu huella digital para desbloquear KapakID y acceder a tu identidad y billetera digital de forma segura.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(text = "Iniciar con Huella", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Implementar PIN luego */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Usar PIN", fontSize = 16.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Tu huella se procesa localmente en tu dispositivo. KapakID nunca comparte tu biometría.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = TextGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}