package com.sanxmon.ceki.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CekiDarkColorScheme = darkColorScheme(
    primary = CekiColors.Primary,
    onPrimary = CekiColors.Base,
    background = CekiColors.Base,
    onBackground = CekiColors.Text,
    surface = CekiColors.Mantle,
    onSurface = CekiColors.Text,
    surfaceVariant = CekiColors.Surface0,
    onSurfaceVariant = CekiColors.Subtext0,
    surfaceContainer = CekiColors.Surface0,
    surfaceContainerHigh = CekiColors.Surface1,
    surfaceContainerHighest = CekiColors.Surface2,
    error = CekiColors.Red,
    onError = CekiColors.Base,
    outline = CekiColors.Surface1,
)

/** Dark-only theme. The app has no light variant. */
@Composable
fun CekiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CekiDarkColorScheme,
        content = content,
    )
}
