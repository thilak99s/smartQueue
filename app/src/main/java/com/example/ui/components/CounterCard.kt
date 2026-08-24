package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Counter
import com.example.data.model.CounterStatus
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CounterCard(
    counter: Counter,
    isAdminMode: Boolean = false,
    onCompleteService: () -> Unit = {},
    onCallNext: () -> Unit = {},
    onToggleStatus: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val statusColor by animateColorAsState(
        when (counter.status) {
            CounterStatus.AVAILABLE -> StatusSuccess
            CounterStatus.BUSY -> PolishOrangeText
            CounterStatus.OFFLINE -> Slate400
        },
        label = "statusColor"
    )

    val statusText = when (counter.status) {
        CounterStatus.AVAILABLE -> "Available"
        CounterStatus.BUSY -> "Serving: ${counter.currentTicketToken ?: "Busy"}"
        CounterStatus.OFFLINE -> "Offline"
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("counter_card_${counter.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Counter Name + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryIndigo50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DesktopWindows,
                            contentDescription = counter.name,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = counter.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PolishTextPrimary
                            )
                        )
                        Text(
                            text = "Served: ${counter.totalServed} customers",
                            style = MaterialTheme.typography.labelSmall.copy(color = PolishTextSecondary)
                        )
                    }
                }

                // Status Chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (counter.status) {
                        CounterStatus.AVAILABLE -> Color(0xFFECFDF5)
                        CounterStatus.BUSY -> PolishOrangeBg
                        CounterStatus.OFFLINE -> Slate100
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        when (counter.status) {
                            CounterStatus.AVAILABLE -> Color(0xFFA7F3D0)
                            CounterStatus.BUSY -> PolishOrangeHighlight
                            CounterStatus.OFFLINE -> Slate200
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Current Service Panel if Busy
            if (counter.status == CounterStatus.BUSY && counter.currentTicketToken != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = PrimaryIndigo50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo100),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Currently Serving:", fontSize = 11.sp, color = PrimaryIndigoDark)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = counter.currentTicketToken ?: "",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = PrimaryIndigo900
                            )
                        }
                        Text(
                            text = "Avg: ${counter.currentService?.avgDurationMinutes ?: 8} min",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = PrimaryIndigo
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Supported Services Tags
            Text(
                text = "Specialties",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = PolishTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                counter.supportedServices.forEach { service ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PolishCanvas,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder)
                    ) {
                        Text(
                            text = service.title,
                            fontSize = 10.sp,
                            color = PolishTextSecondary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Admin Controls
            if (isAdminMode) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (counter.status == CounterStatus.BUSY) {
                        Button(
                            onClick = onCompleteService,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f).testTag("counter_complete_button_${counter.id}")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Done", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (counter.status == CounterStatus.AVAILABLE) {
                        Button(
                            onClick = onCallNext,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f).testTag("counter_call_next_button_${counter.id}")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Call Next", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = onToggleStatus,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("counter_toggle_status_button_${counter.id}")
                    ) {
                        Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = if (counter.status == CounterStatus.OFFLINE) "Go Online" else "Offline",
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
