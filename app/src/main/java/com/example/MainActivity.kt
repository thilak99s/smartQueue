package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QueueTicket
import com.example.ui.components.InnovationHighlightBanner
import com.example.ui.components.NotificationDialog
import com.example.ui.components.SimulationControlSheet
import com.example.ui.components.SmartQueueBottomBar
import com.example.ui.components.SmartQueueTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookAppointmentScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.UserDashboardScreen
import com.example.ui.screens.VirtualQueueScreen
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.SmartQueueTheme
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.QueueViewModel
import com.example.ui.viewmodel.UserRole
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val queueViewModel: QueueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartQueueTheme {
                SmartQueueApp(viewModel = queueViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartQueueApp(
    viewModel: QueueViewModel,
    modifier: Modifier = Modifier
) {
    val queueState by viewModel.queueState.collectAsState()
    val currentDestination by viewModel.currentDestination.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val unreadNotifCount by viewModel.unreadNotificationCount.collectAsState()
    val latestEvent by viewModel.latestInnovationEvent.collectAsState()
    val isSimulationOpen by viewModel.isSimulationSheetOpen.collectAsState()
    val isNotifDialogOpen by viewModel.isNotificationDialogOpen.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val filteredTickets by viewModel.filteredTickets.collectAsState()
    val currentUserTicket by viewModel.currentUserTicket.collectAsState()
    val currentlyServing by viewModel.currentlyServingTickets.collectAsState()
    val notifyWhenNear by viewModel.notifyWhenNearEnabled.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Dialog state for newly booked ticket confirmation
    var newlyBookedTicket by remember { mutableStateOf<QueueTicket?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("smart_queue_main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SmartQueueTopBar(
                currentDestination = currentDestination,
                currentRole = currentRole,
                unreadNotificationCount = unreadNotifCount,
                onRoleToggle = { viewModel.toggleRole() },
                onNotificationClick = { viewModel.setNotificationDialogOpen(true) },
                onSimulationClick = { viewModel.setSimulationSheetOpen(true) },
                onNavigateHome = { viewModel.navigate(AppDestination.LANDING) }
            )
        },
        bottomBar = {
            SmartQueueBottomBar(
                currentDestination = currentDestination,
                currentRole = currentRole,
                onNavigate = { viewModel.navigate(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Real-time Innovation Event Banner (Shows automatic recalculation & smart allocation live!)
            InnovationHighlightBanner(
                latestEvent = latestEvent,
                onDismiss = { viewModel.clearLatestEvent() },
                onViewDetails = { viewModel.setNotificationDialogOpen(true) }
            )

            // Screen Switching with Transitions
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = {
                        (slideInHorizontally { width -> width / 3 } + fadeIn()) togetherWith
                                (slideOutHorizontally { width -> -width / 3 } + fadeOut())
                    },
                    label = "screen_transition"
                ) { destination ->
                    when (destination) {
                        AppDestination.LANDING -> {
                            LandingScreen(
                                queueState = queueState,
                                onNavigate = { viewModel.navigate(it) },
                                onSetRole = { viewModel.setRole(it) },
                                onOpenSimulation = { viewModel.setSimulationSheetOpen(true) }
                            )
                        }

                        AppDestination.USER_DASHBOARD -> {
                            UserDashboardScreen(
                                currentUserTicket = currentUserTicket,
                                counters = queueState.counters,
                                currentlyServing = currentlyServing,
                                onNavigate = { viewModel.navigate(it) },
                                onCancelTicket = { ticketId -> viewModel.cancelTicket(ticketId) },
                                onRefreshQueue = { viewModel.recalculateQueueManual() }
                            )
                        }

                        AppDestination.VIRTUAL_QUEUE -> {
                            VirtualQueueScreen(
                                userTicket = currentUserTicket,
                                allTickets = queueState.tickets,
                                counters = queueState.counters,
                                notifyWhenNearEnabled = notifyWhenNear,
                                onToggleNotifyWhenNear = { viewModel.toggleNotifyWhenNear() },
                                onRecalculateQueue = { viewModel.recalculateQueueManual() },
                                onCancelTicket = { ticketId -> viewModel.cancelTicket(ticketId) }
                            )
                        }

                        AppDestination.BOOK_APPOINTMENT -> {
                            BookAppointmentScreen(
                                onBookConfirmed = { name, phone, email, service, date, slot, preferredCounter, isPriority ->
                                    val booked = viewModel.bookAppointment(
                                        name = name,
                                        phone = phone,
                                        email = email,
                                        serviceType = service,
                                        appointmentDate = date,
                                        appointmentTime = slot,
                                        preferredCounterId = preferredCounter,
                                        isPriority = isPriority
                                    )
                                    newlyBookedTicket = booked
                                }
                            )
                        }

                        AppDestination.ADMIN_DASHBOARD -> {
                            AdminDashboardScreen(
                                queueState = queueState,
                                filteredTickets = filteredTickets,
                                filterState = filterState,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                onServiceFilterChange = { viewModel.updateServiceFilter(it) },
                                onStatusFilterChange = { viewModel.updateStatusFilter(it) },
                                onCompleteService = { counterId -> viewModel.completeService(counterId) },
                                onCallNext = { counterId -> viewModel.callNextCustomer(counterId) },
                                onToggleCounterStatus = { counterId -> viewModel.toggleCounterStatus(counterId) },
                                onCancelTicket = { ticketId -> viewModel.cancelTicket(ticketId) },
                                onSkipTicket = { ticketId -> viewModel.skipTicket(ticketId) },
                                onAssignCounter = { ticketId, counterId -> viewModel.assignCounterManual(ticketId, counterId) },
                                onRecalculateNow = { viewModel.recalculateQueueManual() },
                                onOpenSimulation = { viewModel.setSimulationSheetOpen(true) }
                            )
                        }

                        AppDestination.ANALYTICS -> {
                            AnalyticsScreen(queueState = queueState)
                        }

                        AppDestination.AUTH -> {
                            AuthScreen(
                                currentRole = currentRole,
                                onLoginSuccess = { role, email ->
                                    viewModel.setRole(role)
                                    if (role == UserRole.ADMIN) {
                                        viewModel.navigate(AppDestination.ADMIN_DASHBOARD)
                                    } else {
                                        viewModel.navigate(AppDestination.USER_DASHBOARD)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Simulation Control Sheet
    if (isSimulationOpen) {
        SimulationControlSheet(
            sheetState = sheetState,
            onDismiss = { viewModel.setSimulationSheetOpen(false) },
            onSimulateCompletion = { viewModel.simulateCustomerCompletion() },
            onSimulateCancellation = { viewModel.simulateMiddleCancellation() },
            onSimulateDelay = { viewModel.simulateDelayAtCounter(1, 5) },
            onSimulateAddCustomer = { isVip -> viewModel.simulateAddCustomer(isVip) },
            onSimulateMakeCounterAvailable = { viewModel.simulateMakeCounterAvailable(3) },
            onRecalculateNow = { viewModel.recalculateQueueManual() },
            onResetData = { viewModel.resetSampleData() }
        )
    }

    // Notification Center Dialog
    if (isNotifDialogOpen) {
        NotificationDialog(
            notifications = queueState.notifications,
            onDismiss = { viewModel.setNotificationDialogOpen(false) },
            onClearAll = { viewModel.clearAllNotifications() },
            onNotificationClick = { notifId -> viewModel.markNotificationAsRead(notifId) }
        )
    }

    // Appointment Generated Confirmation Dialog
    newlyBookedTicket?.let { ticket ->
        AlertDialog(
            onDismissRequest = { newlyBookedTicket = null },
            modifier = Modifier.testTag("booking_success_dialog"),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Appointment Confirmed!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Your appointment has been registered and you have entered the virtual queue.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Slate900)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                Text("Token Number:", color = Slate500, fontSize = 12.sp)
                                Text(ticket.token, fontWeight = FontWeight.Black, color = PrimaryIndigo, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                Text("Service:", color = Slate500, fontSize = 12.sp)
                                Text(ticket.serviceType.title, fontWeight = FontWeight.Bold, color = Slate900, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                Text("Queue Position:", color = Slate500, fontSize = 12.sp)
                                Text("#${ticket.positionInQueue}", fontWeight = FontWeight.Bold, color = Slate900, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                Text("Estimated Wait:", color = Slate500, fontSize = 12.sp)
                                Text("${ticket.estimatedWaitMinutes} mins", fontWeight = FontWeight.Bold, color = StatusSuccess, fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        newlyBookedTicket = null
                        viewModel.navigate(AppDestination.VIRTUAL_QUEUE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    modifier = Modifier.testTag("dialog_view_radar_button")
                ) {
                    Text("Track in Virtual Line", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { newlyBookedTicket = null }) {
                    Text("Close")
                }
            }
        )
    }
}
