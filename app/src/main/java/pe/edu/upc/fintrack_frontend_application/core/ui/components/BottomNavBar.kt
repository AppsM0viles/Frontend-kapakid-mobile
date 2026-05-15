package pe.edu.upc.fintrack_frontend_application.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.TextGray

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigateToHome: () -> Unit,
    onNavigateToInbox: () -> Unit,
    onNavigateToRecharges: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryBlue
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "DocumentListRoute",
            onClick = onNavigateToHome,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextGray,
                selectedTextColor = PrimaryBlue,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0xFFE8EAF6)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Buzón") },
            label = { Text("Buzón") },
            selected = currentRoute == "InboxRoute",
            onClick = onNavigateToInbox,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextGray,
                selectedTextColor = PrimaryBlue,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0xFFE8EAF6)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Recargas") },
            label = { Text("Recargas") },
            selected = currentRoute == "TransportRoute",
            onClick = onNavigateToRecharges,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextGray,
                selectedTextColor = PrimaryBlue,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0xFFE8EAF6)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "ProfileRoute",
            onClick = onNavigateToProfile,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextGray,
                selectedTextColor = PrimaryBlue,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0xFFE8EAF6)
            )
        )
    }
}