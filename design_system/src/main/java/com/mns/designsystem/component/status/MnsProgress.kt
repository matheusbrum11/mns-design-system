package com.mns.designsystem.component.status

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.theme.MnsTheme

/**
 * Indicador de progresso circular.
 *
 * Passe [progress] para o modo determinado (0f–1f) ou deixe `null` para o modo
 * indeterminado. Prefira o determinado sempre que houver como estimar: barra
 * indeterminada em operação longa é a principal causa de abandono percebido.
 *
 * @param progress fração concluída (0f–1f), ou `null` para indeterminado.
 * @param size diâmetro do indicador.
 * @param strokeWidth espessura do traço.
 * @param color cor do arco de progresso.
 * @param trackColor cor da trilha de fundo. `Color.Transparent` remove a trilha.
 * @param contentDescription descrição para leitores de tela.
 */
@Composable
public fun MnsCircularProgress(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    size: Dp = MnsTheme.sizing.iconLg,
    strokeWidth: Dp = 3.dp,
    color: Color = MnsTheme.colors.primary,
    trackColor: Color = MnsTheme.colors.outlineVariant,
    contentDescription: String? = null,
) {
    val motion = MnsTheme.motion
    val transition = rememberInfiniteTransition(label = "mnsCircularProgress")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(motion.duration(1000).coerceAtLeast(1), easing = motion.easingLinear),
        ),
        label = "rotation",
    )

    val semanticsModifier = modifier
        .size(size)
        .semantics {
            if (contentDescription != null) this.contentDescription = contentDescription
            progressBarRangeInfo = if (progress != null) {
                ProgressBarRangeInfo(progress.coerceIn(0f, 1f), 0f..1f)
            } else {
                ProgressBarRangeInfo.Indeterminate
            }
        }

    Canvas(modifier = semanticsModifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val inset = strokeWidth.toPx() / 2f
        val arcSize = Size(this.size.width - inset * 2, this.size.height - inset * 2)
        if (trackColor != Color.Transparent) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = stroke,
            )
        }
        val sweep = if (progress != null) progress.coerceIn(0f, 1f) * 360f else 90f
        drawArc(
            color = color,
            startAngle = if (progress != null) -90f else rotation,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = stroke,
        )
    }
}

/**
 * Barra de progresso linear.
 *
 * @param progress fração concluída (0f–1f), ou `null` para indeterminado.
 * @param height espessura da barra.
 * @param color cor do preenchimento.
 * @param trackColor cor da trilha.
 */
@Composable
public fun MnsLinearProgress(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    height: Dp = 6.dp,
    color: Color = MnsTheme.colors.primary,
    trackColor: Color = MnsTheme.colors.surfaceVariant,
    contentDescription: String? = null,
) {
    val motion = MnsTheme.motion
    val transition = rememberInfiniteTransition(label = "mnsLinearProgress")
    val head by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(motion.duration(1200).coerceAtLeast(1), easing = motion.easingStandard),
            repeatMode = RepeatMode.Restart,
        ),
        label = "head",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .semantics {
                if (contentDescription != null) this.contentDescription = contentDescription
                progressBarRangeInfo = if (progress != null) {
                    ProgressBarRangeInfo(progress.coerceIn(0f, 1f), 0f..1f)
                } else {
                    ProgressBarRangeInfo.Indeterminate
                }
            },
    ) {
        val radius = size.height / 2f
        drawRoundRect(
            color = trackColor,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
        )
        if (progress != null) {
            drawRoundRect(
                color = color,
                size = Size(size.width * progress.coerceIn(0f, 1f), size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
            )
        } else {
            val barWidth = size.width * 0.35f
            val start = (size.width + barWidth) * head - barWidth
            drawRoundRect(
                color = color,
                topLeft = Offset(start.coerceAtLeast(0f), 0f),
                size = Size(
                    width = minOf(barWidth, size.width - start.coerceAtLeast(0f)).coerceAtLeast(0f),
                    height = size.height,
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
            )
        }
    }
}
