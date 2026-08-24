package com.mns.designsystem.component.action

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.status.MnsCircularProgress
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.MnsTheme

/**
 * Nível de ênfase de um [MnsButton].
 *
 * A regra do design system é: **uma** ação [PRIMARY] por tela. Se você precisa
 * de duas, uma delas na verdade é [SECONDARY] ou [TEXT] — e a tela provavelmente
 * está pedindo duas decisões ao usuário de uma vez.
 */
public enum class MnsButtonVariant {
    /** Ação principal, preenchida com `colors.primary`. */
    PRIMARY,

    /** Ação de apoio, preenchida com `colors.primaryContainer`. */
    SECONDARY,

    /** Ação de apoio com contorno, sem preenchimento. */
    OUTLINED,

    /** Ação terciária: só texto, sem container. */
    TEXT,

    /** Ação destrutiva (excluir, cancelar pedido). */
    DANGER,
}

/** Tamanho de um [MnsButton]. Controla altura, padding e papel tipográfico. */
public enum class MnsButtonSize {
    /** 36dp — ações dentro de cards e listas. */
    SMALL,

    /** 48dp — **padrão**. */
    MEDIUM,

    /** 56dp — CTA de rodapé (checkout, confirmar). */
    LARGE,
}

/**
 * Botão do design system.
 *
 * ```kotlin
 * MnsButton(
 *     text = "Pay Now",
 *     variant = MnsButtonVariant.PRIMARY,
 *     size = MnsButtonSize.LARGE,
 *     fillMaxWidth = true,
 *     onClick = ::pagar,
 * )
 * ```
 *
 * @param text rótulo do botão.
 * @param onClick ação disparada no toque. Não é chamada quando [enabled] é
 *   `false` nem quando [loading] é `true`.
 * @param variant nível de ênfase — ver [MnsButtonVariant].
 * @param size tamanho — ver [MnsButtonSize].
 * @param enabled quando `false`, o botão fica opaco e ignora toques.
 * @param loading quando `true`, substitui o ícone à esquerda por um spinner e
 *   bloqueia o clique. O rótulo **permanece visível** de propósito: trocar o
 *   texto por um spinner faz o botão mudar de largura e o layout pular.
 * @param leadingIcon ícone antes do rótulo.
 * @param trailingIcon ícone depois do rótulo.
 * @param fillMaxWidth ocupa toda a largura disponível.
 * @param shape forma; default `MnsTheme.shapes.button`.
 * @param contentDescription descrição alternativa para leitores de tela;
 *   por padrão usa [text].
 */
@Composable
public fun MnsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: MnsButtonVariant = MnsButtonVariant.PRIMARY,
    size: MnsButtonSize = MnsButtonSize.MEDIUM,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    fillMaxWidth: Boolean = false,
    shape: Shape = MnsTheme.shapes.button,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = rememberMnsInteractionSource(),
) {
    val colors = MnsTheme.colors
    val sizing = MnsTheme.sizing
    val spacing = MnsTheme.spacing
    val typography = MnsTheme.typography
    val motion = MnsTheme.motion
    val borders = MnsTheme.borders

    val pressed by interactionSource.collectIsPressedAsState()
    val active = enabled && !loading

    val height: Dp = when (size) {
        MnsButtonSize.SMALL -> sizing.buttonHeightSm
        MnsButtonSize.MEDIUM -> sizing.buttonHeightMd
        MnsButtonSize.LARGE -> sizing.buttonHeightLg
    }
    val horizontalPadding: Dp = when (size) {
        MnsButtonSize.SMALL -> spacing.md
        MnsButtonSize.MEDIUM -> spacing.buttonHorizontal
        MnsButtonSize.LARGE -> spacing.xl
    }
    val labelStyle = when (size) {
        MnsButtonSize.SMALL -> typography.labelMedium
        MnsButtonSize.MEDIUM -> typography.labelLarge
        MnsButtonSize.LARGE -> typography.labelLarge
    }
    val iconSize = if (size == MnsButtonSize.SMALL) sizing.iconSm else sizing.iconMd

    val targetContainer: Color
    val contentColor: Color
    val borderWidth: Dp
    val borderColor: Color

    when (variant) {
        MnsButtonVariant.PRIMARY -> {
            targetContainer = if (pressed) colors.primaryPressed else colors.primary
            contentColor = colors.onPrimary
            borderWidth = 0.dp
            borderColor = Color.Transparent
        }

        MnsButtonVariant.SECONDARY -> {
            targetContainer = colors.primaryContainer
            contentColor = colors.onPrimaryContainer
            borderWidth = 0.dp
            borderColor = Color.Transparent
        }

        MnsButtonVariant.OUTLINED -> {
            targetContainer = if (pressed) colors.surfaceVariant else Color.Transparent
            contentColor = colors.primary
            borderWidth = borders.thin
            borderColor = colors.outline
        }

        MnsButtonVariant.TEXT -> {
            targetContainer = if (pressed) colors.surfaceVariant else Color.Transparent
            contentColor = colors.primary
            borderWidth = 0.dp
            borderColor = Color.Transparent
        }

        MnsButtonVariant.DANGER -> {
            targetContainer = colors.danger
            contentColor = colors.onDanger
            borderWidth = 0.dp
            borderColor = Color.Transparent
        }
    }

    val container by animateColorAsState(
        targetValue = targetContainer,
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsButtonContainer",
    )

    MnsSurface(
        modifier = modifier
            .semantics { this.contentDescription = contentDescription ?: text }
            .let { if (fillMaxWidth) it.fillMaxWidth() else it }
            .height(height)
            .defaultMinSize(minWidth = sizing.touchTarget),
        shape = shape,
        color = container,
        contentColor = contentColor,
        borderWidth = borderWidth,
        borderColor = borderColor,
        elevation = MnsTheme.elevation.level0,
        onClick = onClick,
        enabled = active,
        role = Role.Button,
        interactionSource = interactionSource,
    ) {
        Row(
            // `fillMaxHeight()` (e não `fillMaxSize()`) é o que centraliza o
            // conteúdo verticalmente: o Box do MnsSurface alinha em TopStart, e
            // sem preencher a altura o rótulo encostava no topo do botão.
            //
            // A largura, porém, só é preenchida quando `fillMaxWidth` é `true`.
            // `fillMaxSize()` preenchia as duas, e aí o botão consumia toda a
            // largura oferecida pelo pai mesmo com o parâmetro `false` — o que
            // zerava qualquer irmão com `weight(1f)` ao lado (o caso do
            // MnsSectionHeader). Coberto por `MnsActionComponentTest`.
            modifier = Modifier
                .fillMaxHeight()
                .let { if (fillMaxWidth) it.fillMaxWidth() else it }
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(
                spacing.buttonIconGap,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when {
                loading -> MnsCircularProgress(
                    progress = null,
                    size = iconSize,
                    strokeWidth = 2.dp,
                    color = contentColor,
                    trackColor = contentColor.copy(alpha = MnsTheme.opacity.faint),
                )

                leadingIcon != null -> MnsIcon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    size = iconSize,
                    tint = contentColor,
                )
            }
            MnsText(
                text = text,
                style = labelStyle,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (trailingIcon != null) {
                MnsIcon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    size = iconSize,
                    tint = contentColor,
                )
            }
        }
    }
}

