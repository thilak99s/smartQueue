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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Counter
import com.example.data.model.QueueTicket
import com.example.data.model.ServiceType
import com.example.data.model.TicketStatus
import com.example.data.repository.QueueState
import com.example.ui.components.CounterCard
import com.example.ui.components.TokenTicketCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.FilterState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDashboardScreen(
    queueState: QueueState,
    filteredTickets: List<QueueTicket>,
    filterState: FilterState,
    onSearchChange: (String) -> Unit,
    onServiceFilterChange: (ServiceType?) -> Unit,
    onStatusFilterChange: (TicketStatus?) -> Unit,
    onCompleteService: (Int) -> Unit,
    onCallNext: (Int) -> Unit,
    onToggleCounterStatus: (Int) -> Unit,
    onCancelTicket: (String) -> Unit,
    onSkipTicket: (String) -> Unit,
    onAssignCounter: (String, Int) -> Unit,
    onRecalculateNow: () -> Unit,
    onOpenSimulation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live Queue, 1: Counters Station

    val waitingCount = queueState.tickets.count { it.status == TicketStatus.WAITING }
    val servingCount = queueState.tickets.count { it.status == TicketStatus.SERVING }
    val activeCountersCount = queueState.counters.count { it.status != com.example.data.model.CounterStatus.OFFLINE }
    val avgWaitMinutes = if (waitingCount > 0) {
        queueState.tickets.filter { it.status == TicketStatus.WAITING }.map { it.estimatedWaitMinutes }.average().toInt()
    } else 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishCanvas)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Admin Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Admin Control Console",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = PolishTextPrimary,
                        letterSpacing = (-0.3).sp
                    )
                )
                Text(
                    text = "Live Queue & Counter Operations Center",
                    style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onRecalculateNow,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("admin_recalculate_queue_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Recalculate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenSimulation,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PolishOrangeText),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PolishOrangeHighlight),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("admin_sim_lab_button")
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 6 Summary Metrics Grid
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AdminMetricCard(
                title = "Total Active",
                value = "${queueState.tickets.size}",
                icon = Icons.Default.People,
                color = PrimaryIndigo,
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = "Waiting",
                value = "$waitingCount",
                icon = Icons.Default.AccessTime,
                color = PolishOrangeText,
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = "Serving",
                value = "$servingCount",
                icon = Icons.Default.DesktopWindows,
                color = StatusSuccess,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AdminMetricCard(
                title = "Active Counters",
                value = "$activeCountersCount / 4",
                icon = Icons.Default.DesktopWindows,
                color = Color(0xFF0284C7),
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = "Completed",
                value = "${queueState.totalServedCount}",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF059669),
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = "Avg Wait",
                value = "$avgWaitMinutes min",
                icon = Icons.Default.AccessTime,
                color = StatusPriority,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Navigation: Live Queue vs Counters Station
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Surface(
                    onClick = { selectedTab = 0 },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedTab == 0) PrimaryIndigo else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_tab_live_queue")
                ) {
                    Text(
                        text = "Live Queue (${queueState.tickets.count { it.status == TicketStatus.WAITING || it.status == TicketStatus.SERVING }})",
                        color = if (selectedTab == 0) Color.White else PolishTextSecondary,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                Surface(
                    onClick = { selectedTab = 1 },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedTab == 1) PrimaryIndigo else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_tab_counters_station")
                ) {
                    Text(
                        text = "Counters Station (${queueState.counters.size})",
                        color = if (selectedTab == 1) Color.White else PolishTextSecondary,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            // Search and Filters for Queue Table
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = filterState.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search token or customer name...", fontSize = 12.sp, color = PolishTextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PolishTextSecondary, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (filterState.searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedBorderColor = PolishCardBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_search_input")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Service filter chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChipPill(
                    label = "All Services",
                    isSelected = filterState.serviceFilter == null,
                    onClick = { onServiceFilterChange(null) }
                )
                ServiceType.entries.forEach { service ->
                    FilterChipPill(
                        label = service.prefix,
                        isSelected = filterState.serviceFilter == service,
                        onClick = { onServiceFilterChange(if (filterState.serviceFilter == service) null else service) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Queue List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTickets, key = { it.id }) { ticket ->
                    TokenTicketCard(
                        ticket = ticket,
                        isAdminView = true,
                        onCancel = { onCancelTicket(ticket.id) },
                        onSkip = { onSkipTicket(ticket.id) },
                        onAssignCounter = { counterId -> onAssignCounter(ticket.id, counterId) }
                    )
                }
            }
        } else {
            // Counters Station Grid
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(queueState.counters, key = { it.id }) { counter ->
                    CounterCard(
                        counter = counter,
                        isAdminMode = true,
                        onCompleteService = { onCompleteService(counter.id) },
                        onCallNext = { onCallNext(counter.id) },
                        onToggleStatus = { onToggleCounterStatus(counter.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 10.sp, color = PolishTextSecondary)
                Text(text = value, fontWeight = FontWeight.Black, fontSize = 13.sp, color = PolishTextPrimary)
            }
        }
    }
}

@Composable
fun FilterChipPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) PrimaryIndigo else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) PrimaryIndigo else PolishCardBorder
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else PolishTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
