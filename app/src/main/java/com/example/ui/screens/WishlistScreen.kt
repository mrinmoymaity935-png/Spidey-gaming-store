package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.SpideyBgLight
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyCardBorder
import com.example.ui.theme.SpideyElectric
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary

@Composable
fun WishlistScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val wishlistIds by viewModel.wishlist.collectAsState()
    val allProducts by viewModel.products.collectAsState()
    val wishlistProducts = allProducts.filter { it.id in wishlistIds }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpideyBgLight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .shadow(2.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.HOME) },
                modifier = Modifier.testTag("wishlist_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = SpideyBlueDark
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "My Wishlist (${wishlistProducts.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SpideyBlueDark
            )
        }

        if (wishlistProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Empty Wishlist",
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Wishlist is Empty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap the heart icon on any game pack or gear to save it for later.",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.HOME) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SpideyRedPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(46.dp)
                    ) {
                        Text(text = "Explore Store", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wishlistProducts, key = { it.id }) { product ->
                    WishlistItemCard(
                        product = product,
                        onAddToCart = {
                            viewModel.storeRepo.addToCart(product)
                        },
                        onRemove = {
                            viewModel.toggleWishlist(product.id)
                        },
                        onClick = {
                            viewModel.selectProduct(product)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistItemCard(
    product: Product,
    onAddToCart: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF0F2)),
                contentAlignment = Alignment.Center
            ) {
                if (product.iconType == "diamond" || product.category.contains("Diamond", ignoreCase = true)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_real_diamond),
                        contentDescription = null,
                        modifier = Modifier.size(34.dp)
                    )
                } else {
                    Icon(
                        imageVector = getProductIcon(product.iconType),
                        contentDescription = null,
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyTextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${product.priceInr.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyRedPrimary
                )
            }

            // Remove heart
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from Wishlist",
                    tint = SpideyRedPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Add to Cart
            IconButton(onClick = onAddToCart) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Add to Cart",
                    tint = SpideyElectric,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
