package com.sanxmon.ceki.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

/**
 * Rename dialog mirroring `edit-modal.tsx`: auto-focused input, inline error box
 * with danger border, SIMPAN (solid accent, diagonal cut) / BATAL (outline).
 */
@Composable
fun EditNameDialog(
    nama: String,
    error: String?,
    onNamaChange: (String) -> Unit,
    onClose: () -> Unit,
    onEdit: (String) -> Unit,
) {
    BackHandler(onBack = onClose)
    val focusRequester = remember { FocusRequester() }
    var focused by remember { mutableStateOf(false) }
    val dialogShape = MaterialTheme.appShapes.dialog
    val fieldShape = MaterialTheme.appShapes.field

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline, dialogShape)
                .background(MaterialTheme.colorScheme.surface, dialogShape)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = "GANTI NAMA",
                style = MaterialTheme.appTypography.title,
                color = MaterialTheme.colorScheme.primary,
            )

            if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.error, fieldShape)
                        .background(
                            MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                            fieldShape,
                        )
                        .padding(12.dp),
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.appTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            BasicTextField(
                value = nama,
                onValueChange = onNamaChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.appColors.inputField, fieldShape)
                    .border(
                        width = 2.dp,
                        color = if (focused) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.appColors.inputBorder
                        },
                        shape = fieldShape,
                    )
                    .focusRequester(focusRequester)
                    .onFocusChanged { focused = it.isFocused }
                    .padding(horizontal = 16.dp),
                singleLine = true,
                textStyle = MaterialTheme.appTypography.heading.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onEdit(nama) }),
                decorationBox = { innerTextField ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                        if (nama.isEmpty()) {
                            Text(
                                text = "Masukkan nama...",
                                style = MaterialTheme.appTypography.heading,
                                color = MaterialTheme.appColors.textFaint,
                            )
                        }
                        innerTextField()
                    }
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                DialogButton(
                    text = "BATAL",
                    textColor = MaterialTheme.colorScheme.onSurface,
                    background = Color.Transparent,
                    pressedBackground = MaterialTheme.appColors.surfacePressed,
                    borderColor = MaterialTheme.colorScheme.outline,
                    weight = Modifier.weight(1f),
                    onClick = onClose,
                )
                DialogButton(
                    text = "SIMPAN",
                    textColor = MaterialTheme.appColors.onAccent,
                    background = MaterialTheme.appColors.accent,
                    pressedBackground = MaterialTheme.appColors.accentPressed,
                    fontWeight = FontWeight.Black,
                    diagonal = true,
                    weight = Modifier.weight(1f),
                    onClick = { onEdit(nama) },
                )
            }
        }
    }
}
