package com.mns.designsystem.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.foundation.mnsPressScale
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.MnsTheme

/**
 * Aparência de um [MnsCard]. Cada variante corresponde a uma decisão de
 * hierarquia visual — não são apenas "estilos bonitos":
 *
 * - [ELEVATED] separa por sombra: use quando o card flutua sobre conteúdo.
 * - [FILLED] separa por cor: use em listas longas, onde N sombras viram ruído.
 * - [OUTLINED] separa por traço: use em temas flat ou densidade alta.
 * - [ACCENT] destaca **um** card da lista (o evento em alta, o plano recomendado).
 * - [GHOST] não separa nada: agrupa conteúdo sem custo visual.
 */
public enum class MnsCardVariant { ELEVATED, FILLED, OUTLINED, ACCENT, GHOST }

/**
 * Container de conteúdo agrupado.
 *
 * ```kotlin
 * MnsCard(variant = MnsCardVariant.ACCENT, onClick = { abrir(evento) }) {
 *     MnsHeading("North Van Hiking", level = MnsHeadingLevel.H4)
 *     MnsText("Vancouver Community Centre")
 * }
 * ```
 *
 * @param variant aparência — ver [MnsCardVariant].
 * @param onClick quando não-nulo, o card vira alvo de toque com feedback de
 *   escala (mais adequado que ripple em superfícies grandes).
 * @param contentPadding padding interno. Passe `PaddingValues(0.dp)` quando o
 *   card começa com uma imagem sangrada.
 * @param shape forma; default `MnsTheme.shapes.card`.
 * @param elevation sobrescreve a elevação da variante.
 * @param verticalArrangement espaçamento entre os filhos.
 */
@Composable
public fun MnsCard(
    modifier: Modifier = Modifier,
    variant: MnsCardVariant = MnsCardVariant.FILLED,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = MnsTheme.shapes.card,
    contentPadding: Dp = MnsTheme.spacing.cardPadding,
    elevation: Dp? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MnsTheme.spacing.sm),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MnsTheme.colors
    val borders = MnsTheme.borders
    val elevations = MnsTheme.elevation
    val interactionSource = rememberMnsInteractionSource()

    val container: Color
    val onContainer: Color
    val borderWidth: Dp
    val borderColor: Color
    val resolvedElevation: Dp

    when (variant) {
        MnsCardVariant.ELEVATED -> {
            container = colors.surface
            onContainer = colors.onSurface
            borderWidth = 0.dp
            borderColor = Color.Transparent
            resolvedElevation = elevations.level2
        }
        MnsCardVariant.FILLED -> {
            container = colors.surface
            onContainer = colors.onSurface
            borderWidth = 0.dp
            borderColor = Color.Transparent
            resolvedElevation = elevations.level0
        }
        MnsCardVariant.OUTLINED -> {
            container = colors.surface
            onContainer = colors.onSurface
            borderWidth = borders.thin
            borderColor = colors.outline
            resolvedElevation = elevations.level0
        }
        MnsCardVariant.ACCENT -> {
            container = colors.accentContainer
            onContainer = colors.onAccentContainer
            borderWidth = 0.dp
            borderColor = Color.Transparent
            resolvedElevation = elevations.level0
        }
        MnsCardVariant.GHOST -> {
            container = Color.Transparent
            onContainer = colors.onBackground
            borderWidth = 0.dp
            borderColor = Color.Transparent
            resolvedElevation = elevations.level0
        }
    }

    MnsSurface(
        modifier = modifier.let {
            if (onClick != null && enabled) it.mnsPressScale(interactionSource) else it
        },
        shape = shape,
        color = container,
        contentColor = onContainer,
        borderWidth = borderWidth,
        borderColor = borderColor,
        elevation = elevation ?: resolvedElevation,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = verticalArrangement,
            content = content,
        )
    }
}
