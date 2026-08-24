package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.QueueState
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LandingScreen(
    queueState: QueueState,
    onNavigate: (AppDestination) -> Unit,
    onSetRole: (UserRole) -> Unit,
    onOpenSimulation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val waitingCount = queueState.tickets.count { it.status == com.example.data.model.TicketStatus.WAITING }
    val servingCount = queueState.counters.count { it.status == com.example.data.model.CounterStatus.BUSY }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishCanvas)
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
    ) {
        // Hero Section with Gradient (Professional Polish Deep Indigo)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(PrimaryIndigoDeep, PrimaryIndigoDark, PrimaryIndigo)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = PolishOrangeHighlight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Next-Gen Queue Management",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App Title & Tagline
                Text(
                    text = "SmartQueue",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "\"Don't Wait in Line. Manage Your Time.\"",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PolishOrangeHighlight,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "An intelligent real-time queue & appointment management platform that eliminates waiting lines with automatic recalculation and smart multi-counter allocation.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PrimaryIndigo100,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Primary CTA Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigate(AppDestination.BOOK_APPOINTMENT) },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = PrimaryIndigo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("landing_book_appointment_button")
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Book Appointment & Join Line", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(AppDestination.VIRTUAL_QUEUE) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f), contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("landing_join_queue_button")
                        ) {
                            Icon(Icons.Default.DirectionsWalk, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Virtual Radar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                onSetRole(UserRole.ADMIN)
                                onNavigate(AppDestination.ADMIN_DASHBOARD)
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("landing_admin_login_button")
                        ) {
                            Icon(Icons.Default.SupervisorAccount, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Admin Console", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Live Metrics Pill Row
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Active Counters", fontSize = 11.sp, color = PrimaryIndigo100)
                            Text(text = "${queueState.counters.size} Live", fontWeight = FontWeight.Black, color = Color.White, fontSize = 14.sp)
                        }
                        Box(modifier = Modifier.size(1.dp, 24.dp).background(Color.White.copy(alpha = 0.2f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Currently Serving", fontSize = 11.sp, color = PrimaryIndigo100)
                            Text(text = "$servingCount Counters", fontWeight = FontWeight.Black, color = StatusSuccess, fontSize = 14.sp)
                        }
                        Box(modifier = Modifier.size(1.dp, 24.dp).background(Color.White.copy(alpha = 0.2f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "In Line", fontSize = 11.sp, color = PrimaryIndigo100)
                            Text(text = "$waitingCount Waiting", fontWeight = FontWeight.Black, color = PolishOrangeHighlight, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Demo Simulation Callout Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = PolishOrangeBg,
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishOrangeHighlight),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenSimulation() }
                    .testTag("landing_simulation_callout")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PolishOrangeText),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Simulate",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Interactive Queue Simulator",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = PolishTextPrimary
                            )
                        )
                        Text(
                            text = "Test instant completions, cancellations & smart counter allocations live!",
                            style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open",
                        tint = PolishOrangeText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Key Innovations & Core Features Header
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Key Innovations",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = PolishTextPrimary
                )
            )
            Text(
                text = "Algorithms that streamline customer throughput",
                style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Innovation 1: Automatic Queue Recalculation
            InnovationCard(
                title = "Automatic Queue Recalculation",
                description = "When a customer cancels, gets skipped, or a service completes early, all subsequent waiting times recalculate instantly without manual intervention.",
                icon = Icons.Default.Refresh,
                color = PrimaryIndigo
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Innovation 2: Smart Multi-Counter Allocation
            InnovationCard(
                title = "Smart Dynamic Counter Allocation",
                description = "Distributes waiting customers based on counter capability, service type compatibility, and real-time operator availability to balance the workload.",
                icon = Icons.Default.AutoAwesome,
                color = Color(0xFF0284C7)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Innovation 3: Virtual Queue Radar
            InnovationCard(
                title = "Live Line Position Radar",
                description = "Eliminates physical standing lines. Customers track their live queue position remotely and receive turn proximity notifications on their device.",
                icon = Icons.Default.DirectionsWalk,
                color = Color(0xFF059669)
            )
        }
    }
}

@Composable
fun InnovationCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PolishTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PolishTextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
