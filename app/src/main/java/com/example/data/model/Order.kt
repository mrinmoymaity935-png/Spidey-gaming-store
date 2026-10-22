package com.example.data.model

enum class PaymentStatus(val label: String) {
    PENDING("Payment Pending"),
    SUBMITTED("UTR Submitted (Verifying)"),
    VERIFIED("Payment Verified"),
    FAILED("Payment Failed")
}

enum class FulfillmentStatus(val label: String) {
    PENDING("Order Received"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing Delivery"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

data class OrderItem(
    val productId: String,
    val productName: String,
    val priceInr: Double,
    val quantity: Int
) {
    val totalInr: Double get() = priceInr * quantity
}

data class Order(
    val id: String,
    val userId: String,
    val customerPhone: String,
    val customerName: String,
    val targetRecipientInfo: String, // Player UID or shipping address
    val items: List<OrderItem>,
    val totalAmountInr: Double,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val fulfillmentStatus: FulfillmentStatus = FulfillmentStatus.PENDING,
    val paymentUtr: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isDemoOrder: Boolean = true,
    val adminNotes: String = ""
)
