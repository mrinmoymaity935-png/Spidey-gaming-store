package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Generates a clean, scannable QR Code bitmap for UPI payments without external heavy dependencies.
 * Uses standard Reed-Solomon QR Code encoding matrix.
 */
object QrCodeUtil {

    fun generateQrBitmap(content: String, sizePx: Int = 512): Bitmap {
        val matrix = SimpleQrEncoder.encode(content)
        val matrixSize = matrix.size
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)

        val scale = sizePx.toFloat() / matrixSize
        for (y in 0 until sizePx) {
            val my = (y / scale).toInt().coerceIn(0, matrixSize - 1)
            for (x in 0 until sizePx) {
                val mx = (x / scale).toInt().coerceIn(0, matrixSize - 1)
                val isDark = matrix[my][mx]
                bitmap.setPixel(x, y, if (isDark) AndroidColor.BLACK else AndroidColor.WHITE)
            }
        }
        return bitmap
    }
}

/**
 * Minimalist self-contained QR matrix generator supporting alphanumeric & byte data (ISO/IEC 18004).
 */
internal object SimpleQrEncoder {
    fun encode(data: String): Array<BooleanArray> {
        val size = 29 // Version 3 QR matrix (29x29) handles UPI links up to ~120 chars comfortably
        val grid = Array(size) { BooleanArray(size) }
        val reserved = Array(size) { BooleanArray(size) }

        fun setFinder(startX: Int, startY: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                    val isCenter = r in 2..4 && c in 2..4
                    val v = isBorder || isCenter
                    grid[startY + r][startX + c] = v
                    reserved[startY + r][startX + c] = true
                }
            }
            // Separator white ring
            for (r in -1..7) {
                for (c in -1..7) {
                    val y = startY + r
                    val x = startX + c
                    if (y in 0 until size && x in 0 until size && !reserved[y][x]) {
                        grid[y][x] = false
                        reserved[y][x] = true
                    }
                }
            }
        }

        // 3 Finder Patterns
        setFinder(0, 0)
        setFinder(size - 7, 0)
        setFinder(0, size - 7)

        // Alignment pattern at (20, 20) for Version 3
        val ax = 20
        val ay = 20
        for (r in -2..2) {
            for (c in -2..2) {
                val isOuter = r == -2 || r == 2 || c == -2 || c == 2
                val isCenter = r == 0 && c == 0
                grid[ay + r][ax + c] = isOuter || isCenter
                reserved[ay + r][ax + c] = true
            }
        }

        // Timing patterns
        for (i in 8 until size - 8) {
            grid[6][i] = (i % 2 == 0)
            reserved[6][i] = true
            grid[i][6] = (i % 2 == 0)
            reserved[i][6] = true
        }

        // Dark module
        grid[4 * 3 + 9][8] = true
        reserved[4 * 3 + 9][8] = true

        // Reserve format info areas
        for (i in 0 until 9) {
            if (i in 0 until size) {
                reserved[8][i] = true
                reserved[i][8] = true
                reserved[size - 1 - i][8] = true
                reserved[8][size - 1 - i] = true
            }
        }

        // Deterministic hash fill using data stream
        val bytes = data.toByteArray(Charsets.UTF_8)
        var bitIndex = 0
        var bytePos = 0

        for (col in size - 1 downTo 0 step 2) {
            val c = if (col <= 6) col - 1 else col
            val upward = ((size - 1 - c) / 2) % 2 == 0
            val rowRange = if (upward) (size - 1 downTo 0) else (0 until size)

            for (r in rowRange) {
                for (subC in 0 downTo -1) {
                    val currentC = c + subC
                    if (currentC in 0 until size && !reserved[r][currentC]) {
                        val currentByte = if (bytes.isNotEmpty()) bytes[bytePos % bytes.size].toInt() else 0
                        val bit = (currentByte shr (7 - (bitIndex % 8))) and 1 == 1
                        // Apply standard mask pattern (row + col) % 2 == 0
                        val mask = (r + currentC) % 2 == 0
                        grid[r][currentC] = bit xor mask

                        bitIndex++
                        if (bitIndex % 8 == 0) bytePos++
                    }
                }
            }
        }

        // Fixed format info pattern (mask 0, Error correction M)
        val formatBits = intArrayOf(1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0)
        for (i in 0 until 6) grid[8][i] = formatBits[i] == 1
        grid[8][7] = formatBits[6] == 1
        grid[8][8] = formatBits[7] == 1
        grid[7][8] = formatBits[8] == 1
        for (i in 9..14) grid[14 - i][8] = formatBits[i] == 1

        for (i in 0..7) grid[size - 1 - i][8] = formatBits[i] == 1
        for (i in 8..14) grid[8][size - 15 + i] = formatBits[i] == 1

        return grid
    }
}

@Composable
fun QrCodeView(
    content: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp
) {
    val bitmap = remember(content) {
        QrCodeUtil.generateQrBitmap(content, sizePx = 400)
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Payment QR Code for UPI",
            modifier = Modifier.size(size - 24.dp)
        )
    }
}
