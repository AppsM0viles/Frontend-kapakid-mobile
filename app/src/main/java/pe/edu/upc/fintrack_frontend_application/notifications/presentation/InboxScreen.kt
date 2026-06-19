@file:OptIn(ExperimentalMaterial3Api::class)
package pe.edu.upc.fintrack_frontend_application.notifications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.fintrack_frontend_application.core.network.SessionManager
import pe.edu.upc.fintrack_frontend_application.core.ui.components.BottomNavBar
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.BackgroundWhite
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.Alert
import pe.edu.upc.fintrack_frontend_application.notifications.domain.model.AlertType

@Composable
fun InboxScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRecharges: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val alerts = SessionManager.notifications.reversed()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buzón de Notificaciones", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                actions = {
                    Text(
                        text = "${alerts.size} alertas",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "InboxRoute",
                onNavigateToHome = onNavigateToHome,
                onNavigateToInbox = { },
                onNavigateToRecharges = onNavigateToRecharges,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            if (alerts.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxSize().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                        Text("No tienes notificaciones recientes.", color = Color.Gray)
                    }
                }
            } else {
                items(alerts) { alert ->
                    AlertItemCard(alert)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun AlertItemCard(alert: Alert) {
    val (icon, iconColor, bgColor) = when (alert.type) {
        AlertType.WARNING -> Triple(Icons.Default.Warning, Color(0xFFFFA000), Color(0xFFFFF8E1))
        AlertType.INFO -> Triple(Icons.Default.AccountBalance, PrimaryBlue, Color(0xFFE8EAF6))
        AlertType.ERROR -> Triple(Icons.Default.Cancel, Color.Red, Color(0xFFFFEBEE))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alert.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = alert.description,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}