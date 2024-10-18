package io.github.soat7.myburguercontrol.domain.entities.enum

enum class PaymentStatus() {
    REQUESTED,
    APPROVED,
    DENIED,
    ;

    companion object {
        fun fromString(value: String): PaymentStatus = when (value.lowercase()) {
            "payment_required" -> REQUESTED
            "paid" -> APPROVED
            else -> DENIED
        }
    }
}
