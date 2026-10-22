package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FulfillmentStatus
import com.example.data.model.Order
import com.example.data.model.PaymentStatus
import com.example.data.model.Product
import com.example.data.model.StoreSettings
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.AuthRepository
import com.example.data.repository.StoreRepository
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.LocalizedStrings
import com.example.ui.localization.StringsDefinition
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
    INTRO,
    LOGIN,
    OTP_VERIFY,
    HOME,
    CART,
    CHECKOUT,
    PAYMENT,
    ORDERS,
    PROFILE,
    ADMIN,
    AI_CHAT,
    WISHLIST
}

data class AuthUiState(
    val phoneNumberInput: String = "",
    val phoneError: String? = null,
    val otpInput: String = "",
    val otpError: String? = null,
    val isLoading: Boolean = false,
    val resendCountdownSec: Int = 30,
    val canResend: Boolean = false,
    val infoMessage: String? = null,
    val pendingPhoneFormatted: String = "",
    val isDemoMode: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val authRepo = AuthRepository(application)
    val storeRepo = StoreRepository()

    // Navigation & Selected Item - start with Animated Intro
    private val _currentScreen = MutableStateFlow<Screen>(Screen.INTRO)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun onIntroFinish() {
        _currentScreen.value = if (authRepo.currentUser.value != null) Screen.HOME else Screen.LOGIN
    }

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _activePaymentOrder = MutableStateFlow<Order?>(null)
    val activePaymentOrder: StateFlow<Order?> = _activePaymentOrder.asStateFlow()

    private val _selectedOrderDetail = MutableStateFlow<Order?>(null)
    val selectedOrderDetail: StateFlow<Order?> = _selectedOrderDetail.asStateFlow()

    // Auth state
    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepo.currentUser

    // Store state
    val products = storeRepo.products
    val cart = storeRepo.cart
    val orders = storeRepo.orders
    val settings = storeRepo.settings

    val cartTotalCount = cart.map { list -> list.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val cartSubtotal = cart.map { list -> list.sumOf { it.totalInr } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    // Store filtering
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val trendingStatuses = storeRepo.trendingStatuses

    private val _selectedTrendingStatus = MutableStateFlow<com.example.data.model.TrendingStatus?>(null)
    val selectedTrendingStatus: StateFlow<com.example.data.model.TrendingStatus?> = _selectedTrendingStatus.asStateFlow()

    // Multilingual State for Indian Languages
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    val localizedStrings: StateFlow<StringsDefinition> = _currentLanguage
        .map { LocalizedStrings.get(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LocalizedStrings.get(AppLanguage.ENGLISH))

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun setShowLanguageDialog(show: Boolean) {
        _showLanguageDialog.value = show
    }

    val wishlist = storeRepo.wishlist

    fun toggleWishlist(productId: String) {
        storeRepo.toggleWishlist(productId)
    }

    fun selectTrendingStatus(status: com.example.data.model.TrendingStatus?) {
        _selectedTrendingStatus.value = status
    }

    // AI Chat Assistant State
    private val _chatMessages = MutableStateFlow<List<com.example.data.model.ChatMessage>>(
        listOf(
            com.example.data.model.ChatMessage(
                isUser = false,
                text = "👋 Namaste! Welcome to SPIDEY Gaming Store. I am your SPIDEY AI Assistant.\n\nAre you new here? Ask me anything about Free Fire diamond top-ups, how UPI payments work, instant UID delivery, or store offers!"
            )
        )
    )
    val chatMessages: StateFlow<List<com.example.data.model.ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    fun sendChatMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank()) return

        val userMsg = com.example.data.model.ChatMessage(isUser = true, text = trimmed)
        _chatMessages.update { it + userMsg }

        viewModelScope.launch {
            _isAiTyping.value = true
            delay(800) // natural response pacing

            val lower = trimmed.lowercase()
            val aiResponse = when {
                lower.contains("id") || lower.contains("account") || lower.contains("gaming id") || lower.contains("evo") || lower.contains("sakura") || lower.contains("video") || lower.contains("screenshot") ->
                    "🎮 **Verified Gaming IDs (Game Accounts):**\n• Tap the **Gaming IDs** category on the Home screen to view verified accounts.\n• Every ID includes **Proof Screenshots** of the Vault, Evo Guns, Badges & Rank, plus a **Video Walkthrough** clip!\n• **Safe Admin Mediation:** Account login credentials (email & password) are securely handed over to you by the SPIDEY admin after UPI payment verification, with 100% moneyback guarantee."

                lower.contains("diamond") || lower.contains("topup") || lower.contains("top up") || lower.contains("free fire") || lower.contains("ff") ->
                    "💎 **How to Top-Up Diamonds:**\n1. Select your diamond pack from the Store (e.g. 100+10, 310+31, or 520+52).\n2. Tap 'Add to Cart' and Proceed to Checkout.\n3. Enter your **Player Game UID**.\n4. Complete UPI scan & submit your 12-digit UTR.\n5. Diamonds are automatically credited to your game account within 2-5 minutes!"

                lower.contains("upi") || lower.contains("pay") || lower.contains("payment") || lower.contains("utr") || lower.contains("qr") ->
                    "📱 **UPI Payment & UTR Guide:**\n• We support all genuine UPI apps: Google Pay, PhonePe, Paytm, or BHIM.\n• At checkout, a dynamic UPI QR is generated with the exact order amount.\n• You can scan with camera, tap 'Open UPI App', or copy the owner UPI ID.\n• After payment in your UPI app, find the **12-digit UTR / Ref No.** in your payment receipt and enter it on the screen to submit for verification."

                lower.contains("delivery") || lower.contains("time") || lower.contains("koto khon") || lower.contains("fast") || lower.contains("speed") ->
                    "⚡ **Delivery Timeline:**\n• Diamonds, Game Passes & Play Codes: **Instant within 2-5 minutes** after UTR confirmation!\n• Gaming IDs: **Within 15-30 minutes** with direct Admin mediation & login credentials handover!"

                lower.contains("safe") || lower.contains("trust") || lower.contains("scam") || lower.contains("genuine") || lower.contains("security") ->
                    "🛡️ **100% Genuine & Safe:**\n• SPIDEY never asks for your personal password, UPI PIN, or bank OTP.\n• For diamond top-ups, we only require your public Game UID.\n• For Gaming IDs, all transactions are mediated by the SPIDEY admin with verified ownership.\n• Store owner is available on WhatsApp support for any query."

                lower.contains("admin") || lower.contains("owner") || lower.contains("dashboard") || lower.contains("manage") ->
                    "👑 **Owner Admin Controls:**\n• Store owner can manage products and Gaming IDs (add video links & proof screenshots).\n• Verify customer UTR payments and update order status (Confirmed, Processing, Completed).\n• Post **Trending Status / Ads** that appear right at the top of the Store!\n• You can test the Admin Dashboard by switching your role in the Profile tab."

                lower.contains("contact") || lower.contains("whatsapp") || lower.contains("support") || lower.contains("help") || lower.contains("phone") ->
                    "📞 **Customer Support:**\n• WhatsApp Support: **${settings.value.supportWhatsapp}**\n• Support Email: **${settings.value.supportEmail}**\n• Tap the green WhatsApp button in 'Orders' or 'Profile' to chat with us anytime!"

                lower.contains("offer") || lower.contains("discount") || lower.contains("status") || lower.contains("ad") || lower.contains("trend") ->
                    "🔥 **Current Trending Offers:**\n• Check the top **Status Bar** on the Store home screen for today's active deals!\n• Get 100% Bonus Diamonds on selected bundles.\n• Verified Gaming IDs now available with video previews!"

                else ->
                    "🤖 **SPIDEY Assistant:**\nI can help you with:\n1. 🎮 Verified Gaming IDs (with video & proof photos)\n2. 💎 Diamond top-ups & Game Passes\n3. 📱 UPI QR scan & UTR submission\n4. 🔥 Today's trending ads & status offers\n\nWhat would you like to know more about?"
            }

            val botMsg = com.example.data.model.ChatMessage(isUser = false, text = aiResponse)
            _chatMessages.update { it + botMsg }
            _isAiTyping.value = false
        }
    }

    private var countdownJob: Job? = null

    init {
        // Observe user login/logout
        viewModelScope.launch {
            authRepo.currentUser.collect { user ->
                if (user == null) {
                    if (_currentScreen.value != Screen.LOGIN && _currentScreen.value != Screen.OTP_VERIFY) {
                        _currentScreen.value = Screen.LOGIN
                    }
                } else if (_currentScreen.value == Screen.LOGIN || _currentScreen.value == Screen.OTP_VERIFY) {
                    _currentScreen.value = Screen.HOME
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun selectOrderDetail(order: Order?) {
        _selectedOrderDetail.value = order
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    // Phone / OTP Methods
    fun onPhoneNumberChange(input: String) {
        // Allow only digits, max 10
        val digits = input.filter { it.isDigit() }.take(10)
        _authUiState.value = _authUiState.value.copy(
            phoneNumberInput = digits,
            phoneError = null
        )
    }

    fun sendOtp() {
        val phone = _authUiState.value.phoneNumberInput
        val validationErr = authRepo.validateIndianPhoneNumber(phone)
        if (validationErr != null) {
            _authUiState.value = _authUiState.value.copy(phoneError = validationErr)
            return
        }

        viewModelScope.launch {
            _authUiState.value = _authUiState.value.copy(isLoading = true, phoneError = null)
            val result = authRepo.requestOtp(phone)
            result.fold(
                onSuccess = { msg ->
                    _authUiState.value = _authUiState.value.copy(
                        isLoading = false,
                        pendingPhoneFormatted = authRepo.pendingPhoneNumber,
                        infoMessage = "Verification code generated: ${authRepo.generatedOtpCode}",
                        otpInput = "",
                        otpError = null,
                        canResend = false,
                        resendCountdownSec = 30
                    )
                    startOtpCountdown()
                    _currentScreen.value = Screen.OTP_VERIFY
                },
                onFailure = { err ->
                    _authUiState.value = _authUiState.value.copy(
                        isLoading = false,
                        phoneError = err.message ?: "Failed to dispatch SMS OTP"
                    )
                }
            )
        }
    }

    fun onOtpDigitChange(newOtp: String) {
        val digits = newOtp.filter { it.isDigit() }.take(6)
        _authUiState.value = _authUiState.value.copy(
            otpInput = digits,
            otpError = null
        )
        if (digits.length == 6) {
            verifyOtp(digits)
        }
    }

    fun verifyOtp(code: String = _authUiState.value.otpInput) {
        if (code.length != 6) {
            _authUiState.value = _authUiState.value.copy(otpError = "Please enter all 6 digits")
            return
        }

        viewModelScope.launch {
            _authUiState.value = _authUiState.value.copy(isLoading = true, otpError = null)
            val result = authRepo.verifyOtp(code)
            result.fold(
                onSuccess = { user ->
                    _authUiState.value = _authUiState.value.copy(isLoading = false)
                    _currentScreen.value = Screen.HOME
                },
                onFailure = { err ->
                    _authUiState.value = _authUiState.value.copy(
                        isLoading = false,
                        otpError = err.message ?: "Verification failed"
                    )
                }
            )
        }
    }

    fun resendOtp() {
        if (!_authUiState.value.canResend) return
        sendOtp()
    }

    private fun startOtpCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            var remaining = 30
            while (remaining > 0) {
                _authUiState.value = _authUiState.value.copy(
                    resendCountdownSec = remaining,
                    canResend = false
                )
                delay(1000)
                remaining--
            }
            _authUiState.value = _authUiState.value.copy(
                resendCountdownSec = 0,
                canResend = true
            )
        }
    }

    fun logout() {
        authRepo.logout()
        _currentScreen.value = Screen.LOGIN
    }

    fun toggleAdminRole() {
        authRepo.toggleAdminRole()
    }

    fun updateProfile(name: String) {
        authRepo.updateProfile(name)
    }

    // Checkout & Order submission
    fun checkout(targetRecipient: String): Order? {
        val user = currentUser.value ?: return null
        val placedOrder = storeRepo.placeOrder(
            userId = user.id,
            customerPhone = user.phoneNumber,
            customerName = user.displayName,
            targetRecipient = targetRecipient
        )
        _activePaymentOrder.value = placedOrder
        _currentScreen.value = Screen.PAYMENT
        return placedOrder
    }

    fun startPaymentForOrder(order: Order) {
        _activePaymentOrder.value = order
        _currentScreen.value = Screen.PAYMENT
    }

    fun submitUtr(utr: String): Boolean {
        val order = _activePaymentOrder.value ?: return false
        val success = storeRepo.submitUtr(order.id, utr)
        if (success) {
            // refresh active order reference
            _activePaymentOrder.value = storeRepo.orders.value.firstOrNull { it.id == order.id }
        }
        return success
    }
}
