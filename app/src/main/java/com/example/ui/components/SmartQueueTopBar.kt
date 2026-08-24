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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartQueueTopBar(
    currentDestination: AppDestination,
    currentRole: UserRole,
    unreadNotificationCount: Int,
    onRoleToggle: () -> Unit,
    onNotificationClick: () -> Unit,
    onSimulationClick: () -> Unit,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigateHome() }
                    .testTag("topbar_brand_logo")
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(PrimaryIndigo, PrimaryIndigoDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SQ",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SmartQueue",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = PolishTextPrimary,
                                letterSpacing = (-0.3).sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess)
                        )
                    }
                    Text(
                        text = "Real-Time Allocation",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = PolishTextSecondary,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // Right Action Elements: Quick Sim Button, Role Pill, Notification Bell
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Interactive Simulation Lab Button
                Surface(
                    onClick = onSimulationClick,
                    shape = RoundedCornerShape(12.dp),
                    color = PolishOrangeBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PolishOrangeHighlight),
                    modifier = Modifier.testTag("topbar_simulation_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Simulate Queue",
                            tint = PolishOrangeText,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Sim Lab",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishOrangeText
                        )
                    }
                }

                // Role Switcher Pill
                Surface(
                    onClick = onRoleToggle,
                    shape = RoundedCornerShape(20.dp),
                    color = if (currentRole == UserRole.ADMIN) PrimaryIndigo50 else Slate100,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (currentRole == UserRole.ADMIN) PrimaryIndigo200 else Slate200
                    ),
                    modifier = Modifier.testTag("topbar_role_toggle_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (currentRole == UserRole.ADMIN) Icons.Default.SupervisorAccount else Icons.Default.Person,
                            contentDescription = "Switch Role",
                            tint = if (currentRole == UserRole.ADMIN) PrimaryIndigo else Slate700,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentRole == UserRole.ADMIN) "Admin" else "User",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentRole == UserRole.ADMIN) PrimaryIndigo else Slate700
                        )
                    }
                }

                // Notifications Bell
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(
                                containerColor = StatusError,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = if (unreadNotificationCount > 9) "9+" else "$unreadNotificationCount",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("topbar_notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Slate700,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
