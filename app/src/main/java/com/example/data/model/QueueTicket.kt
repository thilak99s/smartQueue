package com.example.data.model

enum class TicketStatus {
    WAITING,
    SERVING,
    COMPLETED,
    CANCELLED,
    SKIPPED
}

data class QueueTicket(
    val id: String,
    val token: String,
    val customerName: String,
    val customerPhone: String = "+1 (555) 019-2834",
    val customerEmail: String = "",
    val serviceType: ServiceType = ServiceType.GENERAL,
    val isPriority: Boolean = false,
    val preferredCounterId: Int? = null,
    val status: TicketStatus = TicketStatus.WAITING,
    val positionInQueue: Int = 0,
    val peopleAhead: Int = 0,
    val estimatedWaitMinutes: Int = 0,
    val assignedCounterId: Int? = null,
    val appointmentTime: String = "11:30 AM",
    val appointmentDate: String = "Today",
    val createdAt: Long = System.currentTimeMillis(),
    val isCurrentUser: Boolean = false,
    val isNearTurnAlerted: Boolean = false,
    val lastRecalculationReason: String? = null
)
