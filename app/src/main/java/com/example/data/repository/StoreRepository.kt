package com.example.data.repository

import com.example.data.model.CartItem
import com.example.data.model.FulfillmentStatus
import com.example.data.model.Order
import com.example.data.model.OrderItem
import com.example.data.model.PaymentStatus
import com.example.data.model.Product
import com.example.data.model.StoreSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StoreRepository {

    // Initial default catalog
    private val defaultProducts = listOf(
        Product(
            id = "prod_dia_100",
            name = "100 Diamonds + 10 Bonus",
            category = "Diamonds",
            description = "Direct top-up via Player UID. Instant automated credit to your gaming account.",
            priceInr = 80.0,
            originalPriceInr = 95.0,
            inStock = true,
            stockCount = 500,
            instantDelivery = true,
            deliveryInfo = "Instant delivery to Player UID within 2 minutes",
            badge = "POPULAR",
            iconType = "DIAMOND"
        ),
        Product(
            id = "prod_dia_310",
            name = "310 Diamonds + 31 Bonus",
            category = "Diamonds",
            description = "Most popular diamond bundle. Unlocks weapon royales, character crates, and spins.",
            priceInr = 240.0,
            originalPriceInr = 280.0,
            inStock = true,
            stockCount = 350,
            instantDelivery = true,
            deliveryInfo = "Instant UID credit",
            badge = "BEST VALUE",
            iconType = "DIAMOND"
        ),
        Product(
            id = "prod_dia_520",
            name = "520 Diamonds + 52 Bonus",
            category = "Diamonds",
            description = "Ideal for season pass progression and legendary gun skins.",
            priceInr = 400.0,
            originalPriceInr = 480.0,
            inStock = true,
            stockCount = 200,
            instantDelivery = true,
            deliveryInfo = "Instant UID credit",
            badge = "HOT",
            iconType = "DIAMOND"
        ),
        Product(
            id = "prod_pass_weekly",
            name = "Weekly Membership Pass",
            category = "Game Passes",
            description = "Receive 450 total diamonds over 7 days + exclusive weekly discount store access.",
            priceInr = 160.0,
            originalPriceInr = 190.0,
            inStock = true,
            stockCount = 150,
            instantDelivery = true,
            deliveryInfo = "Instant pass activation on UID",
            badge = "SAVER",
            iconType = "PASS"
        ),
        Product(
            id = "prod_pass_monthly",
            name = "Monthly Elite Membership",
            category = "Game Passes",
            description = "Receive 2600 total diamonds over 30 days + VIP customer icon and daily rewards.",
            priceInr = 799.0,
            originalPriceInr = 950.0,
            inStock = true,
            stockCount = 100,
            instantDelivery = true,
            deliveryInfo = "Instant pass activation on UID",
            badge = "VIP",
            iconType = "PASS"
        ),
        Product(
            id = "prod_card_gplay_100",
            name = "Google Play Gift Card ₹100",
            category = "Gift Cards",
            description = "Official digital redemption voucher. Redeem in Google Play Store for in-app purchases.",
            priceInr = 100.0,
            inStock = true,
            stockCount = 80,
            instantDelivery = true,
            deliveryInfo = "Voucher code sent via SMS / WhatsApp",
            badge = "OFFICIAL",
            iconType = "CARD"
        ),
        Product(
            id = "prod_card_gplay_500",
            name = "Google Play Gift Card ₹500",
            category = "Gift Cards",
            description = "Digital code voucher. Zero expiry. Valid across all Android games and apps.",
            priceInr = 500.0,
            inStock = true,
            stockCount = 50,
            instantDelivery = true,
            deliveryInfo = "Instant voucher dispatch",
            iconType = "CARD"
        ),
        Product(
            id = "prod_id_ff_75_gm",
            name = "FF Max Lv.75 Grandmaster ID (6 Evo Guns Max + Sakura)",
            category = "Gaming IDs",
            description = "High-tier competitive account with 6 Evo Guns maxed out (MP40 Cobra Lv7, AK Draco Lv7, M1014 Lv6), rare Season 1 Sakura bundle, 14,000+ likes, single owner, 100% clean history.",
            priceInr = 3499.0,
            originalPriceInr = 4999.0,
            inStock = true,
            stockCount = 1,
            instantDelivery = true,
            deliveryInfo = "Instant verified credentials handover via Admin mediation",
            badge = "HOT ID",
            iconType = "GAMING_ID",
            isGamingId = true,
            gameName = "Free Fire Max",
            accountLevel = 75,
            rankTier = "Grandmaster",
            evoGunsCount = 6,
            evoGunsHighlights = "MP40 Cobra Lv7 (Max), AK47 Blue Flame Draco Lv7, M1014 Green Flame Lv6, SCAR Megalodon Lv5",
            rareBundles = "Season 1 Sakura, Season 2 Hip Hop, Red Criminal, Arctic Blue",
            loginMethod = "Google (Full Email Access Handover)",
            serverRegion = "India Server",
            videoUrl = "https://youtube.com/watch?v=spidey_ff_vault_walkthrough",
            videoTitle = "Watch Full 4K Vault & Gun Skins Inspection Video",
            screenshots = listOf(
                "Profile Stats: Level 75, Grandmaster Rank, 14.2k Likes, 4.2 K/D Ratio",
                "Evo Weapon Vault: 6 Maxed Evo Guns with Custom Kill Animation",
                "Rare Bundles: Season 1 Sakura & S2 Hip Hop Outfits",
                "Mythic Emotes: Cobra Exclusive, Tea Time, Pirate Flag & Mythic Fists"
            ),
            accountUidPreview = "UID: 1849****72"
        ),
        Product(
            id = "prod_id_ff_68_cobra",
            name = "FF Max Lv.68 Heroic ID (MP40 Cobra Lv7 + Red Criminal)",
            category = "Gaming IDs",
            description = "Clean single-owner Heroic account. Includes max level MP40 Cobra with snake emote, Red Criminal suit, Season 4 pass, and 8,500+ profile likes.",
            priceInr = 1899.0,
            originalPriceInr = 2499.0,
            inStock = true,
            stockCount = 1,
            instantDelivery = true,
            deliveryInfo = "Safe transfer with SPIDEY Admin protection",
            badge = "VERIFIED",
            iconType = "GAMING_ID",
            isGamingId = true,
            gameName = "Free Fire Max",
            accountLevel = 68,
            rankTier = "Heroic",
            evoGunsCount = 3,
            evoGunsHighlights = "MP40 Predatory Cobra Lv7 (Max), XM8 Destiny Guardian Lv4, UMP Booyah Day",
            rareBundles = "Red Criminal Bundle, Breakdancer Suit, Season 4 Elite Pass",
            loginMethod = "Google Login (Direct Transfer)",
            serverRegion = "India Server",
            videoUrl = "https://youtube.com/watch?v=spidey_ff_cobra_id_demo",
            videoTitle = "Gameplay & Weapon Skins Showcase Video",
            screenshots = listOf(
                "Profile & Rank: Level 68 Heroic, 8.5k Likes",
                "Weapon Vault: MP40 Cobra Lv7 with Max Hit Effect & Emote",
                "Outfits Collection: Red Criminal & Rare Top Hats",
                "Vehicles & Backpacks: Monster Truck Golden & Dragon Wings"
            ),
            accountUidPreview = "UID: 2314****08"
        ),
        Product(
            id = "prod_id_ff_72_hiphop",
            name = "FF Max Lv.72 Elite Master ID (Hip Hop + Blue Flame Draco)",
            category = "Gaming IDs",
            description = "Veteran old-school account from 2018. Features S2 Hip Hop bundle, AK47 Blue Flame Draco max, Golden Shade, Poker MP40, 11,000+ likes.",
            priceInr = 2799.0,
            originalPriceInr = 3800.0,
            inStock = true,
            stockCount = 1,
            instantDelivery = true,
            deliveryInfo = "Direct credential transfer with 100% moneyback guarantee",
            badge = "VETERAN",
            iconType = "GAMING_ID",
            isGamingId = true,
            gameName = "Free Fire Max",
            accountLevel = 72,
            rankTier = "Master",
            evoGunsCount = 4,
            evoGunsHighlights = "AK47 Blue Flame Draco Lv7, M1887 Rapper Underworld, Flashing Spade MP40",
            rareBundles = "Season 2 Hip Hop, Golden Shade, Street Boy, Bunny Warrior",
            loginMethod = "Facebook (Secure Credentials Handover)",
            serverRegion = "India Server",
            videoUrl = "https://youtube.com/watch?v=spidey_ff_hiphop_id_walkthrough",
            videoTitle = "Complete Vault Walkthrough & Collection Review",
            screenshots = listOf(
                "Profile Overview: Level 72 Master, 11.8k Likes, 3.8 K/D",
                "Legendary Skins: AK Draco Lv7 + Rapper Underworld Shotgun",
                "Outfits: Season 2 Hip Hop & Old Veteran Badges",
                "Emotes & Lobby Animations: Throne Emote & Golden Car"
            ),
            accountUidPreview = "UID: 1092****55"
        )
    )

    private val _products = MutableStateFlow<List<Product>>(defaultProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(
        listOf(
            Order(
                id = "SPD-782103",
                userId = "usr_demo",
                customerPhone = "+919876543210",
                customerName = "Rahul Sharma",
                targetRecipientInfo = "Player UID: 489201938",
                items = listOf(
                    OrderItem(
                        productId = "prod_dia_310",
                        productName = "310 Diamonds + 31 Bonus",
                        priceInr = 240.0,
                        quantity = 1
                    )
                ),
                totalAmountInr = 240.0,
                paymentStatus = PaymentStatus.VERIFIED,
                fulfillmentStatus = FulfillmentStatus.COMPLETED,
                paymentUtr = "429104829102",
                createdAt = System.currentTimeMillis() - 86400000,
                isDemoOrder = true,
                adminNotes = "UID top-up credited successfully via server partner."
            )
        )
    )
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _settings = MutableStateFlow(StoreSettings())
    val settings: StateFlow<StoreSettings> = _settings.asStateFlow()

    private val defaultStatuses = listOf(
        com.example.data.model.TrendingStatus(
            id = "stat_bonus",
            title = "100% Diamond Drop",
            tag = "HOT DEAL",
            description = "Special flash event: Double diamonds on every 520+ bundle today! Instant credit via Player UID.",
            promoCode = "DOUBLE520",
            bgGradientHex = Pair(0xFFE50914, 0xFF800000)
        ),
        com.example.data.model.TrendingStatus(
            id = "stat_pass",
            title = "New Elite Pass Live",
            tag = "TRENDING",
            description = "Season Elite Pass activated. Unlock exclusive emotes, gun skins, and 2600 diamonds over 30 days.",
            promoCode = "ELITE2026",
            bgGradientHex = Pair(0xFF0066FF, 0xFF003399)
        ),
        com.example.data.model.TrendingStatus(
            id = "stat_owner",
            title = "Owner Live Status",
            tag = "STATUS",
            description = "Store owner is active now! UPI UTR verified within 60 seconds and diamonds delivered immediately.",
            bgGradientHex = Pair(0xFF10B981, 0xFF065F46)
        ),
        com.example.data.model.TrendingStatus(
            id = "stat_playcode",
            title = "₹100 Play Store Code",
            tag = "GIFT CARD",
            description = "Get instant digital code for Play Store apps, books & games without extra platform fees.",
            bgGradientHex = Pair(0xFFF59E0B, 0xFFB45309)
        )
    )

    private val _trendingStatuses = MutableStateFlow<List<com.example.data.model.TrendingStatus>>(defaultStatuses)
    val trendingStatuses: StateFlow<List<com.example.data.model.TrendingStatus>> = _trendingStatuses.asStateFlow()

    fun adminAddStatus(status: com.example.data.model.TrendingStatus) {
        _trendingStatuses.update { listOf(status) + it }
    }

    fun adminDeleteStatus(statusId: String) {
        _trendingStatuses.update { list -> list.filterNot { it.id == statusId } }
    }

    private val _wishlist = MutableStateFlow<Set<String>>(setOf("prod_dia_520", "prod_pass_weekly"))
    val wishlist: StateFlow<Set<String>> = _wishlist.asStateFlow()

    fun toggleWishlist(productId: String) {
        _wishlist.update { current ->
            if (current.contains(productId)) current - productId else current + productId
        }
    }

    // Cart Operations
    fun addToCart(product: Product) {
        _cart.update { current ->
            val index = current.indexOfFirst { it.product.id == product.id }
            if (index >= 0) {
                current.toMutableList().apply {
                    this[index] = this[index].copy(quantity = this[index].quantity + 1)
                }
            } else {
                current + CartItem(product = product, quantity = 1)
            }
        }
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        _cart.update { current ->
            current.mapNotNull { item ->
                if (item.product.id == productId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else item
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cart.update { current -> current.filterNot { it.product.id == productId } }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Order Creation
    fun placeOrder(
        userId: String,
        customerPhone: String,
        customerName: String,
        targetRecipient: String
    ): Order {
        val currentCart = _cart.value
        val items = currentCart.map {
            OrderItem(
                productId = it.product.id,
                productName = it.product.name,
                priceInr = it.product.priceInr,
                quantity = it.quantity
            )
        }
        val total = items.sumOf { it.totalInr }
        val orderId = "SPD-${(100000..999999).random()}"

        val order = Order(
            id = orderId,
            userId = userId,
            customerPhone = customerPhone,
            customerName = customerName.ifBlank { "Gamer" },
            targetRecipientInfo = targetRecipient,
            items = items,
            totalAmountInr = total,
            paymentStatus = PaymentStatus.PENDING,
            fulfillmentStatus = FulfillmentStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            isDemoOrder = true
        )

        _orders.update { listOf(order) + it }
        clearCart()
        return order
    }

    fun submitUtr(orderId: String, utr: String): Boolean {
        var found = false
        _orders.update { list ->
            list.map {
                if (it.id == orderId) {
                    found = true
                    it.copy(
                        paymentUtr = utr,
                        paymentStatus = PaymentStatus.SUBMITTED
                    )
                } else it
            }
        }
        return found
    }

    // Admin Operations
    fun adminUpdateOrderStatus(
        orderId: String,
        paymentStatus: PaymentStatus,
        fulfillmentStatus: FulfillmentStatus,
        adminNotes: String
    ) {
        _orders.update { list ->
            list.map {
                if (it.id == orderId) {
                    it.copy(
                        paymentStatus = paymentStatus,
                        fulfillmentStatus = fulfillmentStatus,
                        adminNotes = adminNotes
                    )
                } else it
            }
        }
    }

    fun adminAddProduct(product: Product) {
        _products.update { listOf(product) + it }
    }

    fun adminUpdateProduct(product: Product) {
        _products.update { list ->
            list.map { if (it.id == product.id) product else it }
        }
    }

    fun adminToggleProductVisibility(productId: String) {
        _products.update { list ->
            list.map { if (it.id == productId) it.copy(isHidden = !it.isHidden) else it }
        }
    }

    fun adminDeleteProduct(productId: String) {
        _products.update { list -> list.filterNot { it.id == productId } }
    }

    fun adminUpdateSettings(settings: StoreSettings) {
        _settings.value = settings
    }
}
