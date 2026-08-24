package com.example.data.model

enum class CounterStatus {
    AVAILABLE,
    BUSY,
    OFFLINE
}

data class Counter(
    val id: Int,
    val name: String,
    val supportedServices: List<ServiceType>,
    val status: CounterStatus = CounterStatus.AVAILABLE,
    val currentTicketToken: String? = null,
    val currentCustomerName: String? = null,
    val currentService: ServiceType? = null,
    val totalServed: Int = 0,
    val serviceStartTime: Long? = null,
    val estimatedCompletionMinutes: Int = 0
) {
    fun canHandle(service: ServiceType): Boolean {
        return supportedServices.contains(service)
    }

    val isAvailable: Boolean
        get() = status == CounterStatus.AVAILABLE
}
