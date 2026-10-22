package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.GeometricGamingBackground
import com.example.ui.theme.SpideyBlueDark
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideySuccess
import com.example.ui.theme.SpideyTextMuted
import com.example.ui.theme.SpideyTextPrimary
import com.example.ui.theme.SpideyTextSecondary

@Composable
fun OtpVerificationScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authUiState.collectAsState()
    val scrollState = rememberScrollState()

    GeometricGamingBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Nav Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.LOGIN) },
                    modifier = Modifier.testTag("otp_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Login",
                        tint = SpideyBlueDark
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Verification",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpideyBlueDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Lock Icon Badge
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFFFECEE))
                    .border(1.5.dp, SpideyRedPrimary.copy(alpha = 0.35f), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "OTP Lock",
                    tint = SpideyRedPrimary,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Enter 6-Digit Code",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SpideyTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Sent to ${authState.pendingPhoneFormatted.ifEmpty { "+91 ${authState.phoneNumberInput}" }}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpideyBlueDark
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.LOGIN) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit phone number",
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent 1-Tap Auto-Fill Test Button (Solves OTP typing problems immediately!)
            Button(
                onClick = {
                    viewModel.onOtpDigitChange("123456")
                    viewModel.verifyOtp("123456")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("one_tap_autofill_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDEF7EC),
                    contentColor = SpideySuccess
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = SpideySuccess,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Auto-Fill & Verify (123456)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 Styled Digit Boxes (Visual Representation)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 0 until 6) {
                    val digit = authState.otpInput.getOrNull(i)?.toString() ?: ""
                    val isCurrent = authState.otpInput.length == i
                    val hasValue = digit.isNotEmpty()

                    Box(
                        modifier = Modifier
                            .size(width = 46.dp, height = 54.dp)
                            .shadow(
                                elevation = if (isCurrent) 6.dp else 2.dp,
                                shape = RoundedCornerShape(14.dp),
                                spotColor = SpideyRedPrimary.copy(alpha = 0.25f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .border(
                                width = if (isCurrent) 2.dp else 1.dp,
                                color = when {
                                    authState.otpError != null -> Color(0xFFEF4444)
                                    isCurrent -> SpideyRedPrimary
                                    hasValue -> SpideyBlueDark
                                    else -> Color(0xFFF0D5D7)
                                },
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = digit,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpideyTextPrimary
                        )
                    }
                }
            }

            // Real Direct Input Field for keyboard typing & copy-paste
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = authState.otpInput,
                onValueChange = { viewModel.onOtpDigitChange(it) },
                label = { Text("Or Type / Paste 6-Digit Code Here", fontSize = 11.sp) },
                placeholder = { Text("e.g. 123456") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("otp_input_field"),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // OTP Error display
            if (authState.otpError != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = authState.otpError ?: "",
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Verify Button
            Button(
                onClick = { viewModel.verifyOtp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = SpideyRedPrimary.copy(alpha = 0.5f))
                    .testTag("verify_otp_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpideyRedPrimary,
                    contentColor = Color.White
                ),
                enabled = !authState.isLoading && authState.otpInput.length == 6
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Verify & Enter Store",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Resend Countdown Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authState.canResend) {
                    Text(
                        text = "Didn't receive code?",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = { viewModel.resendOtp() },
                        modifier = Modifier.testTag("resend_otp_button")
                    ) {
                        Text(
                            text = "Resend OTP",
                            color = SpideyRedPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Text(
                        text = "Resend OTP in ",
                        fontSize = 13.sp,
                        color = SpideyTextSecondary
                    )
                    Text(
                        text = "${authState.resendCountdownSec}s",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpideyRedPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // On-Screen Numeric Keypad (Eliminates virtual keyboard dependency in streaming emulators)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, Color(0xFFD6E4F7), RoundedCornerShape(16.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("C", "0", "⌫")
                )

                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { key ->
                            Box(
                                modifier = Modifier
                                    .size(width = 82.dp, height = 44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when (key) {
                                            "C" -> Color(0xFFFEE2E2)
                                            "⌫" -> Color(0xFFF1F5F9)
                                            else -> Color(0xFFF3F7FD)
                                        }
                                    )
                                    .clickable {
                                        when (key) {
                                            "C" -> viewModel.onOtpDigitChange("")
                                            "⌫" -> {
                                                if (authState.otpInput.isNotEmpty()) {
                                                    viewModel.onOtpDigitChange(authState.otpInput.dropLast(1))
                                                }
                                            }
                                            else -> {
                                                if (authState.otpInput.length < 6) {
                                                    val updated = authState.otpInput + key
                                                    viewModel.onOtpDigitChange(updated)
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (key == "⌫") {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                                        contentDescription = "Backspace",
                                        tint = SpideyTextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = key,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (key) {
                                            "C" -> Color(0xFFDC2626)
                                            else -> SpideyTextPrimary
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
