package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideyTextPrimary

@Composable
fun SpideyTopBar(
    title: String = "SPIDEY",
    subtitle: String = "GAMING STORE",
    cartCount: Int = 0,
    wishlistCount: Int = 0,
    languageShortCode: String = "EN",
    userRole: UserRole = UserRole.CUSTOMER,
    onCartClick: () -> Unit = {},
    onWishlistClick: () -> Unit = {},
    onAiChatClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onLogoClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(3.dp, spotColor = SpideyRedPrimary.copy(alpha = 0.08f))
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Clean Wing S Logo (without red round border)
            SpideyLogo(
                size = 38.dp,
                showBadgeBackground = false,
                elevation = 0.dp,
                modifier = Modifier
                    .clickable { onLogoClick() }
                    .testTag("top_bar_logo")
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Branding titles
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = SpideyTextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    if (userRole == UserRole.ADMIN) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SpideyRedPrimary)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "ADMIN",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SpideyRedPrimary,
                    letterSpacing = 1.sp
                )
            }

            // Indian Language Switcher Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF0F2))
                    .border(1.dp, Color(0xFFFDDDE0), RoundedCornerShape(8.dp))
                    .clickable { onLanguageClick() }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag("top_bar_language_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language",
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = languageShortCode,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyRedPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // AI Guide Robot Button
            IconButton(
                onClick = onAiChatClick,
                modifier = Modifier.size(36.dp).testTag("top_bar_ai_chat_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "SPIDEY AI Guide",
                    tint = SpideyRedPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Wishlist Heart Button with Badge
            IconButton(
                onClick = onWishlistClick,
                modifier = Modifier.size(36.dp).testTag("top_bar_wishlist_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (wishlistCount > 0) {
                            Badge(
                                containerColor = SpideyRedPrimary,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = wishlistCount.toString(),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Wishlist",
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Cart Icon Button with Badge
            IconButton(
                onClick = onCartClick,
                modifier = Modifier.size(36.dp).testTag("top_bar_cart_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = SpideyRedPrimary,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping Cart",
                        tint = SpideyTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
