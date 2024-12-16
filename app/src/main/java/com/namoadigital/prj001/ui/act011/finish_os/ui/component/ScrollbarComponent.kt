package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.namoadigital.prj001.design.compose.ApplicationTheme
import java.time.LocalDateTime

@Composable
fun Modifier.verticalScrollBar(
    state: ScrollState,
    color: Color = Color.Gray,
    ratio: Float = 6f,
    width: Dp = 7.dp,
    offSetTop: Dp = 16.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0.3f
    val duration = if (state.isScrollInProgress) 150 else 500
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    return this.drawWithContent {
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.2f
        val barHeight = (this.size.height / ratio)
        val barRange = (this.size.height - barHeight) / state.maxValue

        if (needDrawScrollbar) {
            val position = state.value * barRange
            val path = Path()
            val cornerRadius = CornerRadius(4.dp.toPx())

            path.addRoundRect(
                RoundRect(
                    left = this.size.width - width.toPx(),
                    top = position + offSetTop.toPx(),
                    right = this.size.width,
                    bottom = position + barHeight,
                    topLeftCornerRadius = cornerRadius,
                    topRightCornerRadius = cornerRadius,
                    bottomLeftCornerRadius = cornerRadius,
                    bottomRightCornerRadius = cornerRadius
                )
            )
            drawPath(path, color.copy(alpha = alpha))
        }

        drawContent()
    }
}