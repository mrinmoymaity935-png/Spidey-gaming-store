package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Product
import com.example.data.model.TrendingStatus
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.SpideyBgLight
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyCardBorder
import com.example.ui.theme.SpideyElectric
import com.example.ui.theme.SpideyRedDark
import com.example.ui.theme.SpideyRedGlow
import com.example.ui.theme.SpideyRedLight
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideySuccess
import com.example.ui.theme.SpideyWhite
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val trendingStatuses by viewModel.trendingStatuses.collectAsState()
    val selectedStatus by viewModel.selectedTrendingStatus.collectAsState()
    val wishlistIds by viewModel.wishlist.collectAsState()
    val localizedStrings by viewModel.localizedStrings.collectAsState()
    val focusManager = LocalFocusManager.current

    val categories = listOf("All", "Gaming IDs", "Diamonds", "Game Passes", "Gift Cards")
    val categoryDisplayNames = mapOf(
        "All" to localizedStrings.all,
        "Gaming IDs" to localizedStrings.gamingIds,
        "Diamonds" to localizedStrings.diamonds,
        "Game Passes" to localizedStrings.gamePasses,
        "Gift Cards" to localizedStrings.giftCards
    )

    val filteredProducts = products.filter { product ->
        !product.isHidden &&
                (selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)) &&
                (searchQuery.isBlank() || product.name.contains(searchQuery, ignoreCase = true) || product.description.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpideyBgLight)
    ) {
        // Owner Announcement Bar
        if (settings.announcementText.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(SpideyRedDark, SpideyBlueDark)
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = settings.announcementText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Trending Status / Ads Stories Bar
        if (trendingStatuses.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = localizedStrings.trendingAds,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpideyBlueDark,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Text(
                        text = localizedStrings.tapToView,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    trendingStatuses.forEach { status ->
                        StatusStoryBubble(
                            status = status,
                            onClick = { viewModel.selectTrendingStatus(status) }
                        )
                    }
                }
            }
        }

        // SPIDEY AI Assistant Quick Guide Banner (for new users)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFF1F2), Color(0xFFFFECEE))
                    )
                )
                .border(1.dp, Color(0xFFFECDD3), RoundedCornerShape(14.dp))
                .clickable { viewModel.navigateTo(Screen.AI_CHAT) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("home_ai_assistant_banner")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(SpideyRedPrimary, SpideyRedDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = localizedStrings.aiAssistant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyBlueDark
                    )
                    Text(
                        text = localizedStrings.aiSubtitle,
                        fontSize = 10.sp,
                        color = SpideyTextSecondary
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = SpideyRedPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Search Bar & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_input"),
                placeholder = {
                    Text(
                        text = localizedStrings.searchPlaceholder,
                        fontSize = 13.sp,
                        color = SpideyTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SpideyRedPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = SpideyTextMuted
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = SpideyRedPrimary,
                    unfocusedBorderColor = SpideyCardBorder
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category Chips Row with Localized Indian Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    val label = categoryDisplayNames[category] ?: category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) SpideyRedPrimary else Color.White)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) SpideyRedPrimary else SpideyCardBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.setSelectedCategory(category) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("category_chip_$category")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            color = if (isSelected) Color.White else SpideyTextSecondary
                        )
                    }
                }
            }
        }

        // Product Catalog Grid
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = SpideyTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No products found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try clearing your search query or selecting a different category.",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val isWishlisted = wishlistIds.contains(product.id)
                    ProductCard(
                        product = product,
                        isWishlisted = isWishlisted,
                        onToggleWishlist = { viewModel.toggleWishlist(product.id) },
                        onClick = { viewModel.selectProduct(product) },
                        onAddToCart = { viewModel.storeRepo.addToCart(product) }
                    )
                }
            }
        }
    }

    // Trending Status / Ad Dialog
    if (selectedStatus != null) {
        TrendingStatusDialog(
            status = selectedStatus!!,
            onDismiss = { viewModel.selectTrendingStatus(null) }
        )
    }
}

