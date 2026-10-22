package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.SpideyRedPrimary

/**
 * Clean SPIDEY Winged "S" Logo (White & Light-Red combination).
 * Rendered without any circular ring or round border, allowing the sharp aerodynamic
 * wing silhouette to stand cleanly on its own.
 */
@Composable
fun SpideyLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showBadgeBackground: Boolean = false, // No circular red border
    elevation: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (elevation > 0.dp) {
                    Modifier.shadow(elevation, spotColor = SpideyRedPrimary.copy(alpha = 0.35f))
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_spidey_logo),
            contentDescription = "SPIDEY Wing Logo",
            modifier = Modifier.size(size)
        )
    }
}
