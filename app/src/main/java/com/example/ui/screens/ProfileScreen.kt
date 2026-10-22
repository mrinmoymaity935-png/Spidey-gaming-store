package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.SpideyLogo
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
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val localizedStrings by viewModel.localizedStrings.collectAsState()
    val context = LocalContext.current

    val userOrders = orders.filter { it.userId == currentUser?.id || it.customerPhone == currentUser?.phoneNumber }
    val totalSpent = userOrders.filter { it.paymentStatus == com.example.data.model.PaymentStatus.VERIFIED }.sumOf { it.totalAmountInr }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showBackendSetupDialog by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpideyBgLight)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Clean Wing Avatar without red round border
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFFDDDE0), CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SpideyLogo(size = 64.dp, showBadgeBackground = false)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Name & Edit Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentUser?.displayName?.ifBlank { "Gamer" } ?: "Gamer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextPrimary
                    )
                    IconButton(
                        onClick = {
                            editedName = currentUser?.displayName ?: ""
                            showEditNameDialog = true
                        },
                        modifier = Modifier.size(28.dp).testTag("edit_name_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Name",
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Phone with verified badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = SpideyTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentUser?.phoneNumber ?: "",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = SpideySuccess,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Role Badge
                val isAdmin = currentUser?.role == UserRole.ADMIN
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isAdmin) Color(0xFF10B981) else Color(0xFFFFECEE))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isAdmin) "STORE OWNER (ADMIN)" else "VERIFIED CUSTOMER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAdmin) Color.White else SpideyRedPrimary
                    )
                }
            }
        }

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(
                title = "Total Orders",
                value = userOrders.size.toString(),
                subtitle = "Completed & Active",
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                title = "Total Spent",
                value = "₹${totalSpent.toInt()}",
                subtitle = "Verified Purchases",
                modifier = Modifier.weight(1f)
            )
        }

        // Actions & Controls List
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                // Change App Language (Indian Languages)
                ProfileItemRow(
                    icon = Icons.Default.Translate,
                    title = localizedStrings.changeLanguage,
                    subtitle = "${currentLanguage.nativeName} • ${currentLanguage.englishName}",
                    iconTint = SpideyRedPrimary,
                    badgeText = currentLanguage.shortCode,
                    onClick = { viewModel.setShowLanguageDialog(true) }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))

                // Toggle Admin Mode
                ProfileItemRow(
                    icon = Icons.Default.SwitchAccount,
                    title = "Toggle Role (Owner Admin / Customer)",
                    subtitle = if (currentUser?.role == UserRole.ADMIN) "Currently: Admin (Dashboard Unlocked)" else "Currently: Regular Customer",
                    badgeText = "TEST MODE",
                    onClick = {
                        viewModel.toggleAdminRole()
                        val newRole = if (currentUser?.role == UserRole.CUSTOMER) "Admin" else "Customer"
                        Toast.makeText(context, "Switched role to $newRole", Toast.LENGTH_SHORT).show()
                    }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))

                // My Wishlist
                ProfileItemRow(
                    icon = Icons.Filled.Favorite,
                    title = localizedStrings.myWishlist,
                    subtitle = "Saved favorite gaming IDs & diamond bundles",
                    iconTint = com.example.ui.theme.SpideyRedPrimary,
                    onClick = { viewModel.navigateTo(Screen.WISHLIST) }
                )


                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))

                // Production Firebase & Backend Setup Docs
                ProfileItemRow(
                    icon = Icons.Default.AdminPanelSettings,
                    title = "Backend & SMS Setup Guide",
                    subtitle = "Firebase Phone Auth, Firestore & UPI Integration",
                    onClick = { showBackendSetupDialog = true }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))

                // Privacy & Terms
                ProfileItemRow(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy & Store Terms",
                    subtitle = "Strict customer data protection",
                    onClick = { showPrivacyDialog = true }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFF3F8)))

                // Logout
                ProfileItemRow(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Sign Out",
                    subtitle = "Logout from current device session",
                    textColor = Color(0xFFEF4444),
                    iconTint = Color(0xFFEF4444),
                    onClick = { showLogoutConfirm = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }

    // Edit Name Dialog
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Update Display Name", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Gamer Tag / Display Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_edit_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(editedName)
                        showEditNameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SpideyRedPrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Log Out?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to end your current login session?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirm = false
                        viewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Backend Setup & Documentation Dialog
    if (showBackendSetupDialog) {
        BackendSetupDialog(onDismiss = { showBackendSetupDialog = false })
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 12.sp, color = SpideyTextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = SpideyBlueDark)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 10.sp, color = SpideyTextSecondary)
        }
    }
}

@Composable
fun ProfileItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badgeText: String? = null,
    textColor: Color = SpideyTextPrimary,
    iconTint: Color = SpideyRedPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                if (badgeText != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SpideyRedPrimary)
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(text = badgeText, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
            Text(text = subtitle, fontSize = 11.sp, color = SpideyTextSecondary)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = SpideyTextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun BackendSetupDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Production Backend Setup", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SpideyTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "1. Firebase Phone Authentication",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyRedPrimary
                )
                Text(
                    text = "• Enable Phone Auth in Firebase Console > Authentication > Sign-in method.\n" +
                            "• Add Android App Package: com.aistudio.spidey.kxvqzt\n" +
                            "• Register SHA-1 & SHA-256 signing fingerprints in Firebase Project Settings.\n" +
                            "• Download and place google-services.json in the /app root.",
                    fontSize = 12.sp,
                    color = SpideyTextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "2. Owner Admin Permissions",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyRedPrimary
                )
                Text(
                    text = "• In Firebase Auth / Cloud Firestore, set custom claims or document role: 'ADMIN' for the owner phone number.\n" +
                            "• Normal customers can never access or modify products, settings, or other customers' orders.",
                    fontSize = 12.sp,
                    color = SpideyTextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "3. UPI QR & Payment Verification",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyRedPrimary
                )
                Text(
                    text = "• Configure your verified merchant UPI ID in the Admin Dashboard > Settings.\n" +
                            "• The app automatically renders a scannable standard UPI QR with order ID and exact payable amount.\n" +
                            "• Admin verifies the 12-digit UTR in bank statements before marking orders as Completed.",
                    fontSize = 12.sp,
                    color = SpideyTextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpideyRedPrimary)
                ) {
                    Text("Got it")
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Privacy & Data Policy", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SpideyTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• Least-Privilege Data: SPIDEY collects only the mobile number, display gamer tag, and Player UID strictly required to fulfill gaming orders.\n\n" +
                            "• Payment Security: SPIDEY never collects UPI PINs, ATM cards, bank passwords, or login credentials. All payments occur safely within your registered UPI app.\n\n" +
                            "• Data Privacy: Customer orders and mobile numbers are confidential and visible only to the verified account holder and the authorized store admin.",
                    fontSize = 12.sp,
                    color = SpideyTextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpideyRedPrimary)
                ) {
                    Text("Close")
                }
            }
        }
    }
}
