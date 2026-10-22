package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.Screen
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideyTextMuted

data class NavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun SpideyBottomNav(
    currentScreen: Screen,
    userRole: UserRole,
    storeLabel: String = "Store",
    ordersLabel: String = "Orders",
    profileLabel: String = "Profile",
    adminLabel: String = "Admin",
    onNavigate: (Screen) -> Unit
) {
    val items = buildList {
        add(
            NavItem(
                screen = Screen.HOME,
                label = storeLabel,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                testTag = "nav_home"
            )
        )
        add(
            NavItem(
                screen = Screen.ORDERS,
                label = ordersLabel,
                selectedIcon = Icons.Filled.ReceiptLong,
                unselectedIcon = Icons.Outlined.ReceiptLong,
                testTag = "nav_orders"
            )
        )
        add(
            NavItem(
                screen = Screen.PROFILE,
                label = profileLabel,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                testTag = "nav_profile"
            )
        )
        if (userRole == UserRole.ADMIN) {
            add(
                NavItem(
                    screen = Screen.ADMIN,
                    label = adminLabel,
                    selectedIcon = Icons.Filled.AdminPanelSettings,
                    unselectedIcon = Icons.Outlined.AdminPanelSettings,
                    testTag = "nav_admin"
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.12f))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onNavigate(item.screen) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag(item.testTag),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFFFFF1F2) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (isSelected) SpideyRedPrimary else SpideyTextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (isSelected) SpideyRedPrimary else SpideyTextMuted
                    )
                }
            }
        }
    }
}
