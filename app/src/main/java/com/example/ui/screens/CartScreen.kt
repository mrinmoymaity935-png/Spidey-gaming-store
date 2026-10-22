package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.CartItem
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.SpideyBgLight
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyCardBorder
import com.example.ui.theme.SpideyElectric
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideySuccess
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary

@Composable
fun CartScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cart.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    modifier = Modifier.testTag("cart_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SpideyBlueDark
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Shopping Cart (${cartItems.sumOf { it.quantity }})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyBlueDark
                )
            }

            if (cartItems.isNotEmpty()) {
                TextButton(
                    onClick = { viewModel.storeRepo.clearCart() },
                    modifier = Modifier.testTag("clear_cart_button")
                ) {
                    Text(
                        text = "Clear",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFEF4444)
                    )
                }
            }
        }

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEAF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RemoveShoppingCart,
                            contentDescription = "Empty Cart",
                            tint = SpideyElectric,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Cart is Empty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Add diamond packs, memberships, or gear to get started.",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.HOME) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SpideyElectric,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("browse_products_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Explore Store", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Cart Items List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems, key = { it.product.id }) { item ->
                    CartItemRow(
                        item = item,
                        onIncrement = { viewModel.storeRepo.updateCartQuantity(item.product.id, 1) },
                        onDecrement = { viewModel.storeRepo.updateCartQuantity(item.product.id, -1) },
                        onRemove = { viewModel.storeRepo.removeFromCart(item.product.id) }
                    )
                }
            }

            // Checkout Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), spotColor = Color(0xFF0055FF).copy(alpha = 0.12f)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Order Subtotal",
                            fontSize = 14.sp,
                            color = SpideyTextSecondary
                        )
                        Text(
                            text = "₹${subtotal.toInt()}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SpideyTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Processing Fee",
                            fontSize = 14.sp,
                            color = SpideyTextSecondary
                        )
                        Text(
                            text = "FREE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideySuccess
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFEFF3F8))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Payable Amount",
                                fontSize = 12.sp,
                                color = SpideyTextMuted
                            )
                            Text(
                                text = "₹${subtotal.toInt()}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SpideyBlueDark
                            )
                        }

                        Button(
                            onClick = { viewModel.navigateTo(Screen.CHECKOUT) },
                            modifier = Modifier
                                .height(50.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = SpideyElectric.copy(alpha = 0.4f))
                                .testTag("proceed_checkout_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SpideyElectric,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Proceed to Checkout",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            // Icon
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF0F2)),
                contentAlignment = Alignment.Center
            ) {
                if (item.product.iconType == "diamond" || item.product.category.contains("Diamond", ignoreCase = true)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_real_diamond),
                        contentDescription = null,
                        modifier = Modifier.size(34.dp)
                    )
                } else {
                    Icon(
                        imageVector = getProductIcon(item.product.iconType),
                        contentDescription = null,
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title & Price
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyTextPrimary,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${item.product.priceInr.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyBlueDark
                )
            }

            // Quantity stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF3F7FD))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                IconButton(
                    onClick = onDecrement,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (item.quantity == 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = SpideyTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = item.quantity.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyTextPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = SpideyElectric,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
