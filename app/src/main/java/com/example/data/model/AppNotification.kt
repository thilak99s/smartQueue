package com.example.data.model

enum class NotificationType {
    SUCCESS,
    INFO,
    WARNING,
    URGENT,
    RECALCULATION,
    COUNTER_ASSIGNED
}

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType = NotificationType.INFO,
    val timestamp: Long = System.currentTimeMillis(),
    val relatedToken: String? = null,
    val relatedCounterId: Int? = null,
    val isRead: Boolean = false
)

sealed class InnovationEvent {
    data class Recalculation(
        val reason: String,
        val affectedCount: Int,
        val timestamp: Long = System.currentTimeMillis()
    ) : InnovationEvent()

    data class SmartAllocation(
        val counterId: Int,
        val counterName: String,
        val token: String,
        val customerName: String,
        val serviceType: ServiceType,
        val timestamp: Long = System.currentTimeMillis()
    ) : InnovationEvent()
}
