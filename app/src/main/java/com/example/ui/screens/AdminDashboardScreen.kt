package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FulfillmentStatus
import com.example.data.model.Order
import com.example.data.model.PaymentStatus
import com.example.data.model.Product
import com.example.data.model.StoreSettings
import com.example.ui.MainViewModel
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
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Products", "Orders", "Trending Ads", "Settings")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpideyBgLight)
    ) {
        // Tab Header
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = com.example.ui.theme.SpideyRedPrimary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = when (index) {
                                0 -> Icons.Default.Inventory2
                                1 -> Icons.Default.ReceiptLong
                                2 -> Icons.Default.CheckCircle // Will replace with fire
                                else -> Icons.Default.Settings
                            },
                            contentDescription = title,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> AdminProductsTab(viewModel)
            1 -> AdminOrdersTab(viewModel)
            2 -> AdminTrendingAdsTab(viewModel)
            3 -> AdminSettingsTab(viewModel)
        }
    }
}

// ==========================================
// 1. PRODUCTS MANAGEMENT TAB
// ==========================================
@Composable
fun AdminProductsTab(viewModel: MainViewModel) {
    val products by viewModel.products.collectAsState()
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var initialCategoryForAdd by remember { mutableStateOf("Gaming IDs") }
    var deleteCandidate by remember { mutableStateOf<Product?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    val categories = listOf("All", "Gaming IDs", "Diamonds", "Game Passes", "Gift Cards")
    val context = LocalContext.current

    val displayedProducts = products.filter {
        selectedCategoryFilter == "All" || it.category.equals(selectedCategoryFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add Product & Add Gaming ID Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    initialCategoryForAdd = "Gaming IDs"
                    showAddDialog = true
                },
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.4f))
                    .testTag("admin_add_gaming_id_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpideyRedPrimary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.SportsEsports, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "+ Gaming ID", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    initialCategoryForAdd = "Diamonds"
                    showAddDialog = true
                },
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = SpideyElectric.copy(alpha = 0.4f))
                    .testTag("admin_add_product_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpideyElectric,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "+ Diamond/Pass", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Category Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategoryFilter == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) SpideyBlueDark else Color.White)
                        .border(1.dp, if (isSelected) SpideyBlueDark else SpideyCardBorder, RoundedCornerShape(10.dp))
                        .clickable { selectedCategoryFilter = cat }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else SpideyTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize().padding(bottom = 70.dp)
        ) {
            items(displayedProducts, key = { it.id }) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (product.isGamingId) Color(0xFFFECDD3) else SpideyCardBorder
                        )
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
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (product.isGamingId) Color(0xFFFFF1F2) else Color(0xFFFFECEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (product.isGamingId) Icons.Default.SportsEsports else getProductIcon(product.iconType),
                                contentDescription = null,
                                tint = if (product.isGamingId) SpideyRedPrimary else SpideyElectric,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpideyTextPrimary,
                                    maxLines = 1
                                )
                                if (product.isHidden) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFEF4444).copy(alpha = 0.12f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("HIDDEN", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                                    }
                                }
                            }

                            // Badges row for video & screenshots
                            if (product.isGamingId) {
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (product.videoUrl != null) {
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFFFF1F2))
                                                .padding(horizontal = 4.dp, vertical = 1.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Videocam, contentDescription = null, tint = SpideyRedPrimary, modifier = Modifier.size(10.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text("Video", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SpideyRedPrimary)
                                        }
                                    }
                                    if (product.screenshots.isNotEmpty()) {
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFEFF6FF))
                                                .padding(horizontal = 4.dp, vertical = 1.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = SpideyElectric, modifier = Modifier.size(10.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text("${product.screenshots.size} Photos", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SpideyElectric)
                                        }
                                    }
                                }
                            }

                            Text(
                                text = "${product.category} • ₹${product.priceInr.toInt()} (Stock: ${product.stockCount})",
                                fontSize = 11.sp,
                                color = SpideyTextSecondary
                            )
                        }

                        // Actions
                        IconButton(
                            onClick = { viewModel.storeRepo.adminToggleProductVisibility(product.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (product.isHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle visibility",
                                tint = if (product.isHidden) Color(0xFFEF4444) else SpideySuccess,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { editingProduct = product },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Product",
                                tint = SpideyElectric,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { deleteCandidate = product },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Product",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Product Dialog
    if (showAddDialog || editingProduct != null) {
        val isEdit = editingProduct != null
        val target = editingProduct
        AddEditProductDialog(
            initialProduct = target,
            defaultCategory = initialCategoryForAdd,
            onDismiss = {
                showAddDialog = false
                editingProduct = null
            },
            onSave = { product ->
                if (isEdit) {
                    viewModel.storeRepo.adminUpdateProduct(product)
                    Toast.makeText(context, "Product updated", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.storeRepo.adminAddProduct(product)
                    Toast.makeText(context, "Product added to store", Toast.LENGTH_SHORT).show()
                }
                showAddDialog = false
                editingProduct = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (deleteCandidate != null) {
        AlertDialog(
            onDismissRequest = { deleteCandidate = null },
            title = { Text("Delete Product?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete '${deleteCandidate?.name}' from the store catalog?") },
            confirmButton = {
                Button(
                    onClick = {
                        val p = deleteCandidate
                        if (p != null) {
                            viewModel.storeRepo.adminDeleteProduct(p.id)
                            Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
                        }
                        deleteCandidate = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteCandidate = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AddEditProductDialog(
    initialProduct: Product?,
    defaultCategory: String = "Gaming IDs",
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var category by remember { mutableStateOf(initialProduct?.category ?: defaultCategory) }
    val isGamingId = category.equals("Gaming IDs", ignoreCase = true)

    var name by remember {
        mutableStateOf(
            initialProduct?.name ?: if (isGamingId) "FF Max Lv.75 Grandmaster ID (6 Evo Guns + Sakura)" else ""
        )
    }
    var priceStr by remember {
        mutableStateOf(initialProduct?.priceInr?.toInt()?.toString() ?: if (isGamingId) "2499" else "100")
    }
    var stockStr by remember {
        mutableStateOf(initialProduct?.stockCount?.toString() ?: if (isGamingId) "1" else "100")
    }
    var description by remember {
        mutableStateOf(
            initialProduct?.description ?: if (isGamingId) {
                "Verified single-owner competitive Free Fire Max account with maxed evo weapons, rare veteran bundles, clean history, and instant admin transfer."
            } else ""
        )
    }
    var instantDelivery by remember { mutableStateOf(initialProduct?.instantDelivery ?: true) }
    var iconType by remember {
        mutableStateOf(initialProduct?.iconType ?: if (isGamingId) "GAMING_ID" else "DIAMOND")
    }

    // Gaming ID Specific Fields
    var gameName by remember { mutableStateOf(initialProduct?.gameName ?: "Free Fire Max") }
    var accountLevelStr by remember { mutableStateOf(initialProduct?.accountLevel?.toString() ?: "75") }
    var rankTier by remember { mutableStateOf(initialProduct?.rankTier ?: "Grandmaster") }
    var evoGunsCountStr by remember { mutableStateOf(initialProduct?.evoGunsCount?.toString() ?: "6") }
    var evoGunsHighlights by remember {
        mutableStateOf(initialProduct?.evoGunsHighlights ?: "MP40 Cobra Lv7 (Max), AK Draco Lv7, M1014 Lv6")
    }
    var rareBundles by remember {
        mutableStateOf(initialProduct?.rareBundles ?: "Season 1 Sakura, Season 2 Hip Hop, Red Criminal")
    }
    var loginMethod by remember { mutableStateOf(initialProduct?.loginMethod ?: "Google (Full Email Access)") }
    var serverRegion by remember { mutableStateOf(initialProduct?.serverRegion ?: "India Server") }
    var videoUrl by remember {
        mutableStateOf(initialProduct?.videoUrl ?: "https://youtube.com/watch?v=spidey_ff_id_walkthrough")
    }
    var videoTitle by remember {
        mutableStateOf(initialProduct?.videoTitle ?: "Full Vault & Gun Skins Inspection Video")
    }
    var screenshots by remember {
        mutableStateOf(
            initialProduct?.screenshots ?: listOf(
                "Profile Stats: Level 75 Grandmaster, 14k Likes",
                "Weapon Vault: 6 Maxed Evo Guns Showcase",
                "Rare Bundles: Sakura Season 1 & Hip Hop",
                "Mythic Emotes: Pirate Flag & Throne Emote"
            )
        )
    }
    var newScreenshotInput by remember { mutableStateOf("") }

    val categories = listOf("Gaming IDs", "Diamonds", "Game Passes", "Gift Cards")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialProduct != null) "Edit Product" else "Add New Product",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyBlueDark
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SpideyTextMuted)
                    }
                }

                // Category Selector Chips
                Text(text = "Category", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SpideyTextSecondary)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = category.equals(cat, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) (if (cat == "Gaming IDs") SpideyRedPrimary else SpideyElectric) else Color(0xFFF1F5F9))
                                .clickable {
                                    category = cat
                                    if (cat == "Gaming IDs") {
                                        iconType = "GAMING_ID"
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else SpideyTextPrimary
                            )
                        }
                    }
                }

                // Product Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isGamingId) "Account Title (e.g. FF Max Lv.75 Grandmaster)" else "Product Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Price and Stock Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it.filter { c -> c.isDigit() } },
                        label = { Text("Price (₹)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = stockStr,
                        onValueChange = { stockStr = it.filter { c -> c.isDigit() } },
                        label = { Text(if (isGamingId) "Stock (IDs)" else "Stock Count") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                // ==========================================
                // GAMING ID SPECIAL CONTROLS (Video & Screenshots)
                // ==========================================
                if (isGamingId) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF1F2))
                            .border(1.dp, Color(0xFFFECDD3), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = null,
                                    tint = SpideyRedPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GAMING ACCOUNT DETAILS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpideyRedPrimary,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = gameName,
                                    onValueChange = { gameName = it },
                                    label = { Text("Game Name") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.2f)
                                )
                                OutlinedTextField(
                                    value = accountLevelStr,
                                    onValueChange = { accountLevelStr = it.filter { c -> c.isDigit() } },
                                    label = { Text("Level") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(0.8f)
                                )
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = rankTier,
                                    onValueChange = { rankTier = it },
                                    label = { Text("Rank (e.g. Grandmaster)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = evoGunsCountStr,
                                    onValueChange = { evoGunsCountStr = it.filter { c -> c.isDigit() } },
                                    label = { Text("Evo Guns Count") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = evoGunsHighlights,
                                onValueChange = { evoGunsHighlights = it },
                                label = { Text("Evo Guns Highlights (e.g. MP40 Cobra Lv7, AK Draco Lv7)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = rareBundles,
                                onValueChange = { rareBundles = it },
                                label = { Text("Rare Outfits & Bundles (e.g. Sakura S1, Hip Hop S2)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = loginMethod,
                                    onValueChange = { loginMethod = it },
                                    label = { Text("Login Type (Google/FB)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = serverRegion,
                                    onValueChange = { serverRegion = it },
                                    label = { Text("Server Region") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // --- VIDEO URL SECTION ---
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Videocam,
                                            contentDescription = null,
                                            tint = SpideyRedPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "VIDEO PREVIEW (YOUTUBE / CLIP URL)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SpideyBlueDark
                                        )
                                    }

                                    OutlinedTextField(
                                        value = videoUrl,
                                        onValueChange = { videoUrl = it },
                                        label = { Text("Video URL (e.g. YouTube clip)") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    OutlinedTextField(
                                        value = videoTitle,
                                        onValueChange = { videoTitle = it },
                                        label = { Text("Video Title / Description") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // --- SCREENSHOTS SECTION ---
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.PhotoLibrary,
                                                contentDescription = null,
                                                tint = SpideyElectric,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "ATTACHED SCREENSHOTS (${screenshots.size})",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SpideyBlueDark
                                            )
                                        }
                                    }

                                    // Existing screenshots list
                                    if (screenshots.isEmpty()) {
                                        Text(
                                            text = "No screenshots attached yet. Add custom proof or use quick presets below.",
                                            fontSize = 11.sp,
                                            color = SpideyTextMuted
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            screenshots.forEachIndexed { idx, shot ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color(0xFFF1F5F9))
                                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "#${idx + 1}: $shot",
                                                        fontSize = 11.sp,
                                                        color = SpideyTextPrimary,
                                                        modifier = Modifier.weight(1f),
                                                        maxLines = 1
                                                    )
                                                    IconButton(
                                                        onClick = {
                                                            screenshots = screenshots.filterIndexed { i, _ -> i != idx }
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = "Delete screenshot",
                                                            tint = Color(0xFFEF4444),
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Add custom screenshot row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = newScreenshotInput,
                                            onValueChange = { newScreenshotInput = it },
                                            placeholder = { Text("Screenshot title or URL", fontSize = 11.sp) },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Button(
                                            onClick = {
                                                if (newScreenshotInput.isNotBlank()) {
                                                    screenshots = screenshots + newScreenshotInput.trim()
                                                    newScreenshotInput = ""
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = SpideyElectric)
                                        ) {
                                            Text("+ Add", fontSize = 11.sp)
                                        }
                                    }

                                    // Quick Presets
                                    Text(text = "One-tap proof presets:", fontSize = 10.sp, color = SpideyTextMuted)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf(
                                            "+ Profile Stats & KD",
                                            "+ 6 Max Evo Guns Vault",
                                            "+ S1 Sakura & Hip Hop",
                                            "+ Mythic Fists & Emotes"
                                        ).forEach { preset ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFE0E7FF))
                                                    .clickable {
                                                        screenshots = screenshots + preset.removePrefix("+ ")
                                                    }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(text = preset, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // UID / Instant Delivery Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isGamingId) "Admin Mediated Safe Handover" else "Instant Delivery via UID",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = instantDelivery,
                        onCheckedChange = { instantDelivery = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = if (isGamingId) SpideyRedPrimary else SpideyElectric
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Buttons
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (name.isBlank()) return@Button
                            val p = Product(
                                id = initialProduct?.id ?: (if (isGamingId) "prod_id_${System.currentTimeMillis()}" else "prod_${System.currentTimeMillis()}"),
                                name = name.trim(),
                                category = category,
                                priceInr = priceStr.toDoubleOrNull() ?: (if (isGamingId) 2499.0 else 100.0),
                                inStock = (stockStr.toIntOrNull() ?: 0) > 0,
                                stockCount = stockStr.toIntOrNull() ?: (if (isGamingId) 1 else 100),
                                description = description.trim(),
                                instantDelivery = instantDelivery,
                                deliveryInfo = if (isGamingId) "Verified credentials handover via Admin mediation" else "Instant UID credit",
                                iconType = if (isGamingId) "GAMING_ID" else iconType,
                                isGamingId = isGamingId,
                                gameName = gameName.trim(),
                                accountLevel = accountLevelStr.toIntOrNull() ?: 70,
                                rankTier = rankTier.trim(),
                                evoGunsCount = evoGunsCountStr.toIntOrNull() ?: 0,
                                evoGunsHighlights = evoGunsHighlights.ifBlank { null },
                                rareBundles = rareBundles.ifBlank { null },
                                loginMethod = loginMethod.trim(),
                                serverRegion = serverRegion.trim(),
                                videoUrl = videoUrl.ifBlank { null },
                                videoTitle = videoTitle.ifBlank { null },
                                screenshots = screenshots,
                                badge = if (isGamingId) "VERIFIED ID" else null
                            )
                            onSave(p)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isGamingId) SpideyRedPrimary else SpideyElectric
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isGamingId) "Save Gaming ID" else "Save Product")
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. ORDERS MANAGEMENT TAB
// ==========================================
@Composable
fun AdminOrdersTab(viewModel: MainViewModel) {
    val orders by viewModel.orders.collectAsState()
    var selectedOrderForUpdate by remember { mutableStateOf<Order?>(null) }
    var filterStatus by remember { mutableStateOf("ALL") }

    val filteredOrders = orders.filter { order ->
        when (filterStatus) {
            "SUBMITTED" -> order.paymentStatus == PaymentStatus.SUBMITTED
            "PENDING" -> order.paymentStatus == PaymentStatus.PENDING
            "COMPLETED" -> order.fulfillmentStatus == FulfillmentStatus.COMPLETED
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Filter pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL", "SUBMITTED", "PENDING", "COMPLETED").forEach { f ->
                val isSelected = filterStatus == f
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) SpideyElectric else Color.White)
                        .border(1.dp, if (isSelected) SpideyElectric else SpideyCardBorder, RoundedCornerShape(8.dp))
                        .clickable { filterStatus = f }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (f == "SUBMITTED") "Needs Verification" else f,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else SpideyTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredOrders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No orders match this filter.", color = SpideyTextSecondary)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize().padding(bottom = 70.dp)
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOrderForUpdate = order },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = order.id, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                                Text(text = "₹${order.totalAmountInr.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = SpideyElectric)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Customer: ${order.customerName} (${order.customerPhone})", fontSize = 12.sp, color = SpideyTextPrimary)
                            Text(text = "Player UID: ${order.targetRecipientInfo}", fontSize = 12.sp, color = SpideyBlueDark, fontWeight = FontWeight.Medium)

                            if (order.paymentUtr.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Submitted UTR: ${order.paymentUtr}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0055D4)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusPill(label = order.paymentStatus.label, color = if (order.paymentStatus == PaymentStatus.VERIFIED) SpideySuccess else Color(0xFFF59E0B))
                                StatusPill(label = order.fulfillmentStatus.label, color = if (order.fulfillmentStatus == FulfillmentStatus.COMPLETED) SpideySuccess else SpideyElectric)
                            }
                        }
                    }
                }
            }
        }
    }

    // Update Status Dialog
    if (selectedOrderForUpdate != null) {
        AdminUpdateOrderDialog(
            order = selectedOrderForUpdate!!,
            onDismiss = { selectedOrderForUpdate = null },
            onSave = { payStatus, fulfillStatus, notes ->
                val o = selectedOrderForUpdate!!
                viewModel.storeRepo.adminUpdateOrderStatus(o.id, payStatus, fulfillStatus, notes)
                selectedOrderForUpdate = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUpdateOrderDialog(
    order: Order,
    onDismiss: () -> Unit,
    onSave: (PaymentStatus, FulfillmentStatus, String) -> Unit
) {
    var paymentStatus by remember { mutableStateOf(order.paymentStatus) }
    var fulfillmentStatus by remember { mutableStateOf(order.fulfillmentStatus) }
    var adminNotes by remember { mutableStateOf(order.adminNotes) }

    var expandedPay by remember { mutableStateOf(false) }
    var expandedFulfill by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Manage Order ${order.id}", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)
                Text(text = "Amount: ₹${order.totalAmountInr.toInt()} • UTR: ${order.paymentUtr.ifBlank { "Not yet submitted" }}", fontSize = 12.sp, color = SpideyTextSecondary)

                Spacer(modifier = Modifier.height(4.dp))

                // Payment Status Selector
                Text(text = "Payment Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                ExposedDropdownMenuBox(
                    expanded = expandedPay,
                    onExpandedChange = { expandedPay = !expandedPay }
                ) {
                    OutlinedTextField(
                        value = paymentStatus.label,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPay) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPay,
                        onDismissRequest = { expandedPay = false }
                    ) {
                        PaymentStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    paymentStatus = st
                                    expandedPay = false
                                }
                            )
                        }
                    }
                }

                // Fulfillment Status Selector
                Text(text = "Fulfillment Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                ExposedDropdownMenuBox(
                    expanded = expandedFulfill,
                    onExpandedChange = { expandedFulfill = !expandedFulfill }
                ) {
                    OutlinedTextField(
                        value = fulfillmentStatus.label,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFulfill) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFulfill,
                        onDismissRequest = { expandedFulfill = false }
                    ) {
                        FulfillmentStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    fulfillmentStatus = st
                                    expandedFulfill = false
                                }
                            )
                        }
                    }
                }

                // Admin Notes
                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = { adminNotes = it },
                    label = { Text("Admin Delivery Notes (Visible to Customer)") },
                    placeholder = { Text("e.g. Credited 310 Diamonds to UID 489201938") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { onSave(paymentStatus, fulfillmentStatus, adminNotes) },
                        colors = ButtonDefaults.buttonColors(containerColor = SpideyElectric),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update Order")
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. STORE & PAYMENT SETTINGS TAB
// ==========================================
@Composable
fun AdminSettingsTab(viewModel: MainViewModel) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    var upiId by remember { mutableStateOf(settings.ownerUpiId) }
    var merchantName by remember { mutableStateOf(settings.merchantName) }
    var supportWhatsapp by remember { mutableStateOf(settings.supportWhatsapp) }
    var supportEmail by remember { mutableStateOf(settings.supportEmail) }
    var announcement by remember { mutableStateOf(settings.announcementText) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(text = "Payment & Store Configuration", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SpideyBlueDark)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SpideyCardBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("Owner Genuine UPI ID (VPA)") },
                    placeholder = { Text("e.g. spidey.store@okhdfcbank") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("admin_upi_id_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = merchantName,
                    onValueChange = { merchantName = it },
                    label = { Text("Merchant / Store Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = supportWhatsapp,
                    onValueChange = { supportWhatsapp = it },
                    label = { Text("Support WhatsApp Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = supportEmail,
                    onValueChange = { supportEmail = it },
                    label = { Text("Support Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = announcement,
                    onValueChange = { announcement = it },
                    label = { Text("Announcement Banner Text") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        val updated = settings.copy(
                            ownerUpiId = upiId,
                            merchantName = merchantName,
                            supportWhatsapp = supportWhatsapp,
                            supportEmail = supportEmail,
                            announcementText = announcement
                        )
                        viewModel.storeRepo.adminUpdateSettings(updated)
                        Toast.makeText(context, "Store settings saved successfully!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_settings_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpideyElectric)
                ) {
                    Text("Save Settings", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

// ==========================================
// 4. TRENDING ADS / STATUS MANAGEMENT TAB
// ==========================================
@Composable
fun AdminTrendingAdsTab(viewModel: MainViewModel) {
    val statuses by viewModel.trendingStatuses.collectAsState()
    var showAddStatusDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showAddStatusDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = com.example.ui.theme.SpideyRedPrimary.copy(alpha = 0.4f))
                .testTag("admin_add_status_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = com.example.ui.theme.SpideyRedPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Post New Status / Trending Ad", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize().padding(bottom = 70.dp)
        ) {
            items(statuses, key = { it.id }) { status ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
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
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(status.bgGradientHex.first)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = status.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpideyTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(status.bgGradientHex.first).copy(alpha = 0.12f))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(status.tag, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(status.bgGradientHex.first))
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = status.description,
                                fontSize = 11.sp,
                                color = SpideyTextSecondary,
                                maxLines = 2
                            )
                            if (status.promoCode != null) {
                                Text(
                                    text = "Code: ${status.promoCode}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.SpideyRedPrimary
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                viewModel.storeRepo.adminDeleteStatus(status.id)
                                Toast.makeText(context, "Status removed", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Status",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddStatusDialog) {
        AddStatusDialog(
            onDismiss = { showAddStatusDialog = false },
            onSave = { newStatus ->
                viewModel.storeRepo.adminAddStatus(newStatus)
                Toast.makeText(context, "New Status posted to Store!", Toast.LENGTH_SHORT).show()
                showAddStatusDialog = false
            }
        )
    }
}

@Composable
fun AddStatusDialog(
    onDismiss: () -> Unit,
    onSave: (com.example.data.model.TrendingStatus) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("HOT DEAL") }
    var description by remember { mutableStateOf("") }
    var promoCode by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Post Trending Ad / Status",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyBlueDark
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Headline / Status Title") },
                    placeholder = { Text("e.g. 100% Diamond Drop") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it.uppercase() },
                    label = { Text("Tag (HOT DEAL / TRENDING / STATUS)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Offer Description") },
                    placeholder = { Text("e.g. Double diamonds on all packs till 10 PM") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = promoCode,
                    onValueChange = { promoCode = it.uppercase() },
                    label = { Text("Promo Code (Optional)") },
                    placeholder = { Text("e.g. SPIDEY50") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (title.isBlank()) return@Button
                            val st = com.example.data.model.TrendingStatus(
                                id = "stat_${System.currentTimeMillis()}",
                                title = title,
                                tag = tag.ifBlank { "TRENDING" },
                                description = description,
                                promoCode = promoCode.ifBlank { null }
                            )
                            onSave(st)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.SpideyRedPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Post Status")
                    }
                }
            }
        }
    }
}

