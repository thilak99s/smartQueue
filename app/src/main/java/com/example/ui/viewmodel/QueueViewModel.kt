package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Counter
import com.example.data.model.InnovationEvent
import com.example.data.model.QueueTicket
import com.example.data.model.ServiceType
import com.example.data.model.TicketStatus
import com.example.data.repository.QueueRepository
import com.example.data.repository.QueueState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppDestination(val title: String, val route: String) {
    LANDING("Home", "landing"),
    USER_DASHBOARD("Dashboard", "dashboard"),
    VIRTUAL_QUEUE("Virtual Queue", "virtual_queue"),
    BOOK_APPOINTMENT("Book", "book_appointment"),
    ADMIN_DASHBOARD("Admin", "admin_dashboard"),
    ANALYTICS("Analytics", "analytics"),
    AUTH("Account", "auth")
}

enum class UserRole {
    USER,
    ADMIN
}

data class FilterState(
    val serviceFilter: ServiceType? = null,
    val statusFilter: TicketStatus? = null,
    val searchQuery: String = ""
)

class QueueViewModel(
    private val repository: QueueRepository = QueueRepository()
) : ViewModel() {

    val queueState: StateFlow<QueueState> = repository.state

    private val _currentDestination = MutableStateFlow(AppDestination.LANDING)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.USER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _isSimulationSheetOpen = MutableStateFlow(false)
    val isSimulationSheetOpen: StateFlow<Boolean> = _isSimulationSheetOpen.asStateFlow()

    private val _isNotificationDialogOpen = MutableStateFlow(false)
    val isNotificationDialogOpen: StateFlow<Boolean> = _isNotificationDialogOpen.asStateFlow()

    private val _latestInnovationEvent = MutableStateFlow<InnovationEvent?>(null)
    val latestInnovationEvent: StateFlow<InnovationEvent?> = _latestInnovationEvent.asStateFlow()

    private val _notifyWhenNearEnabled = MutableStateFlow(true)
    val notifyWhenNearEnabled: StateFlow<Boolean> = _notifyWhenNearEnabled.asStateFlow()

    private var autoSimulationJob: Job? = null

    // Unread Notifications Count
    val unreadNotificationCount: StateFlow<Int> = queueState.map { state ->
        state.notifications.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Filtered tickets for Admin Table
    val filteredTickets: StateFlow<List<QueueTicket>> = combine(
        queueState,
        _filterState
    ) { state, filter ->
        state.tickets.filter { ticket ->
            val matchesService = filter.serviceFilter == null || ticket.serviceType == filter.serviceFilter
            val matchesStatus = filter.statusFilter == null || ticket.status == filter.statusFilter
            val matchesQuery = filter.searchQuery.isBlank() ||
                    ticket.token.contains(filter.searchQuery, ignoreCase = true) ||
                    ticket.customerName.contains(filter.searchQuery, ignoreCase = true)
            matchesService && matchesStatus && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Active Current User Ticket
    val currentUserTicket: StateFlow<QueueTicket?> = queueState.map { state ->
        state.tickets.firstOrNull { it.isCurrentUser }
            ?: state.tickets.firstOrNull { it.id == state.currentUserId }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Currently Serving Tickets
    val currentlyServingTickets: StateFlow<List<QueueTicket>> = queueState.map { state ->
        state.tickets.filter { it.status == TicketStatus.SERVING }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Waiting Tickets
    val waitingTickets: StateFlow<List<QueueTicket>> = queueState.map { state ->
        state.tickets.filter { it.status == TicketStatus.WAITING }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Observe repository innovation events to show transient banner
        viewModelScope.launch {
            queueState.collect { state ->
                state.latestInnovationEvent?.let { event ->
                    _latestInnovationEvent.value = event
                    delay(6000)
                    if (_latestInnovationEvent.value == event) {
                        _latestInnovationEvent.value = null
                    }
                }
            }
        }
    }

    fun navigate(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
        if (role == UserRole.ADMIN && _currentDestination.value == AppDestination.USER_DASHBOARD) {
            _currentDestination.value = AppDestination.ADMIN_DASHBOARD
        } else if (role == UserRole.USER && _currentDestination.value == AppDestination.ADMIN_DASHBOARD) {
            _currentDestination.value = AppDestination.USER_DASHBOARD
        }
    }

    fun toggleRole() {
        setRole(if (_currentRole.value == UserRole.ADMIN) UserRole.USER else UserRole.ADMIN)
    }

    fun setSimulationSheetOpen(open: Boolean) {
        _isSimulationSheetOpen.value = open
    }

    fun setNotificationDialogOpen(open: Boolean) {
        _isNotificationDialogOpen.value = open
    }

    fun toggleNotifyWhenNear() {
        _notifyWhenNearEnabled.value = !_notifyWhenNearEnabled.value
    }

    fun clearLatestEvent() {
        _latestInnovationEvent.value = null
    }

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun updateServiceFilter(service: ServiceType?) {
        _filterState.value = _filterState.value.copy(serviceFilter = service)
    }

    fun updateStatusFilter(status: TicketStatus?) {
        _filterState.value = _filterState.value.copy(statusFilter = status)
    }

    // Repository Actions
    fun bookAppointment(
        name: String,
        phone: String,
        email: String,
        serviceType: ServiceType,
        appointmentDate: String,
        appointmentTime: String,
        preferredCounterId: Int?,
        isPriority: Boolean
    ): QueueTicket? {
        repository.bookAppointment(name, phone, email, serviceType, appointmentDate, appointmentTime, preferredCounterId, isPriority)
        return queueState.value.tickets.firstOrNull { it.isCurrentUser }
    }

    fun cancelTicket(ticketId: String, reason: String = "User requested cancellation") {
        repository.cancelTicket(ticketId, reason)
    }

    fun skipTicket(ticketId: String) {
        repository.skipTicket(ticketId)
    }

    fun completeService(counterId: Int) {
        repository.completeService(counterId)
    }

    fun callNextCustomer(counterId: Int) {
        repository.makeCounterAvailable(counterId)
    }

    fun toggleCounterStatus(counterId: Int) {
        repository.toggleCounterStatus(counterId)
    }

    fun assignCounterManual(ticketId: String, counterId: Int) {
        repository.manualAssignCounter(ticketId, counterId)
    }

    fun recalculateQueueManual() {
        repository.recalculateQueue("Manual Trigger Recalculation")
    }

    fun resetSampleData() {
        repository.initializeSampleData()
    }

    fun clearAllNotifications() {
        repository.clearNotifications()
    }

    fun markNotificationAsRead(id: String) {
        repository.markNotificationRead(id)
    }

    // Simulation Handlers
    fun simulateCustomerCompletion() = repository.simulateCustomerCompletion()
    fun simulateMiddleCancellation() = repository.simulateCancellationInMiddle()
    fun simulateDelayAtCounter(counterId: Int = 1, delayMins: Int = 5) = repository.simulateDelay()
    fun simulateAddCustomer(isPriority: Boolean = false) = repository.simulateAddCustomer(isPriority)
    fun simulateMakeCounterAvailable(counterId: Int = 3) = repository.simulateMakeCounterAvailable()

    fun toggleAutoSimulation() {
        if (autoSimulationJob?.isActive == true) {
            autoSimulationJob?.cancel()
            autoSimulationJob = null
        } else {
            autoSimulationJob = viewModelScope.launch {
                while (true) {
                    delay(8000)
                    val randomAction = (0..2).random()
                    when (randomAction) {
                        0 -> repository.simulateCustomerCompletion()
                        1 -> repository.simulateAddCustomer(isPriority = (0..3).random() == 0)
                        2 -> repository.allocateAvailableCounters()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoSimulationJob?.cancel()
    }
}
