package com.mns.designsystem.component.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.media.MnsIcons
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.MnsTheme

/**
 * Estado de um [MnsCheckbox]. O terceiro estado ([INDETERMINATE]) existe para
 * o caso "selecionar todos" com seleção parcial — sem ele, o pai mente sobre
 * o estado dos filhos.
 */
public enum class MnsToggleState {
    /** Desmarcado. */
    UNCHECKED,

    /** Marcado. */
    CHECKED,

    /** Parcialmente marcado (alguns filhos selecionados). */
    INDETERMINATE,
}

/**
 * Caixa de seleção com rótulo opcional.
 *
 * O alvo de toque cobre o rótulo inteiro, não apenas o quadrado — 20dp de
 * quadrado é alvo pequeno demais e a regra de 48dp vale para a linha toda.
 *
 * @param state estado atual — ver [MnsToggleState].
 * @param onStateChange chamado com o próximo estado ao tocar. `INDETERMINATE`
 *   sempre avança para `CHECKED`.
 * @param label rótulo à direita da caixa.
 * @param description linha secundária abaixo do rótulo.
 */
@Composable
public fun MnsCheckbox(
    state: MnsToggleState,
    onStateChange: (MnsToggleState) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    enabled: Boolean = true,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val motion = MnsTheme.motion
    val checked = state != MnsToggleState.UNCHECKED

    val container by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.primaryDisabled
            checked -> colors.primary
            else -> Color.Transparent
        },
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsCheckboxContainer",
    )

    Row(
        modifier = modifier
            .clip(MnsTheme.shapes.small)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = {
                    onStateChange(
                        if (state == MnsToggleState.CHECKED) MnsToggleState.UNCHECKED
                        else MnsToggleState.CHECKED,
                    )
                },
            )
            .padding(vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(container, MnsTheme.shapes.extraSmall)
                .border(
                    width = if (checked) 0.dp else MnsTheme.borders.thick,
                    color = if (enabled) colors.outline else colors.outlineVariant,
                    shape = MnsTheme.shapes.extraSmall,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                MnsIcon(
                    imageVector = if (state == MnsToggleState.INDETERMINATE) {
                        MnsIcons.Minus
                    } else {
                        Icons.Filled.Check
                    },
                    contentDescription = null,
                    size = MnsTheme.sizing.iconSm,
                    tint = colors.onPrimary,
                )
            }
        }
        if (label != null) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
                MnsText(
                    text = label,
                    style = MnsTheme.typography.bodyMedium,
                    color = if (enabled) colors.textPrimary else colors.textDisabled,
                )
                if (description != null) {
                    MnsText(
                        text = description,
                        style = MnsTheme.typography.caption,
                        color = if (enabled) colors.textTertiary else colors.textDisabled,
                    )
                }
            }
        }
    }
}

/**
 * Botão de opção. Use dentro de um grupo com [Modifier.selectableGroup] para
 * que leitores de tela anunciem "1 de 3".
 *
 * @param selected se esta opção é a escolhida.
 * @param onSelect chamado ao tocar. Não é chamado se já estiver [selected].
 */
@Composable
public fun MnsRadioButton(
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    enabled: Boolean = true,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val motion = MnsTheme.motion

    val ringColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.outlineVariant
            selected -> colors.primary
            else -> colors.outline
        },
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsRadioRing",
    )
    val dotSize by animateDpAsState(
        targetValue = if (selected) 10.dp else 0.dp,
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsRadioDot",
    )

    Row(
        modifier = modifier
            .clip(MnsTheme.shapes.small)
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = { if (!selected) onSelect() },
            )
            .padding(vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(MnsTheme.borders.thick, ringColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .background(if (enabled) colors.primary else colors.primaryDisabled, CircleShape),
            )
        }
        if (label != null) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
                MnsText(
                    text = label,
                    style = MnsTheme.typography.bodyMedium,
                    color = if (enabled) colors.textPrimary else colors.textDisabled,
                )
                if (description != null) {
                    MnsText(
                        text = description,
                        style = MnsTheme.typography.caption,
                        color = if (enabled) colors.textTertiary else colors.textDisabled,
                    )
                }
            }
        }
    }
}

/**
 * Interruptor liga/desliga.
 *
 * Diferente de um checkbox, um switch aplica a mudança **imediatamente** — não
 * use dentro de formulário que só salva no botão "Confirmar".
 *
 * @param checked estado atual.
 * @param onCheckedChange chamado com o novo estado.
 * @param label rótulo à esquerda; o switch fica alinhado à direita da linha.
 */
@Composable
public fun MnsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    enabled: Boolean = true,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val motion = MnsTheme.motion
    val trackWidth = 48.dp
    val trackHeight = 28.dp
    val thumbSize = 22.dp

    val track by animateColorAsState(
        targetValue = when {
            !enabled -> colors.outlineVariant
            checked -> colors.primary
            else -> colors.surfaceVariant
        },
        animationSpec = motion.tween(motion.durationNormal),
        label = "mnsSwitchTrack",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - 3.dp else 3.dp,
        animationSpec = motion.tween(motion.durationNormal),
        label = "mnsSwitchThumb",
    )

    Row(
        modifier = modifier
            .clip(MnsTheme.shapes.small)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            )
            .padding(vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        if (label != null) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                MnsText(
                    text = label,
                    style = MnsTheme.typography.bodyMedium,
                    color = if (enabled) colors.textPrimary else colors.textDisabled,
                )
                if (description != null) {
                    MnsText(
                        text = description,
                        style = MnsTheme.typography.caption,
                        color = if (enabled) colors.textTertiary else colors.textDisabled,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .background(track, CircleShape),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                // A sobrecarga lambda de `offset` lê o State na fase de layout,
                // e não na de composição: o thumb desliza sem recompor a linha.
                modifier = Modifier
                    .offset { IntOffset(thumbOffset.roundToPx(), 0) }
                    .size(thumbSize)
                    .background(
                        color = if (enabled) colors.surface else colors.surfaceVariant,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

/** Agrupa controles de seleção com espaçamento consistente. */
@Composable
public fun MnsSelectionGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xs)) {
        if (title != null) {
            MnsText(
                text = title,
                style = MnsTheme.typography.labelMedium,
                color = MnsTheme.colors.textSecondary,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.base),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
