package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.AppTheme
import com.sanxmon.ceki.ui.theme.LocalThemeManager
import com.sanxmon.ceki.ui.theme.ThemeManager
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography
import kotlinx.coroutines.launch

/**
 * Appearance bottom sheet: lists the available themes with a color preview and
 * a selected indicator. Tapping a theme applies it immediately (no restart),
 * persisted via ThemeManager → ThemeRepository → DataStore.
 */
@Composable
fun ThemeSelectorSheet(
    onClose: () -> Unit,
) {
    val themeManager = LocalThemeManager.current
    val currentTheme by themeManager.currentTheme.collectAsState(initial = ThemeManager.DEFAULT_THEME)
    val scope = rememberCoroutineScope()

    CekiSheet(onDismissRequest = onClose) {
        Spacer(Modifier.height(14.dp))

        Text(
            text = "Appearance",
            style = MaterialTheme.appTypography.title,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Theme",
            style = MaterialTheme.appTypography.body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        themeManager.availableThemes().forEach { theme ->
            ThemeOptionRow(
                theme = theme,
                selected = theme.id == currentTheme.id,
                onSelect = {
                    scope.launch { themeManager.setTheme(theme.id) }
                },
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ThemeOptionRow(
    theme: AppTheme,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val shape = MaterialTheme.appShapes.card

    CekiPressable(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 3.dp else 2.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = shape,
            )
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                } else {
                    Color.Transparent
                },
                shape = shape,
            )
            .padding(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Color preview: background / primary / accent swatches.
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(end = 12.dp),
            ) {
                PreviewSwatch(theme.colors.background)
                PreviewSwatch(theme.colors.primary)
                PreviewSwatch(theme.colors.accent)
            }
            Text(
                text = theme.displayName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.appTypography.heading,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = if (selected) {
                    Icons.Filled.RadioButtonChecked
                } else {
                    Icons.Filled.RadioButtonUnchecked
                },
                contentDescription = if (selected) "Dipilih" else "Pilih",
                tint = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun PreviewSwatch(color: Color) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.appShapes.badge)
            .background(color),
    )
}
