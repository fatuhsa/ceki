package com.sanxmon.ceki.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.ui.CekiViewModel.ConfirmState
import com.sanxmon.ceki.ui.theme.CekiColors

/**
 * Centered confirm dialog mirroring `confirm-modal.tsx`: warning icon, red border,
 * BATAL / YA, LANJUT actions.
 */
@Composable
fun ConfirmDialog(
    confirm: ConfirmState,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)

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
                .border(2.dp, CekiColors.Red, RoundedCornerShape(24.dp))
                .background(CekiColors.Mantle, RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(CekiColors.Red),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = CekiColors.Base,
                    modifier = Modifier.size(32.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = confirm.title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                ),
                textAlign = TextAlign.Center,
                color = CekiColors.Text,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = confirm.message,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = CekiColors.Subtext0,
            )
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DialogButton(
                    text = "BATAL",
                    textColor = CekiColors.Text,
                    background = CekiColors.Surface0,
                    weight = Modifier.weight(1f),
                    onClick = onClose,
                )
                DialogButton(
                    text = "YA, LANJUT",
                    textColor = CekiColors.Base,
                    background = CekiColors.Red,
                    weight = Modifier.weight(1f),
                    onClick = {
                        confirm.onConfirm()
                        onClose()
                    },
                )
            }
        }
    }
}

@Composable
internal fun DialogButton(
    text: String,
    textColor: androidx.compose.ui.graphics.Color,
    background: androidx.compose.ui.graphics.Color,
    weight: Modifier,
    onClick: () -> Unit,
) {
    CekiPressable(
        onClick = onClick,
        modifier = weight
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(background),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = if (textColor == CekiColors.Base) FontWeight.Black else FontWeight.Bold,
                color = textColor,
            )
        }
    }
}
