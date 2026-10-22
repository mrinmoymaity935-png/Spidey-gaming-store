package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun CheckoutScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val cartItems by viewModel.cart.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()

    var customerName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var recipientInfo by remember { mutableStateOf("") }
    var recipientError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpideyBgLight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.CART) },
                modifier = Modifier.testTag("checkout_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = SpideyBlueDark
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Checkout Order",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SpideyBlueDark
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Recipient Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Customer & Delivery Information",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyBlueDark
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Verified Phone Number (Read-only)
                    OutlinedTextField(
                        value = currentUser?.phoneNumber ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Verified Mobile Number", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = SpideyElectric)
                        },
                        trailingIcon = {
                            Icon(Icons.Default.Bolt, contentDescription = "Verified", tint = SpideySuccess)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF7FAFD),
                            focusedContainerColor = Color(0xFFF7FAFD)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gamer Name / Display Name
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = {
                            customerName = it
                            viewModel.updateProfile(it)
                        },
                        label = { Text("Gamer Name / Full Name", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SpideyElectric)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Player UID / Game ID / Shipping Address
                    OutlinedTextField(
                        value = recipientInfo,
                        onValueChange = {
                            recipientInfo = it
                            if (it.isNotBlank()) recipientError = null
                        },
                        label = { Text("Player Game UID / Recipient Info", fontSize = 12.sp) },
                        placeholder = { Text("e.g. Free Fire UID: 489201938 or Address") },
                        leadingIcon = {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = SpideyRedPrimary)
                        },
                        singleLine = false,
                        maxLines = 2,
                        isError = recipientError != null,
                        supportingText = {
                            if (recipientError != null) {
                                Text(text = recipientError ?: "", color = Color(0xFFEF4444), fontSize = 11.sp)
                            } else {
                                Text("Diamonds and Passes will be credited to this UID", fontSize = 11.sp, color = SpideyTextMuted)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_recipient_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }
            }

            // Order Items Overview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Order Summary (${cartItems.size} items)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyBlueDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.product.name}",
                                fontSize = 13.sp,
                                color = SpideyTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "₹${item.totalInr.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpideyBlueDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Payable Amount", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                        Text(text = "₹${subtotal.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = SpideyRedPrimary)
                    }
                }
            }

            // Information Note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFECEE))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = SpideyRedPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Placing this order will generate an official Order ID and present the owner's genuine UPI QR code and payment apps.",
                    fontSize = 12.sp,
                    color = SpideyTextSecondary,
                    lineHeight = 16.sp
                )
            }
        }

        // Bottom Action
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .shadow(8.dp)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    if (recipientInfo.isBlank()) {
                        recipientError = "Please enter your Player Game UID or address"
                        return@Button
                    }
                    isSubmitting = true
                    viewModel.checkout(targetRecipient = recipientInfo)
                    isSubmitting = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.5f))
                    .testTag("confirm_order_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpideyRedPrimary,
                    contentColor = Color.White
                ),
                enabled = !isSubmitting && cartItems.isNotEmpty()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Place Order & Pay ₹${subtotal.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
