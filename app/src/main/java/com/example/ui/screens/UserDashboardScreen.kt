package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Counter
import com.example.data.model.CounterStatus
import com.example.data.model.QueueTicket
import com.example.data.model.TicketStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserDashboardScreen(
    currentUserTicket: QueueTicket?,
    counters: List<Counter>,
    currentlyServing: List<QueueTicket>,
    onNavigate: (AppDestination) -> Unit,
    onCancelTicket: (String) -> Unit,
    onRefreshQueue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishCanvas)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 36.dp)
    ) {
        // Welcome Header & Refresh Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome Back, ${currentUserTicket?.customerName?.substringBefore(" ") ?: "Customer"}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = PolishTextPrimary,
                        letterSpacing = (-0.3).sp
                    )
                )
                Text(
                    text = "Live Queue & Counter Dashboard",
                    style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
                )
            }

            Surface(
                onClick = onRefreshQueue,
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                modifier = Modifier.testTag("user_refresh_queue_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sync",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryIndigo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Ticket Section (Professional Polish Design)
        if (currentUserTicket != null && currentUserTicket.status != TicketStatus.CANCELLED) {
            HeroTicketCard(
                ticket = currentUserTicket,
                onViewVirtualQueue = { onNavigate(AppDestination.VIRTUAL_QUEUE) },
                onCancel = { onCancelTicket(currentUserTicket.id) }
            )
        } else {
            // No Active Ticket Card
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("no_ticket_card")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PrimaryIndigo50)
                            .border(1.dp, PrimaryIndigo100, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "You're Not In Line Yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = PolishTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Book an appointment or join the live virtual queue to get your dynamic token number and real-time wait estimation.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = PolishTextSecondary,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onNavigate(AppDestination.BOOK_APPOINTMENT) },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("book_ticket_cta_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book Appointment & Join Line", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Dynamic Notification Banner (Professional Polish style)
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
                        .background(PrimaryIndigo)
                        .shadow(2.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (currentUserTicket?.status == TicketStatus.SERVING)
                            "Ready! Proceed to Counter ${currentUserTicket.assignedCounterId ?: 1}"
                        else if (currentUserTicket != null && currentUserTicket.peopleAhead <= 1)
                            "Turn approaching! Counter ${currentUserTicket.assignedCounterId ?: 2} is preparing."
                        else
                            "Smart Allocation Active: Real-time dynamic queue balancing.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo900,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "Recalculates automatically upon counter availability.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = PrimaryIndigoDark,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Live Queue Progression Flow Horizon (Professional Polish)
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Live Queue Progress",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PolishTextPrimary
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PrimaryIndigo50
                    ) {
                        Text(
                            text = "AUTO-RECALCULATING",
                            color = PrimaryIndigo,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progression Flow Pillars (Served, Now Serving, Skipped, Next, You)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Pillar 1: A101 (Served)
                    QueuePillarItem(
                        token = "A101",
                        label = "Served",
                        heightDp = 50,
                        bgColor = Slate100,
                        borderColor = Slate200,
                        textColor = Slate400
                    )

                    // Pillar 2: A102 (Now Serving)
                    QueuePillarItem(
                        token = "A102",
                        label = "Serving",
                        heightDp = 76,
                        bgColor = PrimaryIndigo50,
                        borderColor = PrimaryIndigo200,
                        textColor = PrimaryIndigo,
                        isHighlight = true
                    )

                    // Pillar 3: A103 (Skipped / Recalculated)
                    QueuePillarItem(
                        token = "A103",
                        label = "Skipped",
                        heightDp = 60,
                        bgColor = Slate50,
                        borderColor = Slate300,
                        textColor = Slate400,
                        isStrikethrough = true
                    )

                    // Pillar 4: A104 (Next)
                    QueuePillarItem(
                        token = "A104",
                        label = "Next",
                        heightDp = 56,
                        bgColor = Slate50,
                        borderColor = Slate200,
                        textColor = Slate600
                    )

                    // Pillar 5: User Token (You)
                    QueuePillarItem(
                        token = currentUserTicket?.token ?: "A105",
                        label = "You",
                        heightDp = 68,
                        bgColor = PrimaryIndigo100,
                        borderColor = PrimaryIndigo,
                        textColor = PrimaryIndigo900,
                        isYou = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Action Cards (2 Columns - Professional Polish Grid)
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                color = PolishTextPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Action Card 1: Book New Appointment
            Surface(
                onClick = { onNavigate(AppDestination.BOOK_APPOINTMENT) },
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
                modifier = Modifier
                    .weight(1f)
                    .testTag("user_book_button")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PolishOrangeBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = PolishOrangeText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Book Token",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PolishTextPrimary
                    )
                    Text(
                        text = "Join virtual line",
                        fontSize = 11.sp,
                        color = PolishTextSecondary
                    )
                }
            }

            // Action Card 2: Virtual Queue Radar
            Surface(
                onClick = { onNavigate(AppDestination.VIRTUAL_QUEUE) },
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
                modifier = Modifier
                    .weight(1f)
                    .testTag("user_view_queue_button")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryIndigo50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Queue Radar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PolishTextPrimary
                    )
                    Text(
                        text = "Live line position",
                        fontSize = 11.sp,
                        color = PolishTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Live Serving Counters Board
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Service Counter Status",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = PolishTextPrimary
                )
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFECFDF5),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(StatusSuccess)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LIVE ALLOCATION",
                        color = Color(0xFF065F46),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            counters.forEach { counter ->
                LiveCounterPill(counter = counter)
            }
        }
    }
}

