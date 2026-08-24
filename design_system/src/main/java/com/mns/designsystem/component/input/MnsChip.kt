package com.mns.designsystem.component.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Chip de filtro/seleção.
 *
 * É o componente do "travel ✕" e dos filtros de categoria dos designs de
 * referência. Diferente de um botão, um chip representa **um valor**, não uma
 * ação — por isso tem estado selecionado e semântica de seleção.
 *
 * @param label texto do chip.
 * @param selected estado de seleção. Controla cor de fundo e de conteúdo.
 * @param onClick chamado ao tocar no corpo do chip.
 * @param leadingIcon ícone antes do rótulo (categoria, avatar).
 * @param onDismiss quando não-nulo, exibe um "✕" ao final que remove o chip.
 *   O toque no ✕ **não** dispara [onClick].
 * @param enabled desabilita interação e reduz opacidade.
 * @param height altura do chip.
 */
@Composable
public fun MnsChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onDismiss: (() -> Unit)? = null,
    enabled: Boolean = true,
    height: Dp = MnsTheme.sizing.chipHeight,
    shape: Shape = MnsTheme.shapes.chip,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val motion = MnsTheme.motion

    val container by animateColorAsState(
        targetValue = when {
            !enabled -> colors.surfaceVariant
            selected -> colors.primary
            else -> colors.surfaceVariant
        },
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsChipContainer",
    )
    val contentColor = when {
        !enabled -> colors.textDisabled
        selected -> colors.onPrimary
        else -> colors.textSecondary
    }

    MnsSurface(
        modifier = modifier
            .height(height)
            .defaultMinSize(minWidth = spacing.xxl),
        shape = shape,
        color = container,
        contentColor = contentColor,
        borderWidth = if (selected || !enabled) 0.dp else MnsTheme.borders.thin,
        borderColor = colors.outline,
        onClick = onClick,
        enabled = enabled,
        role = Role.Tab,
    ) {
        Row(
            modifier = Modifier
                .height(height)
                .padding(horizontal = spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            if (leadingIcon != null) {
                MnsIcon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    size = MnsTheme.sizing.iconSm,
                    tint = contentColor,
                )
            }
            MnsText(
                text = label,
                style = MnsTheme.typography.labelMedium,
                color = contentColor,
                maxLines = 1,
            )
            if (onDismiss != null) {
                // O alvo é a caixa, não o ícone: antes o `clickable` ficava no
                // próprio MnsIcon de 16dp, abaixo até do piso de 24×24 do
                // WCAG 2.5.8. Aqui o ✕ ocupa toda a altura do chip e no mínimo
                // 24dp de largura, mantendo o desenho do ícone em 16dp.
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(min = MnsTheme.sizing.iconLg)
                        .clickable(enabled = enabled, onClick = onDismiss),
                    contentAlignment = Alignment.Center,
                ) {
                    MnsIcon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remover $label",
                        size = MnsTheme.sizing.iconSm,
                        tint = contentColor,
                    )
                }
            }
        }
    }
}

/**
 * Linha rolável de chips de filtro.
 *
 * @param options rótulos das opções.
 * @param selectedIndices índices selecionados.
 * @param onToggle chamado com o índice tocado; cabe ao chamador decidir se o
 *   filtro é de seleção única ou múltipla.
 */
@Composable
public fun MnsChipRow(
    options: List<String>,
    selectedIndices: Set<Int>,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
    ) {
        itemsIndexed(options) { index, option ->
            MnsChip(
                label = option,
                selected = index in selectedIndices,
                onClick = { onToggle(index) },
                enabled = enabled,
            )
        }
    }
}
