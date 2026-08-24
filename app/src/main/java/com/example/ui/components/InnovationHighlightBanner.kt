package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InnovationEvent
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.StatusSuccess

@Composable
fun InnovationHighlightBanner(
    latestEvent: InnovationEvent?,
    onDismiss: () -> Unit = {},
    onViewDetails: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = latestEvent != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier.fillMaxWidth()
    ) {
        if (latestEvent != null) {
            val isRecalc = latestEvent is InnovationEvent.Recalculation
            val title = if (isRecalc) "CORE INNOVATION: Automatic Queue Recalculation" else "CORE INNOVATION: Smart Counter Allocation"
            val gradient = if (isRecalc) {
                Brush.horizontalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0C4A6E)))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFF0F172A), Color(0xFF064E3B), Color(0xFF1E293B)))
            }
            val accentColor = if (isRecalc) AccentCyan else StatusSuccess

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .shadow(8.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(gradient)
                    .border(1.5.dp, accentColor.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                    .clickable { onViewDetails() }
                    .padding(12.dp)
                    .testTag("innovation_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(accentColor.copy(alpha = 0.2f))
                            .border(1.dp, accentColor, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRecalc) Icons.Default.Refresh else Icons.Default.AutoAwesome,
                            contentDescription = "Innovation Icon",
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = accentColor,
                                letterSpacing = 0.3.sp
                            )
                        )

                        Spacer(modifier = Modifier.size(2.dp))

                        when (latestEvent) {
                            is InnovationEvent.Recalculation -> {
                                Text(
                                    text = "${latestEvent.reason} → Recomputed ${latestEvent.affectedCount} queue positions in real-time.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                )
                            }
                            is InnovationEvent.SmartAllocation -> {
                                Text(
                                    text = "Auto-assigned ${latestEvent.token} (${latestEvent.customerName}) to ${latestEvent.counterName} for ${latestEvent.serviceType.title}.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
