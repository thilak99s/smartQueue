package com.example.data.repository

import com.example.data.model.AppNotification
import com.example.data.model.Counter
import com.example.data.model.CounterStatus
import com.example.data.model.InnovationEvent
import com.example.data.model.NotificationType
import com.example.data.model.QueueTicket
import com.example.data.model.ServiceType
import com.example.data.model.TicketStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class QueueState(
    val tickets: List<QueueTicket> = emptyList(),
    val counters: List<Counter> = emptyList(),
    val notifications: List<AppNotification> = emptyList(),
    val currentUserId: String = "USER-SNEHA-A108",
    val latestInnovationEvent: InnovationEvent? = null,
    val totalServedCount: Int = 18,
    val totalCancelledCount: Int = 2,
    val autoSimulationRunning: Boolean = false,
    val lastRecalculationTimestamp: Long = System.currentTimeMillis()
)

class QueueRepository {

    private val _state = MutableStateFlow(QueueState())
    val state: StateFlow<QueueState> = _state.asStateFlow()

    private var tokenCounter = 111

    init {
        initializeSampleData()
    }

    fun initializeSampleData() {
        val initialCounters = listOf(
            Counter(
                id = 1,
                name = "Counter 1",
                supportedServices = listOf(ServiceType.GENERAL, ServiceType.DOCUMENT_VERIFICATION),
                status = CounterStatus.BUSY,
                currentTicketToken = "A101",
                currentCustomerName = "Rahul Sharma",
                currentService = ServiceType.GENERAL,
                totalServed = 6,
                estimatedCompletionMinutes = 3
            ),
            Counter(
                id = 2,
                name = "Counter 2",
                supportedServices = listOf(ServiceType.PAYMENT, ServiceType.GENERAL),
                status = CounterStatus.BUSY,
                currentTicketToken = "C102",
                currentCustomerName = "Priya Patel",
                currentService = ServiceType.PAYMENT,
                totalServed = 5,
                estimatedCompletionMinutes = 2
            ),
            Counter(
                id = 3,
                name = "Counter 3",
                supportedServices = listOf(ServiceType.CONSULTATION, ServiceType.CUSTOMER_SUPPORT),
                status = CounterStatus.BUSY,
                currentTicketToken = "D103",
                currentCustomerName = "Arun Verma",
                currentService = ServiceType.CONSULTATION,
                totalServed = 4,
                estimatedCompletionMinutes = 7
            ),
            Counter(
                id = 4,
                name = "Counter 4",
                supportedServices = listOf(ServiceType.GENERAL, ServiceType.DOCUMENT_VERIFICATION, ServiceType.PAYMENT),
                status = CounterStatus.AVAILABLE,
                currentTicketToken = null,
                currentCustomerName = null,
                currentService = null,
                totalServed = 3,
                estimatedCompletionMinutes = 0
            )
        )

        val initialTickets = listOf(
            QueueTicket(
                id = "T-101",
                token = "A101",
                customerName = "Rahul Sharma",
                customerPhone = "+1 (555) 234-5678",
                customerEmail = "rahul.s@example.com",
                serviceType = ServiceType.GENERAL,
                isPriority = false,
                status = TicketStatus.SERVING,
                assignedCounterId = 1,
                positionInQueue = 0,
                peopleAhead = 0,
                estimatedWaitMinutes = 0
            ),
            QueueTicket(
                id = "T-102",
                token = "C102",
                customerName = "Priya Patel",
                customerPhone = "+1 (555) 345-6789",
                customerEmail = "priya.p@example.com",
                serviceType = ServiceType.PAYMENT,
                isPriority = false,
                status = TicketStatus.SERVING,
                assignedCounterId = 2,
                positionInQueue = 0,
                peopleAhead = 0,
                estimatedWaitMinutes = 0
            ),
            QueueTicket(
                id = "T-103",
                token = "D103",
                customerName = "Arun Verma",
                customerPhone = "+1 (555) 456-7890",
                customerEmail = "arun.v@example.com",
                serviceType = ServiceType.CONSULTATION,
                isPriority = false,
                status = TicketStatus.SERVING,
                assignedCounterId = 3,
                positionInQueue = 0,
                peopleAhead = 0,
                estimatedWaitMinutes = 0
            ),
            QueueTicket(
                id = "T-104",
                token = "B104",
                customerName = "Kumar Roy",
                customerPhone = "+1 (555) 567-8901",
                customerEmail = "kumar.r@example.com",
                serviceType = ServiceType.DOCUMENT_VERIFICATION,
                isPriority = true,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 600000
            ),
            QueueTicket(
                id = "T-105",
                token = "A105",
                customerName = "Neha Gupta",
                customerPhone = "+1 (555) 678-9012",
                customerEmail = "neha.g@example.com",
                serviceType = ServiceType.GENERAL,
                isPriority = false,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 500000
            ),
            QueueTicket(
                id = "T-106",
                token = "E106",
                customerName = "Vikram Malhotra",
                customerPhone = "+1 (555) 789-0123",
                customerEmail = "vikram.m@example.com",
                serviceType = ServiceType.CUSTOMER_SUPPORT,
                isPriority = false,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 400000
            ),
            QueueTicket(
                id = "T-107",
                token = "C107",
                customerName = "Ananya Desai",
                customerPhone = "+1 (555) 890-1234",
                customerEmail = "ananya.d@example.com",
                serviceType = ServiceType.PAYMENT,
                isPriority = false,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 300000
            ),
            QueueTicket(
                id = "USER-SNEHA-A108",
                token = "A108",
                customerName = "Sneha Rao (You)",
                customerPhone = "+1 (555) 901-2345",
                customerEmail = "sneha.rao@example.com",
                serviceType = ServiceType.GENERAL,
                isPriority = false,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 200000,
                isCurrentUser = true
            ),
            QueueTicket(
                id = "T-109",
                token = "D109",
                customerName = "Rohan Nair",
                customerPhone = "+1 (555) 012-3456",
                customerEmail = "rohan.n@example.com",
                serviceType = ServiceType.CONSULTATION,
                isPriority = false,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 100000
            ),
            QueueTicket(
                id = "T-110",
                token = "B110",
                customerName = "Meera Kapoor",
                customerPhone = "+1 (555) 123-4567",
                customerEmail = "meera.k@example.com",
                serviceType = ServiceType.DOCUMENT_VERIFICATION,
                isPriority = true,
                status = TicketStatus.WAITING,
                createdAt = System.currentTimeMillis() - 50000
            )
        )

        val initialNotifications = listOf(
            AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Welcome to SmartQueue",
                message = "Your token A108 has been registered in the Virtual Queue.",
                type = NotificationType.INFO,
                relatedToken = "A108"
            ),
            AppNotification(
                id = UUID.randomUUID().toString(),
                title = "System Ready",
                message = "Smart Counter Allocation and Automatic Recalculation engines active.",
                type = NotificationType.SUCCESS
            )
        )

