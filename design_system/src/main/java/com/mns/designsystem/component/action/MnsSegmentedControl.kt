package com.mns.designsystem.component.action

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import kotlin.math.roundToInt

/**
 * Uma opção do [MnsSegmentedControl].
 *
 * @property label texto exibido.
 * @property icon ícone opcional à esquerda do texto.
 * @property enabled quando `false`, a opção não pode ser escolhida.
 */
public data class MnsSegment(
    val label: String,
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
)

/**
 * Controle segmentado — o par "One Way / Round Trip" dos designs de referência.
 *
 * Use para alternar entre **modos de visualização mutuamente exclusivos** com
 * 2 a 4 opções. Acima de 4, use `MnsTabBar`; para filtros múltiplos, use chips.
 *
 * O indicador desliza entre as posições em vez de aparecer/sumir: o movimento
 * comunica que as opções pertencem ao mesmo eixo de escolha.
 *
 * @param segments opções, na ordem de exibição. Precisa ter ao menos uma.
 * @param selectedIndex índice da opção ativa.
 * @param onSelect chamado com o índice tocado.
 * @param height altura do controle.
 */
@Composable
public fun MnsSegmentedControl(
    segments: List<MnsSegment>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = MnsTheme.sizing.buttonHeightMd,
    shape: Shape = MnsTheme.shapes.full,
) {
    require(segments.isNotEmpty()) { "MnsSegmentedControl exige ao menos um segmento." }
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val motion = MnsTheme.motion
    val safeIndex = selectedIndex.coerceIn(0, segments.lastIndex)

    val animatedIndex by animateFloatAsState(
        targetValue = safeIndex.toFloat(),
        animationSpec = motion.tween(motion.durationNormal),
        label = "mnsSegmentIndicator",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(colors.surfaceVariant)
            .padding(4.dp)
            .semantics { selectableGroup() },
    ) {
        val segmentWidth: Dp = (maxWidth) / segments.size

        // Indicador deslizante, desenhado atrás dos rótulos. A sobrecarga
        // lambda de `offset` lê o State na fase de layout, evitando recompor
        // a barra inteira a cada frame da animação.
        Box(
            modifier = Modifier
                .offset { IntOffset((segmentWidth.toPx() * animatedIndex).roundToInt(), 0) }
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(shape)
                .background(colors.primary),
        )

        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            segments.forEachIndexed { index, segment ->
                val isSelected = index == safeIndex
                val itemEnabled = enabled && segment.enabled
                val contentColor = when {
                    !itemEnabled -> colors.textDisabled
                    isSelected -> colors.onPrimary
                    else -> colors.textSecondary
                }
                MnsSurface(
                    modifier = Modifier.width(segmentWidth).fillMaxHeight(),
                    shape = shape,
                    color = androidx.compose.ui.graphics.Color.Transparent,
                    contentColor = contentColor,
                    onClick = { onSelect(index) },
                    enabled = itemEnabled,
                    role = Role.RadioButton,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.xs, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (segment.icon != null) {
                            MnsIcon(
                                imageVector = segment.icon,
                                contentDescription = null,
                                size = MnsTheme.sizing.iconSm,
                                tint = contentColor,
                            )
                        }
                        MnsText(
                            text = segment.label,
                            style = MnsTheme.typography.labelMedium,
                            color = contentColor,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}
