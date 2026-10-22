package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.ui.theme.SpideyRedDark
import com.example.ui.theme.SpideyRedLight
import com.example.ui.theme.SpideyRedPrimary

/**
 * Premium White & Red Geometric Gaming Background.
 * Renders a crisp gaming white surface with soft red polygon facets
 * and delicate glowing crimson accents.
 */
@Composable
fun GeometricGamingBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFFFF7F7),
                        Color(0xFFFDF0F0),
                        Color(0xFFFCE9E9)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Top-Right soft red polygon shard
            val pathTopRight = Path().apply {
                moveTo(w * 0.62f, 0f)
                lineTo(w, 0f)
                lineTo(w, h * 0.22f)
                lineTo(w * 0.76f, h * 0.17f)
                close()
            }
            drawPath(
                path = pathTopRight,
                color = SpideyRedLight.copy(alpha = 0.55f)
            )

            // Top-Left subtle crimson facet
            val pathTopLeft = Path().apply {
                moveTo(0f, 0f)
                lineTo(w * 0.36f, 0f)
                lineTo(w * 0.18f, h * 0.12f)
                lineTo(0f, h * 0.16f)
                close()
            }
            drawPath(
                path = pathTopLeft,
                color = Color(0xFFFFECEE).copy(alpha = 0.6f)
            )

            // Mid-Right floating polygon
            val pathMidRight = Path().apply {
                moveTo(w, h * 0.42f)
                lineTo(w * 0.84f, h * 0.48f)
                lineTo(w * 0.88f, h * 0.60f)
                lineTo(w, h * 0.65f)
                close()
            }
            drawPath(
                path = pathMidRight,
                color = SpideyRedPrimary.copy(alpha = 0.05f)
            )

            // Bottom-Left angular polygon
            val pathBottomLeft = Path().apply {
                moveTo(0f, h * 0.70f)
                lineTo(w * 0.26f, h * 0.82f)
                lineTo(w * 0.12f, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = pathBottomLeft,
                color = Color(0xFFFFE0E2).copy(alpha = 0.45f)
            )

            // Delicate glowing red accent sphere behind top branding
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SpideyRedPrimary.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.16f),
                    radius = w * 0.45f
                ),
                center = Offset(w * 0.5f, h * 0.16f),
                radius = w * 0.45f
            )
        }

        content()
    }
}
