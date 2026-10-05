package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurrencyType
import com.example.ui.theme.BcvBlue
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.EuroIndigo
import com.example.ui.theme.ParaleloGreen

@Composable
fun CurrencyBadgeIcon(
    type: CurrencyType,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    when (type) {
        CurrencyType.BCV_USD -> BcvIcon(modifier = modifier, size = size)
        CurrencyType.PARALELO_USD -> ParaleloIcon(modifier = modifier, size = size)
        CurrencyType.BINANCE_USDT -> BinanceIcon(modifier = modifier, size = size)
        CurrencyType.BCV_EUR -> EuroIcon(modifier = modifier, size = size)
    }
}

@Composable
fun BcvIcon(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(BcvBlue),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.7f)) {
            val w = this.size.width
            val h = this.size.height

            // Bank pediment / roof triangle
            val roof = Path().apply {
                moveTo(w * 0.5f, h * 0.1f)
                lineTo(w * 0.9f, h * 0.38f)
                lineTo(w * 0.1f, h * 0.38f)
                close()
            }
            drawPath(roof, Color.White)

            // Horizontal architrave
            drawRect(
                color = Color.White,
                topLeft = Offset(w * 0.12f, h * 0.42f),
                size = Size(w * 0.76f, h * 0.08f)
            )

            // Columns
            val colW = w * 0.12f
            val colH = h * 0.30f
            val colY = h * 0.52f
            drawRect(Color.White, Offset(w * 0.18f, colY), Size(colW, colH))
            drawRect(Color.White, Offset(w * 0.44f, colY), Size(colW, colH))
            drawRect(Color.White, Offset(w * 0.70f, colY), Size(colW, colH))

            // Base foundation
            drawRect(
                color = Color.White,
                topLeft = Offset(w * 0.08f, h * 0.84f),
                size = Size(w * 0.84f, h * 0.08f)
            )
        }
    }
}

@Composable
fun ParaleloIcon(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(ParaleloGreen),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.7f)) {
            val w = this.size.width
            val h = this.size.height

            // Parallel trend arrows and dollar glyph
            val strokeWidth = w * 0.14f

            // Left line with upward arrow
            drawLine(
                color = Color.White,
                start = Offset(w * 0.32f, h * 0.82f),
                end = Offset(w * 0.32f, h * 0.22f),
                strokeWidth = strokeWidth
            )
            val arrowLeft = Path().apply {
                moveTo(w * 0.16f, h * 0.38f)
                lineTo(w * 0.32f, h * 0.15f)
                lineTo(w * 0.48f, h * 0.38f)
            }
            drawPath(arrowLeft, Color.White, style = Stroke(strokeWidth))

            // Right line with downward arrow
            drawLine(
                color = Color.White,
                start = Offset(w * 0.68f, h * 0.18f),
                end = Offset(w * 0.68f, h * 0.78f),
                strokeWidth = strokeWidth
            )
            val arrowRight = Path().apply {
                moveTo(w * 0.52f, h * 0.62f)
                lineTo(w * 0.68f, h * 0.85f)
                lineTo(w * 0.84f, h * 0.62f)
            }
            drawPath(arrowRight, Color.White, style = Stroke(strokeWidth))
        }
    }
}

@Composable
fun BinanceIcon(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E2026)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.72f)) {
            val w = this.size.width
            val h = this.size.height

            // Central diamond
            val centerDiamond = Path().apply {
                moveTo(w * 0.5f, h * 0.34f)
                lineTo(w * 0.66f, h * 0.5f)
                lineTo(w * 0.5f, h * 0.66f)
                lineTo(w * 0.34f, h * 0.5f)
                close()
            }
            drawPath(centerDiamond, BinanceGold)

            // Top diamond
            val topDiamond = Path().apply {
                moveTo(w * 0.5f, h * 0.08f)
                lineTo(w * 0.62f, h * 0.20f)
                lineTo(w * 0.5f, h * 0.32f)
                lineTo(w * 0.38f, h * 0.20f)
                close()
            }
            drawPath(topDiamond, BinanceGold)

            // Bottom diamond
            val bottomDiamond = Path().apply {
                moveTo(w * 0.5f, h * 0.68f)
                lineTo(w * 0.62f, h * 0.80f)
                lineTo(w * 0.5f, h * 0.92f)
                lineTo(w * 0.38f, h * 0.80f)
                close()
            }
            drawPath(bottomDiamond, BinanceGold)

            // Left diamond
            val leftDiamond = Path().apply {
                moveTo(w * 0.20f, h * 0.38f)
                lineTo(w * 0.32f, h * 0.50f)
                lineTo(w * 0.20f, h * 0.62f)
                lineTo(w * 0.08f, h * 0.50f)
                close()
            }
            drawPath(leftDiamond, BinanceGold)

            // Right diamond
            val rightDiamond = Path().apply {
                moveTo(w * 0.80f, h * 0.38f)
                lineTo(w * 0.92f, h * 0.50f)
                lineTo(w * 0.80f, h * 0.62f)
                lineTo(w * 0.68f, h * 0.50f)
                close()
            }
            drawPath(rightDiamond, BinanceGold)
        }
    }
}

@Composable
fun EuroIcon(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(EuroIndigo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "€",
            color = Color.White,
            fontSize = (size.value * 0.62f).sp,
            fontWeight = FontWeight.Black
        )
    }
}
