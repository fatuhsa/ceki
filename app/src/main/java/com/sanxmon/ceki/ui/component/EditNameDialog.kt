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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.ui.theme.CekiColors

/**
 * Rename dialog mirroring `edit-modal.tsx`: auto-focused input, inline error box,
 * SIMPAN / BATAL actions.
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CekiColors.Crust),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .border(2.dp, CekiColors.Primary, RoundedCornerShape(20.dp))
                .background(CekiColors.Mantle, RoundedCornerShape(20.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = "GANTI NAMA",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                ),
                color = CekiColors.Primary,
            )

            if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CekiColors.Red)
                        .padding(12.dp),
                ) {
                    Text(
                        text = error,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                        ),
                        color = CekiColors.Crust,
                    )
                }
            }

            BasicTextField(
                value = nama,
                onValueChange = onNamaChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CekiColors.Surface0)
                    .border(1.dp, CekiColors.Surface1, RoundedCornerShape(14.dp))
                    .focusRequester(focusRequester)
                    .padding(horizontal = 16.dp),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CekiColors.Text,
                ),
                cursorBrush = SolidColor(CekiColors.Primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onEdit(nama) }),
                decorationBox = { innerTextField ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                        if (nama.isEmpty()) {
                            Text(
                                text = "Masukkan nama...",
                                fontSize = 18.sp,
                                color = CekiColors.Subtext0,
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
                    textColor = CekiColors.Text,
                    background = CekiColors.Surface0,
                    weight = Modifier.weight(1f),
                    onClick = onClose,
                )
                DialogButton(
                    text = "SIMPAN",
                    textColor = CekiColors.Base,
                    background = CekiColors.Primary,
                    weight = Modifier.weight(1f),
                    onClick = { onEdit(nama) },
                )
            }
        }
    }
}
