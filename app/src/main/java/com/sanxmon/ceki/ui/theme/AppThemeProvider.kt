package com.sanxmon.ceki.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** The currently selected [AppTheme], provided for components that need tokens. */
val LocalAppTheme = staticCompositionLocalOf<AppTheme> { ThemeManager.DEFAULT_THEME }

/** Theme manager access (used by the theme selector to switch themes). */
val LocalThemeManager = staticCompositionLocalOf<ThemeManager> {
    error("LocalThemeManager not provided — wrap the app in AppThemeProvider")
}

/**
 * Applies the persisted theme to the whole Compose tree and keeps the system
 * bar icon color in sync (light icons on dark themes, dark icons on light).
 *
 * DataStore → ThemeRepository → ThemeManager → AppThemeProvider → Compose UI
 */
@Composable
fun AppThemeProvider(
    themeManager: ThemeManager,
    content: @Composable () -> Unit,
) {
    val theme by themeManager.currentTheme.collectAsState(initial = ThemeManager.DEFAULT_THEME)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !theme.isDark
                isAppearanceLightNavigationBars = !theme.isDark
            }
        }
    }

    CompositionLocalProvider(
        LocalAppTheme provides theme,
        LocalThemeManager provides themeManager,
    ) {
        MaterialTheme(
            colorScheme = theme.colors.toColorScheme(),
            typography = theme.typography.toTypography(),
            shapes = theme.shapes.toShapes(),
            content = content,
        )
    }
}
