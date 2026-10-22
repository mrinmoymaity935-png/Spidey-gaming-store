package com.example.data.model

data class Product(
    val id: String,
    val name: String,
    val category: String, // "Gaming IDs", "Diamonds", "Game Passes", "Gift Cards"
    val description: String,
    val priceInr: Double,
    val originalPriceInr: Double? = null,
    val inStock: Boolean = true,
    val stockCount: Int = 999,
    val instantDelivery: Boolean = true,
    val deliveryInfo: String = "Instant delivery to Game UID / Email",
    val isHidden: Boolean = false,
    val badge: String? = null,
    val iconType: String = "DIAMOND", // DIAMOND, PASS, CARD, GAMING_ID, BUNDLE

    // Gaming ID Specific Fields
    val isGamingId: Boolean = false,
    val gameName: String = "Free Fire Max",
    val accountLevel: Int? = null,
    val rankTier: String? = null, // e.g. "Grandmaster", "Heroic", "Ace"
    val evoGunsCount: Int? = null,
    val evoGunsHighlights: String? = null, // e.g. "MP40 Cobra Lv7, AK Draco Lv7"
    val rareBundles: String? = null, // e.g. "Sakura S1, Hip Hop S2, Criminal Red"
    val loginMethod: String = "Google", // "Google", "Facebook", "Twitter/X"
    val serverRegion: String = "India",
    val videoUrl: String? = null, // Gameplay / Vault walkthrough video link
    val videoTitle: String? = null,
    val screenshots: List<String> = emptyList(), // Screenshot URLs or photo descriptions
    val accountUidPreview: String? = null
) {
    val discountPercent: Int?
        get() = if (originalPriceInr != null && originalPriceInr > priceInr) {
            (((originalPriceInr - priceInr) / originalPriceInr) * 100).toInt()
        } else null
}

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalInr: Double
        get() = product.priceInr * quantity
}
