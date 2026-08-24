package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QueueTicket
import com.example.data.model.TicketStatus
import com.example.ui.theme.*

@Composable
fun TokenTicketCard(
    ticket: QueueTicket,
    isAdminView: Boolean = false,
    onCancel: () -> Unit = {},
    onSkip: () -> Unit = {},
    onAssignCounter: (Int) -> Unit = {},
    onCardClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (statusColor, statusBg, statusLabel) = when (ticket.status) {
        TicketStatus.SERVING -> Triple(StatusSuccess, Color(0xFFECFDF5), "Now Serving")
        TicketStatus.WAITING -> Triple(PrimaryIndigo, PrimaryIndigo50, "Waiting (Pos #${ticket.positionInQueue})")
        TicketStatus.COMPLETED -> Triple(Color(0xFF059669), Color(0xFFF0FDF4), "Completed")
        TicketStatus.CANCELLED -> Triple(StatusError, Color(0xFFFEF2F2), "Cancelled")
        TicketStatus.SKIPPED -> Triple(PolishOrangeText, PolishOrangeBg, "Skipped")
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = if (ticket.isCurrentUser) 4.dp else 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = if (ticket.isCurrentUser) 1.5.dp else 1.dp,
            color = if (ticket.isCurrentUser) PrimaryIndigo else PolishCardBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("ticket_card_${ticket.token}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // User highlight badge if it's the current user's ticket
            if (ticket.isCurrentUser) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryIndigo50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo200),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "YOUR ACTIVE APPOINTMENT",
                        color = PrimaryIndigo,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Top Row: Token Badge + Customer Info + Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(PrimaryIndigo, PrimaryIndigoDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ticket.token,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = ticket.customerName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PolishTextPrimary,
                                    fontSize = 15.sp
                                )
                            )
                            if (ticket.isPriority) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = StatusPriority.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = "Priority",
                                            tint = StatusPriority,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "VIP",
                                            color = StatusPriority,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            text = ticket.serviceType.title,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PolishTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = statusBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Middle Stats Row: People Ahead, Est Wait, Counter
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PolishCanvas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (ticket.status == TicketStatus.SERVING) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.DesktopWindows,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "At Counter ${ticket.assignedCounterId ?: 1}",
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = "Serving Now",
                            color = PolishTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else if (ticket.status == TicketStatus.WAITING) {
                        Column {
                            Text(text = "People Ahead", fontSize = 10.sp, color = PolishTextSecondary)
                            Text(
                                text = "${ticket.peopleAhead} ahead",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = PolishTextPrimary
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Est. Wait", fontSize = 10.sp, color = PolishTextSecondary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = PrimaryIndigo,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${ticket.estimatedWaitMinutes} min",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrimaryIndigo
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Counter", fontSize = 10.sp, color = PolishTextSecondary)
                            Text(
                                text = if (ticket.assignedCounterId != null) "Counter ${ticket.assignedCounterId}" else "Auto-Assign",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = PolishTextPrimary
                            )
                        }
                    } else {
                        Text(
                            text = "Status: ${ticket.status.name}",
                            color = PolishTextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = ticket.appointmentTime,
                            color = PolishTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Reason if Recalculated
            ticket.lastRecalculationReason?.let { reason ->
                if (ticket.status == TicketStatus.WAITING) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "⚡ Recalculated: $reason",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = PrimaryIndigo,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // Admin Action Buttons (Cancel, Skip, Call)
            if (isAdminView && (ticket.status == TicketStatus.WAITING || ticket.status == TicketStatus.SERVING)) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (ticket.status == TicketStatus.WAITING) {
                        OutlinedButton(
                            onClick = onSkip,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.weight(1f).testTag("skip_ticket_${ticket.token}")
                        ) {
                            Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Skip", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onCancel,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusError),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusError.copy(alpha = 0.4f)),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.weight(1f).testTag("cancel_ticket_${ticket.token}")
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Cancel", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
