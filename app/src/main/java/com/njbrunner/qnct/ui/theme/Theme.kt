package com.njbrunner.qnct.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = DeepIndigo,
    secondary = Sand,
    tertiary = Mint,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = DeepIndigo,
    onTertiary = DeepIndigo,
    onBackground = DeepIndigo,
    onSurface = DeepIndigo
)

private val DarkColorScheme = darkColorScheme(
    primary = Sand,
    secondary = Mint,
    tertiary = Amber,
    background = DeepIndigo,
    surface = DeepIndigo,
    onPrimary = DeepIndigo,
    onSecondary = DeepIndigo,
    onTertiary = DeepIndigo,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun QnctTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
