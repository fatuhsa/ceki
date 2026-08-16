package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.ui.theme.CekiColors

/**
 * Bottom sheet mirroring `player-actions-modal.tsx`: shows the tapped player and
 * offers GANTI NAMA / RESET SKOR.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerActionsSheet(
    playerName: String,
    playerScore: Int,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onReset: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = CekiColors.Mantle,
        scrimColor = CekiColors.Crust,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CekiColors.Surface2),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
        ) {
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                ) {
                    Text(
                        text = "Pemain",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                        ),
                        color = CekiColors.Subtext1,
                    )
                    Text(
                        text = playerName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                        ),
                        color = CekiColors.Text,
                    )
                }
                Text(
                    text = "$playerScore",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        fontFeatureSettings = "tnum",
                    ),
                    color = CekiColors.Primary,
                )
            }

            CekiPressable(
                onClick = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CekiColors.Surface0),
            ) {
                SheetActionRow(icon = { Icon(Icons.Filled.Edit, null, tint = CekiColors.Primary, modifier = Modifier.size(20.dp)) }) {
                    Text(
                        text = "GANTI NAMA",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp,
                        ),
                        color = CekiColors.Primary,
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            CekiPressable(
                onClick = onReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CekiColors.Red),
            ) {
                SheetActionRow(icon = { Icon(Icons.Filled.RestartAlt, null, tint = CekiColors.Base, modifier = Modifier.size(20.dp)) }) {
                    Text(
                        text = "RESET SKOR",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                        ),
                        color = CekiColors.Base,
                    )
                }
            }
        }
    }
}

@Composable
private fun SheetActionRow(
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(Modifier.width(10.dp))
        content()
    }
}
