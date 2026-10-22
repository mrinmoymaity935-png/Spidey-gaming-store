package com.example.data.model

data class StoreSettings(
    val ownerUpiId: String = "spidey.gaming@okhdfcbank",
    val merchantName: String = "SPIDEY GAMING STORE",
    val supportWhatsapp: String = "+919876543210",
    val supportEmail: String = "support@spideystore.in",
    val announcementText: String = "⚡ Instant Digital Delivery within 2-5 minutes of UTR verification!",
    val enableUpiQr: Boolean = true,
    val enableGPay: Boolean = true,
    val enablePhonePe: Boolean = true,
    val enablePaytm: Boolean = true,
    val manualVerificationNote: String = "Store admin cross-checks UTR with bank records before delivering diamonds/passes."
)