@Composable
fun HeroTicketCard(
    ticket: QueueTicket,
    onViewVirtualQueue: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_user_token_card")
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
                // Header: Active Pill + Counter Badge
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
                        Text(
                            text = if (ticket.status == TicketStatus.SERVING) "NOW SERVING" else "ACTIVE APPOINTMENT",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (ticket.assignedCounterId != null) "Counter ${ticket.assignedCounterId}" else "Auto-Assign",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Main Token Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ticket Number",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = ticket.token,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 46.sp,
                                letterSpacing = (-1).sp
                            )
                        )
                        Text(
                            text = ticket.serviceType.title,
                            color = PrimaryIndigo100,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Pos", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(
                                text = if (ticket.status == TicketStatus.SERVING) "NOW" else "#${ticket.positionInQueue}",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Divider line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.2f))
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Row: People Ahead & Est Wait Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "People Ahead",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${ticket.peopleAhead} people",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Est. Wait Time",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${ticket.estimatedWaitMinutes} mins",
                            color = PolishOrangeHighlight,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions: Live Radar + Leave Line
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onViewVirtualQueue,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = PrimaryIndigo
                        ),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(44.dp)
                            .testTag("dashboard_view_virtual_queue_button")
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Queue Radar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFCA5A5)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("dashboard_cancel_ticket_button")
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Leave", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun QueuePillarItem(
    token: String,
    label: String,
    heightDp: Int,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    isHighlight: Boolean = false,
    isStrikethrough: Boolean = false,
    isYou: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(heightDp.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .border(
                    width = if (isYou || isHighlight) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = token,
                fontWeight = if (isHighlight || isYou) FontWeight.Black else FontWeight.Bold,
                fontSize = 11.sp,
                color = textColor,
                textDecoration = if (isStrikethrough) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isYou) FontWeight.Black else FontWeight.Medium,
            color = if (isYou) PrimaryIndigo else PolishTextSecondary
        )
    }
}

@Composable
fun LiveCounterPill(
    counter: Counter,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PrimaryIndigo50),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DesktopWindows,
                        contentDescription = null,
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = counter.name,
                        fontWeight = FontWeight.Bold,
                        color = PolishTextPrimary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = counter.supportedServices.joinToString(", ") { it.title.substringBefore(" ") },
                        color = PolishTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            if (counter.status == CounterStatus.BUSY && counter.currentTicketToken != null) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PolishOrangeBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PolishOrangeHighlight)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Serving: ${counter.currentTicketToken}",
                            color = PolishOrangeText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
                ) {
                    Text(
                        text = "Available",
                        color = StatusSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
