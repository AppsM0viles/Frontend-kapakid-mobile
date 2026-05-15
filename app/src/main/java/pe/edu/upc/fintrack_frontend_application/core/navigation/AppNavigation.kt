package pe.edu.upc.fintrack_frontend_application.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import pe.edu.upc.fintrack_frontend_application.iam.presentation.login.LoginScreen
import pe.edu.upc.fintrack_frontend_application.iam.presentation.profile.ProfileScreen
import pe.edu.upc.fintrack_frontend_application.documents.presentation.list.DocumentListScreen
import pe.edu.upc.fintrack_frontend_application.documents.presentation.detail.DniScreen
import pe.edu.upc.fintrack_frontend_application.documents.presentation.detail.CarneScreen
import pe.edu.upc.fintrack_frontend_application.transportation.presentation.recharge.RechargeScreen
import pe.edu.upc.fintrack_frontend_application.notifications.presentation.InboxScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(DocumentListRoute) { popUpTo(LoginRoute) { inclusive = true } }
                }
            )
        }

        composable<DocumentListRoute> {
            DocumentListScreen(
                onNavigateToDni = { navController.navigate(DniDetailRoute(it)) },
                onNavigateToCarne = { navController.navigate(CarneDetailRoute(it)) },
                onNavigateToProfile = { navController.navigate(ProfileRoute) { launchSingleTop = true } },
                onNavigateToRecharges = { navController.navigate(TransportRoute) { launchSingleTop = true } },
                onNavigateToInbox = { navController.navigate(InboxRoute) { launchSingleTop = true } }
            )
        }

        composable<DniDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DniDetailRoute>()
            DniScreen(documentId = route.documentId, onBackClick = { navController.navigateUp() })
        }

        composable<CarneDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CarneDetailRoute>()
            CarneScreen(documentId = route.documentId, onBackClick = { navController.navigateUp() })
        }

        composable<TransportRoute> {
            RechargeScreen(onBackClick = { navController.navigateUp() })
        }

        composable<InboxRoute> {
            InboxScreen(
                onNavigateToHome = { navController.navigate(DocumentListRoute) { popUpTo(0) { inclusive = true } } },
                onNavigateToRecharges = { navController.navigate(TransportRoute) { launchSingleTop = true } },
                onNavigateToProfile = { navController.navigate(ProfileRoute) { launchSingleTop = true } }
            )
        }

        composable<ProfileRoute> {
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate(LoginRoute) { popUpTo(0) { inclusive = true } }
                }
            )
        }
    }
}