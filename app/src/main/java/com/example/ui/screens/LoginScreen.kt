package com.example.ui.screens

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MainViewModel
import com.example.ui.components.GeometricGamingBackground
import com.example.ui.components.SpideyLogo
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyRedLight
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authUiState.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val localizedStrings by viewModel.localizedStrings.collectAsState()
    val scrollState = rememberScrollState()

    GeometricGamingBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Language selector button at the top-right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .border(1.dp, SpideyRedPrimary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .clickable { viewModel.setShowLanguageDialog(true) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("login_language_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = SpideyRedPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = currentLanguage.nativeName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideyRedPrimary
                        )
                    }
                }
            }

            // Top Section: Logo, Branding, Welcome text, Phone input, Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Sleek Red & White SPIDEY wing logo (Clean, no red round border)
                SpideyLogo(size = 96.dp, showBadgeBackground = false)

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Brand text "SPIDEY" with small "GAMING STORE" subtitle
                Text(
                    text = localizedStrings.appName,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SpideyBlueDark,
                    letterSpacing = 2.sp
                )

                Text(
                    text = localizedStrings.tagline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyRedPrimary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // 3. Heading: "Welcome Back!"
                Text(
                    text = localizedStrings.welcomeBack,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SpideyTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 4. Subtitle: "Sign in to continue"
                Text(
                    text = localizedStrings.signInSubtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SpideyTextSecondary
                )

                Spacer(modifier = Modifier.height(26.dp))

                // 5. Rounded mobile-number field with Indian flag, +91 country code, and placeholder
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = SolidColor(if (authState.phoneError != null) Color(0xFFEF4444) else Color(0xFFF0D5D7))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Indian Flag
                        Image(
                            painter = painterResource(id = R.drawable.ic_flag_in),
                            contentDescription = "India Country Flag",
                            modifier = Modifier
                                .size(width = 24.dp, height = 16.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // +91 Code
                        Text(
                            text = "+91",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SpideyTextPrimary
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Subtle vertical separator
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(22.dp)
                                .background(Color(0xFFD9E5F5))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Editable phone field
                        Box(modifier = Modifier.weight(1f)) {
                            if (authState.phoneNumberInput.isEmpty()) {
                                Text(
                                    text = localizedStrings.enterMobile,
                                    fontSize = 15.sp,
                                    color = SpideyTextMuted
                                )
                            }
                            BasicTextField(
                                value = authState.phoneNumberInput,
                                onValueChange = { viewModel.onPhoneNumberChange(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_phone_input"),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (authState.phoneNumberInput.length == 10 && !authState.isLoading) {
                                            viewModel.sendOtp()
                                        }
                                    }
                                ),
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpideyTextPrimary,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }
                }

                // Error message if any
                if (authState.phoneError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = authState.phoneError ?: "",
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 6. Large rounded button labeled "Send OTP"
                Button(
                    onClick = { viewModel.sendOtp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(12.dp, RoundedCornerShape(18.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.4f))
                        .testTag("send_otp_button"),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SpideyRedPrimary,
                        contentColor = Color.White
                    ),
                    enabled = !authState.isLoading && authState.phoneNumberInput.length == 10
                ) {
                    if (authState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            text = localizedStrings.sendOtp,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 7. Shield icon and text "Secure OTP verification"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Security Shield",
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = localizedStrings.secureOtp,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Demo Notice Badge with 1-tap quick fill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SpideyRedLight.copy(alpha = 0.7f))
                        .border(1.dp, SpideyRedPrimary.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .clickable { viewModel.onPhoneNumberChange("9876543210") }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Demo Info",
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "⚡ Tap to Quick-Fill: 9876543210",
                        fontSize = 12.sp,
                        color = SpideyRedPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 8. Subtle, low-opacity blue gaming-controller illustration near the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_gaming_controller),
                    contentDescription = "Subtle Gaming Controller Illustration",
                    modifier = Modifier
                        .size(width = 170.dp, height = 95.dp)
                        .alpha(0.20f)
                )
            }
        }
    }
}
