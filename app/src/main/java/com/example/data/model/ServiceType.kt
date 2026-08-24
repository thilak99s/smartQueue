package com.example.data.model

enum class ServiceType(
    val title: String,
    val prefix: String,
    val avgDurationMinutes: Int,
    val description: String,
    val iconName: String
) {
    GENERAL("General Service", "A", 5, "General inquiries and token services", "HelpOutline"),
    DOCUMENT_VERIFICATION("Document Verification", "B", 8, "Official document review and notary", "VerifiedUser"),
    PAYMENT("Payment Service", "C", 4, "Bill payments, fees & disbursements", "Payments"),
    CONSULTATION("Consultation", "D", 12, "One-on-one specialist consultation", "RecordVoiceOver"),
    CUSTOMER_SUPPORT("Customer Support", "E", 6, "Account help & grievance resolutions", "SupportAgent");

    companion object {
        fun fromPrefix(prefix: String): ServiceType {
            return entries.firstOrNull { it.prefix.equals(prefix, ignoreCase = true) } ?: GENERAL
        }
    }
}