@Composable
fun StatusStoryBubble(
    status: TrendingStatus,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .width(68.dp)
    ) {
        // Gradient Ring around story avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(SpideyRedPrimary, SpideyRedGlow, SpideyWhite, SpideyRedPrimary)
                    )
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(status.bgGradientHex.first), Color(status.bgGradientHex.second))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (status.tag) {
                            "HOT DEAL" -> Icons.Default.LocalFireDepartment
                            "TRENDING" -> Icons.Default.Bolt
                            "STATUS" -> Icons.Default.AutoAwesome
                            else -> Icons.Default.CardGiftcard
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = status.title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = SpideyTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TrendingStatusDialog(
    status: TrendingStatus,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(status.bgGradientHex.first), Color(status.bgGradientHex.second))
                            )
                        )
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = status.tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = status.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Description & Promo Code
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = status.description,
                        fontSize = 14.sp,
                        color = SpideyTextPrimary,
                        lineHeight = 20.sp
                    )

                    if (status.promoCode != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF1F5F9))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                                .clickable {
                                    val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cb.setPrimaryClip(ClipData.newPlainText("Promo", status.promoCode))
                                    Toast.makeText(context, "Promo code copied: ${status.promoCode}", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Promo Code", fontSize = 10.sp, color = SpideyTextMuted)
                                Text(text = status.promoCode, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SpideyRedPrimary)
                            }
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = SpideyRedPrimary, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpideyRedPrimary)
                    ) {
                        Text("Awesome, Got it!", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    isWishlisted: Boolean,
    onToggleWishlist: () -> Unit,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.08f))
            .clickable { onClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SpideyCardBorder, Color(0xFFF8FAFC))))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Category/Badge icon area with Wishlist button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (product.isGamingId) {
                            Brush.linearGradient(
                                listOf(Color(0xFFFFF1F2), Color(0xFFFFECEE))
                            )
                        } else {
                            Brush.linearGradient(
                                listOf(Color(0xFFFDF2F2), Color(0xFFFFF7F7))
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Category icon / Real Diamond Image
                if (product.iconType == "diamond" || product.category.contains("Diamond", ignoreCase = true)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_real_diamond),
                        contentDescription = product.name,
                        modifier = Modifier.size(56.dp)
                    )
                } else {
                    Icon(
                        imageVector = getProductIcon(product.iconType),
                        contentDescription = product.name,
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(46.dp)
                    )
                }

                // Wishlist heart button top-left
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { onToggleWishlist() }
                        .testTag("wishlist_toggle_${product.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) SpideyRedPrimary else SpideyTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Badge top-right
                if (product.badge != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (product.isGamingId) SpideyRedPrimary else SpideyElectric)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Instant Delivery or Verified ID Tag bottom-left
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (product.isGamingId) SpideyRedDark.copy(alpha = 0.9f) else SpideyBlueDark.copy(alpha = 0.85f))
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (product.isGamingId) Icons.Default.Verified else Icons.Default.Bolt,
                        contentDescription = null,
                        tint = if (product.isGamingId) Color(0xFFFFD700) else Color(0xFF00E5FF),
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (product.isGamingId) "VERIFIED ID" else "INSTANT",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gaming ID Media Tags (Video & Screenshots count)
            if (product.isGamingId) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.videoUrl != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFF1F2))
                                .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = SpideyRedPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Video", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SpideyRedPrimary)
                        }
                    }

                    if (product.screenshots.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEFF6FF))
                                .border(0.8.dp, Color(0xFFBFDBFE), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                tint = SpideyRedPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("${product.screenshots.size} Photos", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SpideyRedPrimary)
                        }
                    }

                    if (product.accountLevel != null) {
                        Text(
                            text = "Lv.${product.accountLevel}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpideyBlueDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            // Product Title
            Text(
                text = product.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SpideyTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp,
                modifier = Modifier.height(34.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price Row & Add to Cart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "₹${product.priceInr.toInt()}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyRedPrimary
                    )
                    if (product.originalPriceInr != null) {
                        Text(
                            text = "₹${product.originalPriceInr.toInt()}",
                            fontSize = 11.sp,
                            color = SpideyTextMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Add to cart circular button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SpideyRedPrimary)
                        .clickable { onAddToCart() }
                        .testTag("add_cart_btn_${product.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

fun getProductIcon(iconType: String): ImageVector {
    return when (iconType.uppercase()) {
        "DIAMOND" -> Icons.Default.Diamond
        "PASS" -> Icons.Default.Verified
        "CARD" -> Icons.Default.CardGiftcard
        "GAMING_ID" -> Icons.Default.SportsEsports
        "CONTROLLER" -> Icons.Default.SportsEsports
        "HEADSET" -> Icons.Default.SportsEsports
        else -> Icons.Default.Star
    }
}
