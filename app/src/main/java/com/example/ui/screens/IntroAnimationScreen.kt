package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MainViewModel
import com.example.ui.components.SpideyLogo
import com.example.ui.theme.SpideyRedDark
import com.example.ui.theme.SpideyRedGlow
import com.example.ui.theme.SpideyRedLight
import com.example.ui.theme.SpideyRedPrimary
import com.example.ui.theme.SpideyWhite
import kotlinx.coroutines.delay

/**
 * Authentic LG OLED / WebOS TV Style Boot Opening Animation.
 * Starts with an intense center optical laser beam expanding horizontally,
 * aperture expanding vertically to power on the display, followed by an LG-style
 * light sweep revealing the clean Winged "S" logo and bold "SPIDEY FF STORE" typography.
 */
@Composable
fun IntroAnimationScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    // Stage 1: Horizontal TV beam expansion
    val tvBeamWidth = remember { Animatable(0.02f) }
    val tvBeamHeight = remember { Animatable(0.003f) }
    val tvFlashAlpha = remember { Animatable(0f) }

    // Stage 2: Logo and Brand Reveal
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val progressAnim = remember { Animatable(0f) }

    // LG TV style orbital spark rotation & pulse
    val infiniteTransition = rememberInfiniteTransition(label = "lg_tv_spark")
    val sparkPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkPulse"
    )

    LaunchedEffect(Unit) {
        // Step 1: Laser beam ignition across screen (LG TV OLED power-on slit)
        tvFlashAlpha.animateTo(1f, tween(150))
        tvBeamWidth.animateTo(1f, tween(320, easing = FastOutSlowInEasing))

        // Step 2: Screen aperture opens vertically
        tvBeamHeight.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        tvFlashAlpha.animateTo(0.15f, tween(300))

        // Step 3: Logo swooshes into view with spring effect
        logoAlpha.animateTo(1f, tween(300))
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = 350f)
        )

        // Step 4: SPIDEY FF STORE bold typography flashes in
        textAlpha.animateTo(1f, tween(350, easing = LinearEasing))

        // Step 5: WebOS loading bar sweep
        progressAnim.animateTo(1f, tween(1100, easing = FastOutSlowInEasing))

        // Step 6: Power on complete -> Enter Store
        delay(250)
        viewModel.onIntroFinish()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0304)) // Deep stealth OLED black
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        // LG TV Optical Horizontal Power Beam
        if (tvBeamHeight.value < 0.98f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(tvBeamWidth.value)
                    .height((maxHeight * tvBeamHeight.value).coerceAtLeast(3.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color.White,
                                SpideyRedGlow,
                                SpideyRedPrimary.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // Skip Button top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1F080A).copy(alpha = 0.85f))
                .border(1.dp, SpideyRedPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .clickable { viewModel.onIntroFinish() }
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .testTag("skip_intro_btn")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Skip", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SpideyWhite)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = SpideyRedPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Center Content: LG TV Style Brand Presentation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Winged "S" Logo without round red circle (clean silhouette)
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale.value * sparkPulse)
                    .alpha(logoAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                // Subtle radial light glow behind the logo
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    SpideyRedPrimary.copy(alpha = 0.45f),
                                    SpideyRedGlow.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Clean Logo without any red circular ring
                SpideyLogo(
                    size = 110.dp,
                    showBadgeBackground = false
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bold Typography: "SPIDEY FF STORE" (LG TV Style Title Reveal)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha.value)
            ) {
                Text(
                    text = "SPIDEY FF STORE",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    color = SpideyWhite,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // High-tech TV status pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF22090C))
                        .border(1.dp, SpideyRedPrimary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = null,
                        tint = SpideyRedPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PREMIUM FREE FIRE STORE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpideyRedLight,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // LG TV / WebOS Style Sleek Loader Bar
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF280B0F))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scaleX = progressAnim.value, scaleY = 1f)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    SpideyRedDark,
                                    SpideyRedPrimary,
                                    SpideyWhite
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = SpideyRedPrimary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Booting Gaming Store...",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = SpideyWhite.copy(alpha = 0.65f),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
