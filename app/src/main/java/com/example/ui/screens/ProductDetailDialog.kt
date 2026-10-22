package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Product
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyElectric
import com.example.ui.theme.SpideyRedDark
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideySuccess
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary
import kotlinx.coroutines.delay

@Composable
fun ProductDetailDialog(
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onBuyNow: (Product) -> Unit
) {
    var viewingScreenshot by remember { mutableStateOf<String?>(null) }
    var showVideoPlayer by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (product.isGamingId) Color(0xFFFFF1F2) else Color(0xFFEAF2FF))
                            .border(
                                1.dp,
                                if (product.isGamingId) Color(0xFFFECDD3) else Color(0xFFBFDBFE),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (product.isGamingId) "🎮 VERIFIED GAMING ID" else product.category.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideyRedPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("dialog_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SpideyTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hero Graphic Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (product.isGamingId) {
                                Brush.linearGradient(
                                    listOf(Color(0xFF880808), SpideyRedDark, SpideyBlueDark)
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(Color(0xFFFFF1F2), Color(0xFFFFECEE))
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (product.iconType == "diamond" || product.category.contains("Diamond", ignoreCase = true)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_real_diamond),
                                contentDescription = product.name,
                                modifier = Modifier.size(64.dp)
                            )
                        } else {
                            Icon(
                                imageVector = if (product.isGamingId) Icons.Default.SportsEsports else getProductIcon(product.iconType),
                                contentDescription = product.name,
                                tint = if (product.isGamingId) Color.White else SpideyRedPrimary,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                        if (product.isGamingId) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${product.gameName} • ${product.serverRegion}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Product Title
                Text(
                    text = product.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyTextPrimary,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Price display
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${product.priceInr.toInt()}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyRedPrimary
                    )
                    if (product.originalPriceInr != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${product.originalPriceInr.toInt()}",
                            fontSize = 15.sp,
                            color = SpideyTextMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDEF7EC))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${product.discountPercent}% OFF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpideySuccess
                            )
                        }
                    }
                }

                // Gaming ID Specific Sections
                if (product.isGamingId) {
                    Spacer(modifier = Modifier.height(14.dp))

                    // Video Showcase Banner (if available)
                    if (product.videoUrl != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showVideoPlayer = true },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(listOf(SpideyRedPrimary, SpideyRedDark))
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(SpideyRedPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = "Play Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "VIDEO SHOWCASE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color.White.copy(alpha = 0.2f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(text = "1080p HD", fontSize = 8.sp, color = Color.White)
                                        }
                                    }
                                    Text(
                                        text = product.videoTitle ?: "Full Vault & Gun Skins Inspection Video",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }

                                Text(
                                    text = "Watch",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpideyElectric
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Account Specs Grid
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "ACCOUNT SPECIFICATIONS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideyBlueDark,
                            letterSpacing = 0.8.sp
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SpecItemBox(title = "Level", value = "Lv. ${product.accountLevel ?: "70+"}", modifier = Modifier.weight(1f))
                            SpecItemBox(title = "Rank Tier", value = product.rankTier ?: "Heroic", modifier = Modifier.weight(1f))
                            SpecItemBox(title = "Evo Guns", value = "${product.evoGunsCount ?: 0} Max/Up", modifier = Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SpecItemBox(title = "Login Method", value = product.loginMethod, modifier = Modifier.weight(1f))
                            SpecItemBox(title = "Region", value = product.serverRegion, modifier = Modifier.weight(1f))
                            if (product.accountUidPreview != null) {
                                SpecItemBox(title = "UID Preview", value = product.accountUidPreview, modifier = Modifier.weight(1f))
                            }
                        }

                        if (product.evoGunsHighlights != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFFBEB))
                                    .border(0.8.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(text = "Evo Guns Highlights:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                    Text(text = product.evoGunsHighlights, fontSize = 11.sp, color = Color(0xFF92400E))
                                }
                            }
                        }

                        if (product.rareBundles != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF0FDF4))
                                    .border(0.8.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(text = "Rare Bundles & Vault:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                    Text(text = product.rareBundles, fontSize = 11.sp, color = Color(0xFF166534))
                                }
                            }
                        }
                    }

                    // Screenshots Carousel / Grid
                    if (product.screenshots.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = SpideyRedPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "VERIFIED SCREENSHOTS (${product.screenshots.size})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpideyBlueDark,
                                    letterSpacing = 0.8.sp
                                )
                            }
                            Text(text = "Tap to view", fontSize = 10.sp, color = SpideyTextMuted)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            product.screenshots.forEachIndexed { index, screenshot ->
                                ScreenshotThumbnailCard(
                                    index = index + 1,
                                    caption = screenshot,
                                    onClick = { viewingScreenshot = screenshot }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    color = SpideyTextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Delivery & Security Features
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (product.isGamingId) Color(0xFFFFF1F2) else Color(0xFFF6FAFF))
                        .border(
                            1.dp,
                            if (product.isGamingId) Color(0xFFFECDD3) else Color(0xFFE2EDFB),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (product.isGamingId) Icons.Default.Key else if (product.instantDelivery) Icons.Default.Bolt else Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = product.deliveryInfo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SpideyBlueDark
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SpideySuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (product.isGamingId) {
                                "100% Safe Admin Mediation: Credentials handed over directly after UTR verification with full email change support."
                            } else {
                                "100% Genuine & Verified Top-Up via Owner UPI"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SpideyTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            onAddToCart(product)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("dialog_add_to_cart"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFF1F2),
                            contentColor = SpideyRedPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Add to Cart", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            onBuyNow(product)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("dialog_buy_now"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SpideyRedPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (product.isGamingId) "Buy Gaming ID" else "Buy Now",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    // Video Player Dialog (Simulated Video Playback with External Link)
    if (showVideoPlayer && product.videoUrl != null) {
        GamingIdVideoPlayerDialog(
            title = product.videoTitle ?: "Gaming ID Vault & Weapons Inspection",
            videoUrl = product.videoUrl!!,
            accountName = product.name,
            onDismiss = { showVideoPlayer = false }
        )
    }

    // Fullscreen Screenshot Viewer Dialog
    if (viewingScreenshot != null) {
        FullscreenScreenshotDialog(
            caption = viewingScreenshot!!,
            productName = product.name,
            onDismiss = { viewingScreenshot = null }
        )
    }
}

@Composable
fun SpecItemBox(title: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 9.sp, color = SpideyTextMuted)
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SpideyBlueDark,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ScreenshotThumbnailCard(
    index: Int,
    caption: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(135.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                )
            )
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SpideyRedPrimary)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(text = "#$index", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Icon(
                    imageVector = Icons.Default.ZoomIn,
                    contentDescription = "Expand",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = caption,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 3,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun FullscreenScreenshotDialog(
    caption: String,
    productName: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.5.dp, SpideyRedPrimary, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VERIFIED PROOF SCREENSHOT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Simulated high-detail gaming screenshot card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1E293B), Color(0xFF111827), Color(0xFF3B0764))
                            )
                        )
                        .border(1.dp, Color(0xFF475569), RoundedCornerShape(14.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = caption,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Authenticated by SPIDEY Admin • Single Owner",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = productName,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SpideyRedPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close Preview", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GamingIdVideoPlayerDialog(
    title: String,
    videoUrl: String,
    accountName: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var playbackProgress by remember { mutableFloatStateOf(0.15f) }

    // Simulate video playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500)
            if (playbackProgress >= 1f) {
                playbackProgress = 0f
            } else {
                playbackProgress += 0.05f
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.94f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE VIDEO INSPECTION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Simulated video screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF020617))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Video animation representation
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = if (isPlaying) SpideyRedPrimary else Color.Gray,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isPlaying) "Playing Vault Video Preview..." else "Video Paused",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "1080p 60fps • Inspection Mode",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    // Progress bar at bottom of video
                    LinearProgressIndicator(
                        progress = { playbackProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .align(Alignment.BottomCenter),
                        color = SpideyRedPrimary,
                        trackColor = Color(0xFF334155)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = accountName,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Player Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { isPlaying = !isPlaying }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = SpideyRedPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Text(
                            text = if (isPlaying) "Pause" else "Resume",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // External open button
                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Opening video: $videoUrl", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Open Link", fontSize = 11.sp, color = SpideyRedPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SpideyRedPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Account Details", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
