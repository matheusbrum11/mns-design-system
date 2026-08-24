package com.mns.designsystem.component.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.foundation.mnsSurface
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.LocalMnsTextStyle
import com.mns.designsystem.theme.MnsTheme

/**
 * Container primitivo do design system: um retângulo com forma, cor, borda,
 * elevação e cor de conteúdo consistentes.
 *
 * Todo componente que "tem fundo" (card, sheet, dialog, chip, banner) é
 * construído em cima deste. Ele resolve sozinho a parte que costuma ser
 * esquecida: propagar `LocalContentColor`, para que os textos e ícones
 * internos peguem automaticamente a cor certa para aquele fundo.
 *
 * @param shape recorte aplicado a fundo, borda e sombra simultaneamente.
 * @param color cor do fundo.
 * @param contentColor cor herdada por textos e ícones filhos. Por padrão é
 *   calculada por contraste sobre [color].
 * @param borderWidth espessura da borda; `0.dp` remove a borda.
 * @param borderColor cor da borda.
 * @param elevation altura da sombra. Use `MnsTheme.elevation.*`.
 * @param onClick quando não-nulo, torna a superfície clicável com ripple e
 *   semântica de botão.
 * @param enabled desabilita o clique e reduz a opacidade.
 */
@Composable
public fun MnsSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MnsTheme.shapes.card,
    color: Color = MnsTheme.colors.surface,
    contentColor: Color = MnsTheme.colors.onSurface,
    borderWidth: Dp = 0.dp,
    borderColor: Color = MnsTheme.colors.outline,
    elevation: Dp = MnsTheme.elevation.level0,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    role: Role = Role.Button,
    interactionSource: MutableInteractionSource = rememberMnsInteractionSource(),
    content: @Composable () -> Unit,
) {
    val opacity = MnsTheme.opacity
    val effectiveColor = if (enabled) color else disabledContainerColor(color, opacity.disabled)
    val effectiveContent = if (enabled) contentColor else contentColor.copy(alpha = opacity.disabled)

    Box(
        modifier = modifier
            .mnsSurface(
                shape = shape,
                color = effectiveColor,
                borderWidth = borderWidth,
                borderColor = borderColor,
                elevation = elevation,
            )
            .clip(shape)
            .let { base ->
                if (onClick == null) {
                    base
                } else {
                    base.clickable(
                        enabled = enabled,
                        role = role,
                        interactionSource = interactionSource,
                        indication = androidx.compose.foundation.LocalIndication.current,
                        onClick = onClick,
                    )
                }
            },
    ) {
        CompositionLocalProvider(
            LocalContentColor provides effectiveContent,
            LocalMnsTextStyle provides MnsTheme.typography.bodyMedium,
            content = content,
        )
    }
}

/**
 * Cor de container de um componente desabilitado.
 *
 * **Escala** o alpha existente em vez de substituí-lo. A diferença importa:
 * `Color.copy(alpha = x)` preserva o RGB, e um container transparente tem RGB
 * (0,0,0) — trocar só o alpha transformava `Color.Transparent` em preto
 * semiopaco, fazendo um `MnsButton` de variante `TEXT` ou `OUTLINED`
 * desabilitado desenhar uma caixa escura sobre fundo claro.
 *
 * Escalando, um container transparente (alpha 0) continua transparente, e um
 * container opaco esmaece na proporção esperada.
 *
 * @param color cor do container quando habilitado.
 * @param disabledOpacity `opacity.disabled` do tema (quanto se perde ao desabilitar).
 */
internal fun disabledContainerColor(color: Color, disabledOpacity: Float): Color =
    color.copy(alpha = color.alpha * (1f - disabledOpacity))
