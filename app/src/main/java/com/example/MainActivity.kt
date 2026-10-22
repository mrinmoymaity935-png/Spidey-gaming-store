package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.components.SpideyBottomNav
import com.example.ui.components.SpideyTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroAnimationScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.OtpVerificationScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.ProductDetailDialog
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.SpideyBgLight
import com.example.ui.theme.SpideyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpideyTheme {
                val viewModel: MainViewModel = viewModel()
                SpideyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SpideyApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val cartCount by viewModel.cartTotalCount.collectAsState()
    val wishlistIds by viewModel.wishlist.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()

    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val localizedStrings by viewModel.localizedStrings.collectAsState()

    val userRole = currentUser?.role ?: UserRole.CUSTOMER
    val isStoreNavigationScreen = currentScreen in listOf(
        Screen.HOME,
        Screen.ORDERS,
        Screen.PROFILE,
        Screen.ADMIN
    )

    // Back button handling
    BackHandler(enabled = currentScreen != Screen.HOME && currentScreen != Screen.LOGIN && currentScreen != Screen.INTRO) {
        when (currentScreen) {
            Screen.OTP_VERIFY -> viewModel.navigateTo(Screen.LOGIN)
            Screen.CART, Screen.CHECKOUT, Screen.PAYMENT, Screen.WISHLIST, Screen.AI_CHAT -> viewModel.navigateTo(Screen.HOME)
            Screen.ORDERS, Screen.PROFILE, Screen.ADMIN -> viewModel.navigateTo(Screen.HOME)
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isStoreNavigationScreen) {
                SpideyTopBar(
                    title = localizedStrings.appName,
                    subtitle = when (currentScreen) {
                        Screen.HOME -> localizedStrings.tagline
                        Screen.ORDERS -> localizedStrings.myOrders.uppercase()
                        Screen.PROFILE -> localizedStrings.profile.uppercase()
                        Screen.ADMIN -> localizedStrings.admin.uppercase()
                        else -> localizedStrings.tagline
                    },
                    cartCount = cartCount,
                    wishlistCount = wishlistIds.size,
                    languageShortCode = currentLanguage.shortCode,
                    userRole = userRole,
                    onCartClick = { viewModel.navigateTo(Screen.CART) },
                    onWishlistClick = { viewModel.navigateTo(Screen.WISHLIST) },
                    onAiChatClick = { viewModel.navigateTo(Screen.AI_CHAT) },
                    onLanguageClick = { viewModel.setShowLanguageDialog(true) },
                    onLogoClick = { viewModel.navigateTo(Screen.HOME) }
                )
            }
        },
        bottomBar = {
            if (isStoreNavigationScreen) {
                SpideyBottomNav(
                    currentScreen = currentScreen,
                    userRole = userRole,
                    storeLabel = localizedStrings.all,
                    ordersLabel = localizedStrings.orders,
                    profileLabel = localizedStrings.profile,
                    adminLabel = localizedStrings.admin,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SpideyBgLight)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.INTRO -> IntroAnimationScreen(viewModel = viewModel)
                Screen.LOGIN -> LoginScreen(viewModel = viewModel)
                Screen.OTP_VERIFY -> OtpVerificationScreen(viewModel = viewModel)
                Screen.HOME -> HomeScreen(viewModel = viewModel)
                Screen.CART -> CartScreen(viewModel = viewModel)
                Screen.CHECKOUT -> CheckoutScreen(viewModel = viewModel)
                Screen.PAYMENT -> PaymentScreen(viewModel = viewModel)
                Screen.ORDERS -> OrdersScreen(viewModel = viewModel)
                Screen.PROFILE -> ProfileScreen(viewModel = viewModel)
                Screen.ADMIN -> AdminDashboardScreen(viewModel = viewModel)
                Screen.AI_CHAT -> AiChatScreen(viewModel = viewModel)
                Screen.WISHLIST -> WishlistScreen(viewModel = viewModel)
            }

            // Product Detail Dialog
            if (selectedProduct != null) {
                ProductDetailDialog(
                    product = selectedProduct!!,
                    onDismiss = { viewModel.selectProduct(null) },
                    onAddToCart = { product ->
                        viewModel.storeRepo.addToCart(product)
                    },
                    onBuyNow = { product ->
                        viewModel.storeRepo.addToCart(product)
                        viewModel.navigateTo(Screen.CART)
                    }
                )
            }

            // Indian Language Selector Dialog
            if (showLanguageDialog) {
                LanguageSelectorDialog(
                    currentLanguage = currentLanguage,
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onDismiss = { viewModel.setShowLanguageDialog(false) }
                )
            }
        }
    }
}
