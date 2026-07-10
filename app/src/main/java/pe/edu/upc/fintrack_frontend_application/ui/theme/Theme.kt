package pe.edu.upc.fintrack_frontend_application.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import pe.edu.upc.fintrack_frontend_application.core.ui.theme.PrimaryBlue

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    background = DarkBackground,
    surface = CardDark,
    onPrimary = Color.White,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun KapakIDTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}