/**
 * Botão que contém apenas um ícone.
 *
 * @param icon ícone exibido.
 * @param contentDescription **obrigatório**: sem rótulo textual, é a única
 *   informação que o leitor de tela tem. Passe `null` apenas se o botão for
 *   puramente decorativo — o que quase nunca é o caso.
 * @param variant ênfase — ver [MnsButtonVariant].
 * @param size lado do alvo de toque. O default é `sizing.touchTarget` (48dp) e
 *   é o que você deve usar. Reduza **apenas** dentro de containers densos onde
 *   48dp não cabe — campo de texto, chip, cabeçalho de alerta — e nunca abaixo
 *   de 24dp, o piso do WCAG 2.5.8 (AA). Ver `docs/tokens/sizing.md`.
 */
@Composable
public fun MnsIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: MnsButtonVariant = MnsButtonVariant.TEXT,
    enabled: Boolean = true,
    size: Dp = MnsTheme.sizing.touchTarget,
    iconSize: Dp = MnsTheme.sizing.iconMd,
    shape: Shape = MnsTheme.shapes.full,
    interactionSource: MutableInteractionSource = rememberMnsInteractionSource(),
) {
    val colors = MnsTheme.colors
    val borders = MnsTheme.borders

    val container: Color
    val contentColor: Color
    val borderWidth: Dp
    when (variant) {
        MnsButtonVariant.PRIMARY -> {
            container = colors.primary; contentColor = colors.onPrimary; borderWidth = 0.dp
        }

        MnsButtonVariant.SECONDARY -> {
            container = colors.primaryContainer; contentColor =
                colors.onPrimaryContainer; borderWidth = 0.dp
        }

        MnsButtonVariant.OUTLINED -> {
            container = Color.Transparent; contentColor = colors.onSurface; borderWidth =
                borders.thin
        }

        MnsButtonVariant.TEXT -> {
            container = Color.Transparent; contentColor = colors.onSurface; borderWidth = 0.dp
        }

        MnsButtonVariant.DANGER -> {
            container = colors.dangerContainer; contentColor =
                colors.onDangerContainer; borderWidth = 0.dp
        }
    }

    MnsSurface(
        modifier = modifier.size(size),
        shape = shape,
        color = container,
        contentColor = contentColor,
        borderWidth = borderWidth,
        borderColor = colors.outline,
        onClick = onClick,
        enabled = enabled,
        role = Role.Button,
        interactionSource = interactionSource,
    ) {
        Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
            MnsIcon(
                imageVector = icon,
                contentDescription = contentDescription,
                size = iconSize,
                tint = contentColor,
            )
        }
    }
}
