package com.mns.designsystem.component.input

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Slider de valor contínuo ou discreto.
 *
 * Implementado com `Canvas` + gestos em vez de envolver o `Slider` do Material
 * porque o thumb e a trilha do Material carregam paddings e elevações próprias
 * que não são tokenizáveis — o resultado ficava fora da grade em temas densos.
 *
 * @param value valor atual, dentro de [valueRange].
 * @param onValueChange chamado continuamente durante o arraste.
 * @param valueRange faixa permitida.
 * @param steps número de paradas intermediárias. `0` = contínuo.
 * @param label rótulo acima da trilha.
 * @param formatValue formatação do valor exibido à direita do rótulo.
 * @param trackHeight espessura da trilha.
 */
@Composable
public fun MnsSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    label: String? = null,
    formatValue: ((Float) -> String)? = null,
    trackHeight: Dp = 6.dp,
    thumbRadius: Dp = 11.dp,
) {
    val colors = MnsTheme.colors
    val span = (valueRange.endInclusive - valueRange.start).takeIf { it > 0f } ?: 1f
    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)
    var widthPx by remember { mutableFloatStateOf(0f) }

    fun emit(positionX: Float) {
        if (!enabled || widthPx <= 0f) return
        val raw = (positionX / widthPx).coerceIn(0f, 1f)
        val snapped = if (steps > 0) {
            val stepSize = 1f / (steps + 1)
            kotlin.math.round(raw / stepSize) * stepSize
        } else {
            raw
        }
        onValueChange(valueRange.start + snapped * span)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
    ) {
        if (label != null || formatValue != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MnsText(
                    text = label.orEmpty(),
                    style = MnsTheme.typography.labelMedium,
                    color = if (enabled) colors.textSecondary else colors.textDisabled,
                )
                if (formatValue != null) {
                    MnsText(
                        text = formatValue(value),
                        style = MnsTheme.typography.labelMedium,
                        color = if (enabled) colors.textPrimary else colors.textDisabled,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbRadius * 2)
                .semantics {
                    progressBarRangeInfo = ProgressBarRangeInfo(
                        current = value,
                        range = valueRange,
                        steps = steps,
                    )
                }
                .pointerInput(enabled, steps, widthPx) {
                    detectTapGestures { offset -> emit(offset.x) }
                }
                .pointerInput(enabled, steps, widthPx) {
                    detectDragGestures { change, _ -> emit(change.position.x) }
                },
            contentAlignment = Alignment.CenterStart,
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(thumbRadius * 2)) {
                widthPx = size.width
                val trackPx = trackHeight.toPx()
                val centerY = size.height / 2f
                val radius = CornerRadius(trackPx / 2f, trackPx / 2f)
                drawRoundRect(
                    color = if (enabled) colors.surfaceVariant else colors.outlineVariant,
                    topLeft = Offset(0f, centerY - trackPx / 2f),
                    size = Size(size.width, trackPx),
                    cornerRadius = radius,
                )
                drawRoundRect(
                    color = if (enabled) colors.primary else colors.primaryDisabled,
                    topLeft = Offset(0f, centerY - trackPx / 2f),
                    size = Size(size.width * fraction, trackPx),
                    cornerRadius = radius,
                )
                drawCircle(
                    color = if (enabled) colors.surface else colors.surfaceVariant,
                    radius = thumbRadius.toPx(),
                    center = Offset(size.width * fraction, centerY),
                )
                drawCircle(
                    color = if (enabled) colors.primary else colors.primaryDisabled,
                    radius = thumbRadius.toPx() - 3.dp.toPx(),
                    center = Offset(size.width * fraction, centerY),
                )
            }
        }
    }
}

/** Cor auxiliar reutilizada por sliders desabilitados. */
internal val SliderDisabledTint: Color = Color.Transparent
