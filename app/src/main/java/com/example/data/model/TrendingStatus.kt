package com.example.data.model

data class TrendingStatus(
    val id: String,
    val title: String,
    val tag: String = "TRENDING", // TRENDING, HOT DEAL, EVENT, NOTICE
    val description: String,
    val promoCode: String? = null,
    val targetProductId: String? = null,
    val bgGradientHex: Pair<Long, Long> = Pair(0xFFE50914, 0xFF990000), // Default red gradient
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
