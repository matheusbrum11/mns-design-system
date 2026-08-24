package com.mns.designsystem.foundation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.theme.MnsTheme

/**
 * Aplica [block] apenas quando [condition] é verdadeira.
 *
 * Encadear `Modifier` condicionalmente com `if` quebra a leitura da cadeia;
 * este helper mantém tudo em uma expressão só.
 *
 * ```kotlin
 * Modifier
 *     .fillMaxWidth()
 *     .applyIf(selecionado) { border(2.dp, MnsTheme.colors.primary) }
 * ```
 */
public inline fun Modifier.applyIf(
    condition: Boolean,
    block: Modifier.() -> Modifier,
): Modifier = if (condition) block() else this

/**
 * Sombra do design system, com opacidade tokenizada.
 *
 * Usa `spotShadowColor`/`ambientShadowColor` derivados de `colors.shadow` e dos
 * alphas de [com.mns.designsystem.token.MnsElevation], o que faz a mesma
 * elevação render corretamente em tema claro e escuro.
 *
 * @param elevation altura da sombra. Use um degrau de `MnsTheme.elevation`.
 * @param shape recorte da sombra — precisa casar com o `background` do elemento.
 */
public fun Modifier.mnsShadow(
    elevation: Dp,
    shape: Shape,
): Modifier = composed {
    val tokens = MnsTheme.elevation
    val shadowColor = MnsTheme.colors.shadow
    if (elevation.value <= 0f || tokens.shadowAlpha <= 0f) {
        this
    } else {
        shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = shadowColor.copy(alpha = tokens.ambientAlpha),
            spotColor = shadowColor.copy(alpha = tokens.shadowAlpha),
        )
    }
}

/**
 * Container padrão do design system: sombra + fundo + borda opcional, todos
 * recortados pela mesma [shape]. Garante que os três nunca saiam de sincronia.
 */
public fun Modifier.mnsSurface(
    shape: Shape,
    color: Color,
    borderWidth: Dp,
    borderColor: Color,
    elevation: Dp,
): Modifier = this
    .mnsShadow(elevation, shape)
    .background(color = color, shape = shape)
    .applyIf(borderWidth.value > 0f) { border(borderWidth, borderColor, shape) }

/**
 * Reduz opacidade e desabilita interação visualmente quando [enabled] é `false`.
 * A desativação *funcional* continua sendo responsabilidade do componente —
 * este modifier trata só do aspecto visual.
 */
public fun Modifier.mnsDisabled(enabled: Boolean): Modifier = composed {
    if (enabled) this else alpha(MnsTheme.opacity.disabled)
}

/**
 * Encolhe levemente o elemento enquanto pressionado. É o feedback tátil usado
 * pelos cards e atalhos, onde um ripple retangular ficaria pesado.
 *
 * @param interactionSource o mesmo passado ao `clickable` do elemento.
 * @param pressedScale escala no estado pressionado. 1f desliga o efeito.
 */
@Composable
public fun Modifier.mnsPressScale(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.97f,
): Modifier {
    val motion = MnsTheme.motion
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsPressScale",
    )
    return scale(scale)
}

/** `MutableInteractionSource` memoizado — evita recriar a cada recomposição. */
@Composable
public fun rememberMnsInteractionSource(): MutableInteractionSource =
    remember { MutableInteractionSource() }
