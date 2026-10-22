package com.example.ui.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FulfillmentStatus
import com.example.data.model.Order
import com.example.data.model.PaymentStatus
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.SpideyBgLight
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyCardBorder
import com.example.ui.theme.SpideyElectric
import com.example.ui.theme.SpideySuccess
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val selectedOrder by viewModel.selectedOrderDetail.collectAsState()
    val context = LocalContext.current

    // Filter orders for this user (or all if admin)
    val userOrders = if (currentUser?.role == com.example.data.model.UserRole.ADMIN) {
        orders
    } else {
        orders.filter { it.userId == currentUser?.id || it.customerPhone == currentUser?.phoneNumber }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpideyBgLight)
    ) {
        // WhatsApp Support Quick Action Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF25D366)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "WhatsApp Customer Support",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideyBlueDark
                        )
                        Text(
                            text = settings.supportWhatsapp,
                            fontSize = 11.sp,
                            color = SpideyTextSecondary
                        )
                    }
                }

                Button(
                    onClick = { openWhatsApp(context, settings.supportWhatsapp, "Hello SPIDEY Support, I need assistance with my order.") },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(36.dp).testTag("whatsapp_support_btn")
                ) {
                    Text("Chat", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (userOrders.isEmpty()) {
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
                            .background(Color(0xFFEAF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = SpideyElectric,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No Orders Yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your diamond top-ups, game passes, and purchases will appear here.",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.HOME) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpideyElectric, contentColor = Color.White),
                        modifier = Modifier.height(46.dp)
                    ) {
                        Text("Explore Store", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(userOrders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        onClick = { viewModel.selectOrderDetail(order) },
                        onPayNow = { viewModel.startPaymentForOrder(order) },
                        onSupport = {
                            openWhatsApp(
                                context,
                                settings.supportWhatsapp,
                                "Hello SPIDEY Support, inquiry regarding Order ID: ${order.id} (Amount: ₹${order.totalAmountInr.toInt()})"
                            )
                        }
                    )
                }
            }
        }
    }

    // Order Details Dialog
    if (selectedOrder != null) {
        OrderDetailDialog(
            order = selectedOrder!!,
            supportWhatsapp = settings.supportWhatsapp,
            onDismiss = { viewModel.selectOrderDetail(null) },
            onPayNow = {
                val o = selectedOrder!!
                viewModel.selectOrderDetail(null)
                viewModel.startPaymentForOrder(o)
            }
        )
    }
}

@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit,
    onPayNow: () -> Unit,
    onSupport: () -> Unit
) {
    val dateStr = remember(order.createdAt) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(order.createdAt))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFF0055FF).copy(alpha = 0.08f))
            .clickable { onClick() }
            .testTag("order_card_${order.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Order ID & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.id,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyBlueDark
                    )
                    if (order.isDemoOrder) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEAF2FF))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text("DEMO", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = SpideyElectric)
                        }
                    }
                }

                Text(text = dateStr, fontSize = 11.sp, color = SpideyTextMuted)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items list snippet
            order.items.forEach { item ->
                Text(
                    text = "• ${item.quantity}x ${item.productName}",
                    fontSize = 13.sp,
                    color = SpideyTextPrimary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Pills Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Payment Status Pill
                StatusPill(
                    label = order.paymentStatus.label,
                    color = when (order.paymentStatus) {
                        PaymentStatus.VERIFIED -> SpideySuccess
                        PaymentStatus.SUBMITTED -> SpideyElectric
                        PaymentStatus.PENDING -> Color(0xFFF59E0B)
                        PaymentStatus.FAILED -> Color(0xFFEF4444)
                    }
                )

                // Fulfillment Status Pill
                StatusPill(
                    label = order.fulfillmentStatus.label,
                    color = when (order.fulfillmentStatus) {
                        FulfillmentStatus.COMPLETED -> SpideySuccess
                        FulfillmentStatus.PROCESSING, FulfillmentStatus.CONFIRMED -> SpideyElectric
                        FulfillmentStatus.PENDING -> Color(0xFF6B7280)
                        FulfillmentStatus.CANCELLED -> Color(0xFFEF4444)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))
            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Row: Total & Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total Amount", fontSize = 11.sp, color = SpideyTextMuted)
                    Text(
                        text = "₹${order.totalAmountInr.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyBlueDark
                    )
                }

                if (order.paymentStatus == PaymentStatus.PENDING) {
                    Button(
                        onClick = onPayNow,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpideyElectric, contentColor = Color.White),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pay via UPI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onSupport() }
                    ) {
                        Text(text = "Order Details", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SpideyElectric)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = SpideyElectric, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatusPill(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun OrderDetailDialog(
    order: Order,
    supportWhatsapp: String,
    onDismiss: () -> Unit,
    onPayNow: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Order Summary", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SpideyTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Order ID: ${order.id}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SpideyElectric)
                Text(text = "Target Player UID: ${order.targetRecipientInfo}", fontSize = 13.sp, color = SpideyTextPrimary)

                if (order.paymentUtr.isNotBlank()) {
                    Text(text = "Submitted UTR: ${order.paymentUtr}", fontSize = 12.sp, color = SpideyTextSecondary)
                }

                if (order.adminNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF3F7FD))
                            .padding(8.dp)
                    ) {
                        Text(text = "Admin Note: ${order.adminNotes}", fontSize = 11.sp, color = SpideyBlueDark)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))
                Spacer(modifier = Modifier.height(14.dp))

                order.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${item.quantity}x ${item.productName}", fontSize = 13.sp, color = SpideyTextPrimary)
                        Text(text = "₹${item.totalInr.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Grand Total", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "₹${order.totalAmountInr.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = SpideyElectric)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            openWhatsApp(context, supportWhatsapp, "Inquiry for Order ID: ${order.id}")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    if (order.paymentStatus == PaymentStatus.PENDING) {
                        Button(
                            onClick = onPayNow,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SpideyElectric, contentColor = Color.White),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Pay via UPI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

fun openWhatsApp(context: Context, phone: String, message: String) {
    val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
    val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed. Contact phone: $phone", Toast.LENGTH_LONG).show()
    }
}
