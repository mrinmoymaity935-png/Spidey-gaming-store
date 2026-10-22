package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentStatus
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.QrCodeView
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
fun PaymentScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeOrder by viewModel.activePaymentOrder.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    var utrInput by remember { mutableStateOf(activeOrder?.paymentUtr ?: "") }
    var utrError by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(activeOrder?.paymentStatus == PaymentStatus.SUBMITTED || activeOrder?.paymentStatus == PaymentStatus.VERIFIED) }

    val scrollState = rememberScrollState()

    val order = activeOrder
    if (order == null) {
        Box(
            modifier = modifier.fillMaxSize().background(SpideyBgLight),
            contentAlignment = Alignment.Center
        ) {
            Text("No active order found.", color = SpideyTextSecondary)
        }
        return
    }

    // Build genuine standard UPI URI
    val upiUri = "upi://pay?pa=${settings.ownerUpiId}&pn=${Uri.encode(settings.merchantName)}&am=${order.totalAmountInr.toInt()}&cu=INR&tn=${Uri.encode("Order " + order.id)}"

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.ORDERS) },
                modifier = Modifier.testTag("payment_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = SpideyBlueDark
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "UPI Payment & QR",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order ID & Amount Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Order ID", fontSize = 13.sp, color = SpideyTextSecondary)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEAF2FF))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = order.id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SpideyElectric)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Total Payable Amount", fontSize = 12.sp, color = SpideyTextMuted)
                    Text(
                        text = "₹${order.totalAmountInr.toInt()}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyBlueDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recipient: ${order.targetRecipientInfo}",
                        fontSize = 12.sp,
                        color = SpideyTextSecondary
                    )
                }
            }

            // Scannable UPI QR Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.QrCode2, contentDescription = null, tint = SpideyRedPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Scan with any UPI App",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideyBlueDark
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // QR Code
                    QrCodeView(
                        content = upiUri,
                        size = 190.dp,
                        modifier = Modifier
                            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.2f))
                            .border(1.5.dp, Color(0xFFF0D5D7), RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Owner UPI ID Copy Bar
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF3F7FD))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", settings.ownerUpiId))
                                Toast.makeText(context, "UPI ID copied: ${settings.ownerUpiId}", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = settings.ownerUpiId,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SpideyBlueDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy UPI ID",
                            tint = SpideyElectric,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Direct UPI App Launch Button
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(upiUri)
                            }
                            try {
                                context.startActivity(Intent.createChooser(intent, "Pay via UPI App"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "No UPI app found on device. Please copy UPI ID or scan QR.", Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("launch_upi_app_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEAF2FF),
                            contentColor = SpideyElectric
                        )
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Open Google Pay / PhonePe / Paytm", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Step 2: UTR Reference Submission Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Submit 12-Digit UTR / Transaction ID",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyBlueDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "After sending ₹${order.totalAmountInr.toInt()}, enter the 12-digit UTR/Ref from your UPI app receipt below.",
                        fontSize = 12.sp,
                        color = SpideyTextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = utrInput,
                        onValueChange = {
                            val digits = it.filter { c -> c.isDigit() }.take(12)
                            utrInput = digits
                            if (digits.length == 12) utrError = null
                        },
                        label = { Text("12-Digit UPI Ref / UTR Number", fontSize = 12.sp) },
                        placeholder = { Text("e.g. 429104829102") },
                        singleLine = true,
                        isError = utrError != null,
                        supportingText = {
                            if (utrError != null) {
                                Text(text = utrError ?: "", color = Color(0xFFEF4444), fontSize = 11.sp)
                            } else {
                                Text(text = "${utrInput.length}/12 digits", fontSize = 11.sp, color = SpideyTextMuted)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("utr_input_field"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isSubmitted) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFDEF7EC))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SpideySuccess, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "UTR Submitted for Verification", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SpideySuccess)
                                Text(text = "The store owner is verifying payment. Status will update in 'My Orders'.", fontSize = 11.sp, color = Color(0xFF03543F))
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                if (utrInput.length != 12) {
                                    utrError = "Please enter all 12 digits of the UPI UTR"
                                    return@Button
                                }
                                viewModel.submitUtr(utrInput)
                                isSubmitted = true
                                Toast.makeText(context, "UTR submitted! Admin will verify and complete your order.", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.4f))
                                .testTag("submit_utr_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SpideyRedPrimary,
                                contentColor = Color.White
                            ),
                            enabled = utrInput.length == 12
                        ) {
                            Text(text = "Submit UTR for Verification", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // Security Policy Note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFECEE))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = SpideyRedPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Security Guarantee: SPIDEY never asks for your UPI PIN or bank OTP. Payments are made safely through your own UPI app.",
                    fontSize = 11.sp,
                    color = SpideyBlueDark,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