        _state.value = QueueState(
            tickets = initialTickets,
            counters = initialCounters,
            notifications = initialNotifications,
            currentUserId = "USER-SNEHA-A108"
        )

        // Automatically calculate the queue positions and wait times
        recalculateQueueInternal("System Initialization", autoAllocate = true)
    }

    // ==========================================
    // CORE INNOVATION 1: AUTOMATIC RECALCULATION
    // ==========================================
    fun recalculateQueue(reason: String) {
        recalculateQueueInternal(reason, autoAllocate = false)
    }

    private fun recalculateQueueInternal(reason: String, autoAllocate: Boolean = false) {
        _state.update { current ->
            var updatedTickets = current.tickets.toMutableList()
            val activeCounters = current.counters.filter { it.status != CounterStatus.OFFLINE }

            // Group waiting tickets: priority first, then creation time
            val waitingIndices = updatedTickets.indices.filter { updatedTickets[it].status == TicketStatus.WAITING }
                .sortedWith(
                    compareByDescending<Int> { updatedTickets[it].isPriority }
                        .thenBy { updatedTickets[it].createdAt }
                )

            val notificationsToAdd = mutableListOf<AppNotification>()

            waitingIndices.forEachIndexed { rank, ticketIndex ->
                val ticket = updatedTickets[ticketIndex]
                val position = rank + 1
                val peopleAhead = rank

                // Dynamic wait time calculation based on service type average and counter throughput
                val matchingCounters = activeCounters.count { it.canHandle(ticket.serviceType) }.coerceAtLeast(1)
                val baseWait = (peopleAhead * ticket.serviceType.avgDurationMinutes) / matchingCounters
                val estimatedWait = if (ticket.isPriority) (baseWait * 0.6).toInt().coerceAtLeast(2) else baseWait.coerceAtLeast(3)

                // Near turn alert trigger
                var alerted = ticket.isNearTurnAlerted
                if (position <= 2 && !alerted && ticket.isCurrentUser) {
                    alerted = true
                    notificationsToAdd.add(
                        AppNotification(
                            id = UUID.randomUUID().toString(),
                            title = "Your Turn is Approaching! 🔔",
                            message = "Only $peopleAhead person ahead of you. Please prepare to move towards the service hall.",
                            type = NotificationType.URGENT,
                            relatedToken = ticket.token
                        )
                    )
                }

                updatedTickets[ticketIndex] = ticket.copy(
                    positionInQueue = position,
                    peopleAhead = peopleAhead,
                    estimatedWaitMinutes = estimatedWait,
                    isNearTurnAlerted = alerted,
                    lastRecalculationReason = reason
                )
            }

            // Push an Innovation event notification for recalculation if triggered by action
            val event = InnovationEvent.Recalculation(
                reason = reason,
                affectedCount = waitingIndices.size
            )

            if (reason != "System Initialization") {
                notificationsToAdd.add(
                    AppNotification(
                        id = UUID.randomUUID().toString(),
                        title = "Queue Recalculated ⚡",
                        message = "Reason: $reason. ${waitingIndices.size} positions & wait times automatically updated.",
                        type = NotificationType.RECALCULATION
                    )
                )
            }

            current.copy(
                tickets = updatedTickets,
                notifications = notificationsToAdd + current.notifications,
                latestInnovationEvent = event,
                lastRecalculationTimestamp = System.currentTimeMillis()
            )
        }

        if (autoAllocate) {
            allocateAvailableCounters()
        }
    }

    // ==========================================
    // CORE INNOVATION 2: SMART COUNTER ALLOCATION
    // ==========================================
    fun allocateAvailableCounters() {
        _state.update { current ->
            var updatedTickets = current.tickets.toMutableList()
            var updatedCounters = current.counters.toMutableList()
            val notificationsToAdd = mutableListOf<AppNotification>()
            var allocationEvent: InnovationEvent.SmartAllocation? = null

            // Find available counters
            val availableCounterIndices = updatedCounters.indices.filter { updatedCounters[it].status == CounterStatus.AVAILABLE }

            for (counterIndex in availableCounterIndices) {
                val counter = updatedCounters[counterIndex]

                // Find best matching waiting ticket:
                // 1. Priority tickets that counter can handle
                // 2. Waiting tickets that counter can handle, ordered by arrival
                val waitingCandidates = updatedTickets.indices
                    .filter { updatedTickets[it].status == TicketStatus.WAITING && counter.canHandle(updatedTickets[it].serviceType) }
                    .sortedWith(
                        compareByDescending<Int> { updatedTickets[it].isPriority }
                            .thenBy { updatedTickets[it].createdAt }
                    )

                if (waitingCandidates.isNotEmpty()) {
                    val assignedTicketIndex = waitingCandidates.first()
                    val assignedTicket = updatedTickets[assignedTicketIndex]

                    // Update ticket
                    updatedTickets[assignedTicketIndex] = assignedTicket.copy(
                        status = TicketStatus.SERVING,
                        assignedCounterId = counter.id,
                        positionInQueue = 0,
                        peopleAhead = 0,
                        estimatedWaitMinutes = 0
                    )

                    // Update counter
                    updatedCounters[counterIndex] = counter.copy(
                        status = CounterStatus.BUSY,
                        currentTicketToken = assignedTicket.token,
                        currentCustomerName = assignedTicket.customerName,
                        currentService = assignedTicket.serviceType,
                        serviceStartTime = System.currentTimeMillis(),
                        estimatedCompletionMinutes = assignedTicket.serviceType.avgDurationMinutes
                    )

                    allocationEvent = InnovationEvent.SmartAllocation(
                        counterId = counter.id,
                        counterName = counter.name,
                        token = assignedTicket.token,
                        customerName = assignedTicket.customerName,
                        serviceType = assignedTicket.serviceType
                    )

                    val isUser = assignedTicket.isCurrentUser
                    notificationsToAdd.add(
                        AppNotification(
                            id = UUID.randomUUID().toString(),
                            title = if (isUser) "Counter ${counter.id} Assigned to You! 🚀" else "Smart Allocation: ${counter.name}",
                            message = if (isUser)
                                "Please proceed to ${counter.name} now for ${assignedTicket.serviceType.title}."
                            else
                                "Ticket ${assignedTicket.token} (${assignedTicket.customerName}) allocated to ${counter.name}.",
                            type = NotificationType.COUNTER_ASSIGNED,
                            relatedToken = assignedTicket.token,
                            relatedCounterId = counter.id
                        )
                    )
                }
            }

            current.copy(
                tickets = updatedTickets,
                counters = updatedCounters,
                notifications = notificationsToAdd + current.notifications,
                latestInnovationEvent = allocationEvent ?: current.latestInnovationEvent
            )
        }

        // After assigning, recalculate remaining queue wait times and positions
        recalculateQueueInternal("Post-Counter Allocation", autoAllocate = false)
    }

    // ==========================================
    // USER ACTIONS
    // ==========================================
    fun bookAppointment(
        name: String,
        phone: String,
        email: String,
        serviceType: ServiceType,
        date: String,
        timeSlot: String,
        preferredCounterId: Int?,
        isPriority: Boolean
    ): QueueTicket {
        val nextNum = tokenCounter++
        val token = "${serviceType.prefix}$nextNum"
        val ticketId = "USER-${UUID.randomUUID().toString().take(8)}"

        val newTicket = QueueTicket(
            id = ticketId,
            token = token,
            customerName = name,
            customerPhone = phone,
            customerEmail = email,
            serviceType = serviceType,
            isPriority = isPriority,
            preferredCounterId = preferredCounterId,
            status = TicketStatus.WAITING,
            appointmentDate = date,
            appointmentTime = timeSlot,
            createdAt = System.currentTimeMillis(),
            isCurrentUser = true
        )

        _state.update { current ->
            // Mark previous user tickets as non-primary current user or keep
            val updated = current.tickets.map { it.copy(isCurrentUser = false) } + newTicket
            val notification = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Appointment Confirmed! 🎉",
                message = "Token $token generated for ${serviceType.title} on $date at $timeSlot.",
                type = NotificationType.SUCCESS,
                relatedToken = token
            )

            current.copy(
                tickets = updated,
                currentUserId = ticketId,
                notifications = listOf(notification) + current.notifications
            )
        }

        recalculateQueue("New Customer Joined ($token)")
        allocateAvailableCounters()

        return _state.value.tickets.first { it.id == ticketId }
    }

    fun cancelTicket(ticketId: String, reason: String = "Customer Cancelled") {
        val ticket = _state.value.tickets.firstOrNull { it.id == ticketId } ?: return

        // If ticket was being served at a counter, make counter available
        if (ticket.status == TicketStatus.SERVING && ticket.assignedCounterId != null) {
            _state.update { current ->
                val updatedCounters = current.counters.map { c ->
                    if (c.id == ticket.assignedCounterId) {
                        c.copy(
                            status = CounterStatus.AVAILABLE,
                            currentTicketToken = null,
                            currentCustomerName = null,
                            currentService = null,
                            estimatedCompletionMinutes = 0
                        )
                    } else c
                }
                current.copy(counters = updatedCounters)
            }
        }

        _state.update { current ->
            val updated = current.tickets.map {
                if (it.id == ticketId) it.copy(status = TicketStatus.CANCELLED) else it
            }
            val notif = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Appointment Cancelled",
                message = "Ticket ${ticket.token} (${ticket.customerName}) was cancelled.",
                type = NotificationType.WARNING,
                relatedToken = ticket.token
            )
            current.copy(
                tickets = updated,
                totalCancelledCount = current.totalCancelledCount + 1,
                notifications = listOf(notif) + current.notifications
            )
        }

        recalculateQueue("Ticket ${ticket.token} Cancelled")
        allocateAvailableCounters()
    }

    fun skipTicket(ticketId: String) {
        val ticket = _state.value.tickets.firstOrNull { it.id == ticketId } ?: return

        _state.update { current ->
            val updated = current.tickets.map {
                if (it.id == ticketId) it.copy(status = TicketStatus.SKIPPED) else it
            }
            val notif = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Token Skipped",
                message = "Ticket ${ticket.token} was skipped due to customer delay.",
                type = NotificationType.WARNING,
                relatedToken = ticket.token
            )
            current.copy(
                tickets = updated,
                notifications = listOf(notif) + current.notifications
            )
        }

        recalculateQueue("Ticket ${ticket.token} Skipped")
        allocateAvailableCounters()
    }

    // ==========================================
    // ADMIN ACTIONS & COUNTER CONTROLS
    // ==========================================
    fun completeService(counterId: Int) {
        val counter = _state.value.counters.firstOrNull { it.id == counterId } ?: return
        val currentToken = counter.currentTicketToken

        _state.update { current ->
            val updatedTickets = current.tickets.map { ticket ->
                if (ticket.token == currentToken && ticket.status == TicketStatus.SERVING) {
                    ticket.copy(status = TicketStatus.COMPLETED)
                } else ticket
            }

            val updatedCounters = current.counters.map { c ->
                if (c.id == counterId) {
                    c.copy(
                        status = CounterStatus.AVAILABLE,
                        currentTicketToken = null,
                        currentCustomerName = null,
                        currentService = null,
                        totalServed = c.totalServed + 1,
                        estimatedCompletionMinutes = 0
                    )
                } else c
            }

            val notif = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Service Completed ✅",
                message = "Counter $counterId finished serving ticket $currentToken.",
                type = NotificationType.SUCCESS,
                relatedCounterId = counterId,
                relatedToken = currentToken
            )

            current.copy(
                tickets = updatedTickets,
                counters = updatedCounters,
                totalServedCount = current.totalServedCount + 1,
                notifications = listOf(notif) + current.notifications
            )
        }

        // Automatic smart allocation & recalculation!
        allocateAvailableCounters()
    }

    fun makeCounterAvailable(counterId: Int) {
        _state.update { current ->
            val updatedCounters = current.counters.map { c ->
                if (c.id == counterId) {
                    c.copy(
                        status = CounterStatus.AVAILABLE,
                        currentTicketToken = null,
                        currentCustomerName = null,
                        currentService = null,
                        estimatedCompletionMinutes = 0
                    )
                } else c
            }
            current.copy(counters = updatedCounters)
        }

        allocateAvailableCounters()
    }

    fun toggleCounterStatus(counterId: Int) {
        val counter = _state.value.counters.firstOrNull { it.id == counterId } ?: return
        val newStatus = when (counter.status) {
            CounterStatus.AVAILABLE -> CounterStatus.BUSY
            CounterStatus.BUSY -> CounterStatus.AVAILABLE
            CounterStatus.OFFLINE -> CounterStatus.AVAILABLE
        }

        if (newStatus == CounterStatus.AVAILABLE) {
            makeCounterAvailable(counterId)
        } else {
            _state.update { current ->
                val updatedCounters = current.counters.map {
                    if (it.id == counterId) it.copy(status = newStatus) else it
                }
                current.copy(counters = updatedCounters)
            }
            recalculateQueue("Counter $counterId Status Changed to $newStatus")
        }
    }

    fun callNextForCounter(counterId: Int) {
        makeCounterAvailable(counterId)
    }

    fun manualAssignCounter(ticketId: String, counterId: Int) {
        val ticket = _state.value.tickets.firstOrNull { it.id == ticketId } ?: return
        val counter = _state.value.counters.firstOrNull { it.id == counterId } ?: return

        _state.update { current ->
            val updatedTickets = current.tickets.map {
                if (it.id == ticketId) {
                    it.copy(status = TicketStatus.SERVING, assignedCounterId = counterId, estimatedWaitMinutes = 0, positionInQueue = 0, peopleAhead = 0)
                } else it
            }

            val updatedCounters = current.counters.map {
                if (it.id == counterId) {
                    it.copy(
                        status = CounterStatus.BUSY,
                        currentTicketToken = ticket.token,
                        currentCustomerName = ticket.customerName,
                        currentService = ticket.serviceType,
                        serviceStartTime = System.currentTimeMillis(),
                        estimatedCompletionMinutes = ticket.serviceType.avgDurationMinutes
                    )
                } else it
            }

            val notif = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Manual Counter Assignment",
                message = "Assigned ${ticket.token} (${ticket.customerName}) directly to ${counter.name}.",
                type = NotificationType.COUNTER_ASSIGNED,
                relatedToken = ticket.token,
                relatedCounterId = counterId
            )

            current.copy(
                tickets = updatedTickets,
                counters = updatedCounters,
                notifications = listOf(notif) + current.notifications
            )
        }

        recalculateQueue("Manual Assignment to Counter $counterId")
    }

    // ==========================================
    // SIMULATION SUITE FOR DEMO / HACKATHON
    // ==========================================
    fun simulateCustomerCompletion() {
        // Complete whichever counter is busy
        val busyCounter = _state.value.counters.firstOrNull { it.status == CounterStatus.BUSY }
        if (busyCounter != null) {
            completeService(busyCounter.id)
        } else {
            // If all available, make one busy or notify
            _state.update { current ->
                val notif = AppNotification(
                    id = UUID.randomUUID().toString(),
                    title = "Simulation Note",
                    message = "All counters are currently available. Adding next ticket.",
                    type = NotificationType.INFO
                )
                current.copy(notifications = listOf(notif) + current.notifications)
            }
        }
    }

    fun simulateCancellationInMiddle() {
        // Find a waiting ticket in the middle (e.g. index 1 or 2 among waiting)
        val waitingTickets = _state.value.tickets.filter { it.status == TicketStatus.WAITING && !it.isCurrentUser }
        if (waitingTickets.isNotEmpty()) {
            val target = waitingTickets.getOrNull(1) ?: waitingTickets.first()
            cancelTicket(target.id, "Demo Simulation: Mid-Queue Cancellation")
        }
    }

    fun simulateDelay() {
        // Add wait time / delay to currently serving
        _state.update { current ->
            val notif = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "Delay Injected (+5m) ⏱️",
                message = "Counter 1 complex document verification taking longer. Recalculating queue...",
                type = NotificationType.WARNING
            )
            current.copy(notifications = listOf(notif) + current.notifications)
        }
        recalculateQueue("Service Delay at Counter 1 (+5 mins)")
    }

    fun simulateAddCustomer(isPriority: Boolean = false) {
        val names = listOf("Amit Verma", "Divya Sen", "Kavita Reddy", "Sanjay Joshi", "Pooja Hegde", "Manoj Tiwari")
        val randomName = names.random()
        val randomService = ServiceType.entries.random()
        bookAppointment(
            name = randomName,
            phone = "+1 (555) ${ (100..999).random() }-${ (1000..9999).random() }",
            email = "${randomName.lowercase().replace(" ", ".")}@example.com",
            serviceType = randomService,
            date = "Today",
            timeSlot = "12:00 PM",
            preferredCounterId = null,
            isPriority = isPriority
        )
    }

    fun simulateMakeCounterAvailable() {
        val busy = _state.value.counters.firstOrNull { it.status == CounterStatus.BUSY }
        if (busy != null) {
            makeCounterAvailable(busy.id)
        } else {
            allocateAvailableCounters()
        }
    }

    fun setCurrentUser(ticketId: String) {
        _state.update { current ->
            val updated = current.tickets.map {
                it.copy(isCurrentUser = (it.id == ticketId))
            }
            current.copy(tickets = updated, currentUserId = ticketId)
        }
    }

    fun clearNotifications() {
        _state.update { it.copy(notifications = emptyList()) }
    }

    fun markNotificationRead(id: String) {
        _state.update { current ->
            val updated = current.notifications.map {
                if (it.id == id) it.copy(isRead = true) else it
            }
            current.copy(notifications = updated)
        }
    }
}
