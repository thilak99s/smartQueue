package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Counter
import com.example.data.model.QueueTicket
import com.example.data.model.TicketStatus
import com.example.ui.theme.*

@Composable
fun VirtualQueueScreen(
    userTicket: QueueTicket?,
    allTickets: List<QueueTicket>,
    counters: List<Counter>,
    notifyWhenNearEnabled: Boolean,
    onToggleNotifyWhenNear: () -> Unit,
    onRecalculateQueue: () -> Unit,
    onCancelTicket: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val waitingTickets = allTickets.filter { it.status == TicketStatus.WAITING }
        .sortedWith(compareByDescending<QueueTicket> { it.isPriority }.thenBy { it.createdAt })
    val servingTickets = allTickets.filter { it.status == TicketStatus.SERVING }

    val isTurnApproaching = userTicket != null && (userTicket.peopleAhead <= 1 || userTicket.status == TicketStatus.SERVING)

    // Calculate queue progress: (Total - People Ahead) / Total
    val totalInLine = (waitingTickets.size + servingTickets.size).coerceAtLeast(1)
    val peopleAhead = userTicket?.peopleAhead ?: 0
    val targetProgress = if (userTicket == null) 0f else if (userTicket.status == TicketStatus.SERVING) 1.0f else (1.0f - (peopleAhead.toFloat() / totalInLine)).coerceIn(0.1f, 0.95f)
    val animatedProgress by animateFloatAsState(targetValue = targetProgress, label = "queueProgress")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishCanvas)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 36.dp)
    ) {
        // Proximity Warning Alert if turn is approaching or serving!
        AnimatedVisibility(visible = isTurnApproaching) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (userTicket?.status == TicketStatus.SERVING) Color(0xFF064E3B) else Color(0xFF78350F),
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (userTicket?.status == TicketStatus.SERVING) StatusSuccess else StatusWarning
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("turn_approaching_alert_banner")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (userTicket?.status == TicketStatus.SERVING) Icons.Default.CheckCircle else Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (userTicket?.status == TicketStatus.SERVING)
                                "YOUR TURN HAS ARRIVED!"
                            else
                                "YOUR TURN IS APPROACHING!",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (userTicket?.status == TicketStatus.SERVING)
                                "Please proceed immediately to Counter ${userTicket.assignedCounterId ?: 1}."
                            else
                                "You are next in line (${userTicket?.peopleAhead ?: 0} ahead). Please approach Counter ${userTicket?.assignedCounterId ?: 2}.",
                            color = Color(0xFFF1F5F9),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Virtual Radar Main Card (Professional Polish Dark Indigo Gradient)
        Surface(
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("virtual_queue_radar_card")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(PrimaryIndigo, PrimaryIndigoDark, PrimaryIndigoDeep)
                        )
                    )
                    .padding(22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Top Indicator: Remote Queue Tag + Recalculate Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(StatusSuccess))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "VIRTUAL RADAR ACTIVE",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Surface(
                            onClick = onRecalculateQueue,
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.testTag("virtual_queue_recalculate_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Recalculate", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // User Ticket & Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Active Token", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text(
                                text = userTicket?.token ?: "A108",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = (-1).sp
                                )
                            )
                            Text(
                                text = userTicket?.customerName ?: "Sneha Rao",
                                color = PrimaryIndigo100,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Status", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (userTicket?.status == TicketStatus.SERVING) StatusSuccess else Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = userTicket?.status?.name ?: "WAITING",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = userTicket?.serviceType?.title ?: "General Service",
                                color = PrimaryIndigo100,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress Bar with Percentage
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Queue Progression",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${(animatedProgress * 100).toInt()}% towards counter",
                                color = PolishOrangeHighlight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PolishOrangeHighlight,
                            trackColor = Color.White.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3-Metric Summary
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "People Ahead", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text(
                                    text = "${userTicket?.peopleAhead ?: 0}",
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                            Box(modifier = Modifier.size(1.dp, 28.dp).background(Color.White.copy(alpha = 0.2f)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Est. Wait Time", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text(
                                    text = "${userTicket?.estimatedWaitMinutes ?: 12} min",
                                    fontWeight = FontWeight.Black,
                                    color = PolishOrangeHighlight,
                                    fontSize = 16.sp
                                )
                            }
                            Box(modifier = Modifier.size(1.dp, 28.dp).background(Color.White.copy(alpha = 0.2f)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Assigned Counter", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text(
                                    text = if (userTicket?.assignedCounterId != null) "Counter ${userTicket.assignedCounterId}" else "Auto-Assign",
                                    fontWeight = FontWeight.Black,
                                    color = StatusSuccess,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remote flexibility card (Professional Polish)
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = PrimaryIndigo50,
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo100),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsWalk,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "You are free to grab a coffee or step outside. Your spot in the line is locked and recalculates in real-time.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PrimaryIndigo900,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "Notify Me When My Turn Is Near" Setting Card (Professional Polish)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("notify_when_near_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryIndigo50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Notify When Turn Is Near",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PolishTextPrimary
                            )
                        )
                        Text(
                            text = "Alerts sound when 2 people ahead",
                            style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
                        )
                    }
                }

                Switch(
                    checked = notifyWhenNearEnabled,
                    onCheckedChange = { onToggleNotifyWhenNear() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = PrimaryIndigo
                    ),
                    modifier = Modifier.testTag("notify_when_near_toggle")
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live Queue Progression Horizon
        Text(
            text = "Live Queue Flow",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                color = PolishTextPrimary
            )
        )
        Text(
            text = "Visual token progression across active service counters",
            style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Serving items first
            itemsIndexed(servingTickets) { index, ticket ->
                QueueHorizonChip(ticket = ticket, isServing = true)
            }

            // Waiting items
            itemsIndexed(waitingTickets) { index, ticket ->
                QueueHorizonChip(ticket = ticket, isServing = false)
            }
        }
    }
}

@Composable
fun QueueHorizonChip(
    ticket: QueueTicket,
    isServing: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (ticket.isCurrentUser) PrimaryIndigo50
        else if (isServing) Color(0xFFECFDF5)
        else Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (ticket.isCurrentUser) PrimaryIndigo
            else if (isServing) Color(0xFFA7F3D0)
            else PolishCardBorder
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = ticket.token,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = if (ticket.isCurrentUser) PrimaryIndigo else if (isServing) Color(0xFF065F46) else PolishTextPrimary
            )
            Text(
                text = if (isServing) "Serving" else "Pos #${ticket.positionInQueue}",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (ticket.isCurrentUser) PrimaryIndigoDark else PolishTextSecondary
            )
            if (ticket.isCurrentUser) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = PrimaryIndigo,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "YOU",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}
