package pe.edu.upc.fintrack_frontend_application.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    background = BackgroundWhite,
    surface = CardBackground,
    onPrimary = Color.White,
    onBackground = TextBlack,
    onSurface = TextBlack
)

@Composable
fun KapakIDTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}