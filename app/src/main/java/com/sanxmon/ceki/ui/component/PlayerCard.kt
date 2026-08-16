package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.ui.theme.CekiColors

/**
 * Player scorecard mirroring `player-card.tsx`: tap to select, long-press to open
 * the actions sheet. Selected cards invert to the primary color.
 */
@Composable
fun PlayerCard(
    nama: String,
    skor: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)

    CekiPressable(
        onClick = onSelect,
        onLongClick = onLongPress,
        modifier = modifier
            .border(2.dp, if (isSelected) CekiColors.Primary else CekiColors.Surface0, shape)
            .background(if (isSelected) CekiColors.Primary else CekiColors.Surface0, shape)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            ) {
                Text(
                    text = "Pemain",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                    color = if (isSelected) CekiColors.Base.copy(alpha = 0.6f) else CekiColors.Subtext0,
                )
                Text(
                    text = nama,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.3).sp,
                    ),
                    color = if (isSelected) CekiColors.Base else CekiColors.Text,
                )
            }
            Text(
                text = "$skor",
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp,
                    fontFeatureSettings = "tnum",
                ),
                color = if (isSelected) CekiColors.Base else CekiColors.Text,
            )
        }
    }
}
