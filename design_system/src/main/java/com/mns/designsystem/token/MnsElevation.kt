package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * **Semantic tokens de elevação.**
 *
 * Além do valor em dp, carrega [shadowAlpha] e [ambientAlpha] porque a mesma
 * elevação precisa de sombras muito diferentes em tema claro e escuro — em
 * tema escuro a sombra praticamente some e o que separa as camadas é a cor da
 * superfície. Componentes que respeitam isso não "flutuam errado" no dark mode.
 */
@Immutable
public data class MnsElevation(
    /** 0dp — no plano da superfície. */
    val level0: Dp,
    /** 1dp — separação mínima (barra fixa sobre conteúdo rolando). */
    val level1: Dp,
    /** 3dp — cards. */
    val level2: Dp,
    /** 6dp — FAB, menu suspenso. */
    val level3: Dp,
    /** 8dp — bottom sheet. */
    val level4: Dp,
    /** 12dp — dialog modal. */
    val level5: Dp,
    /** Opacidade da sombra projetada (`spotShadowColor`). */
    val shadowAlpha: Float,
    /** Opacidade da sombra ambiente. */
    val ambientAlpha: Float,
) {
    public companion object {
        /** Elevações para temas claros — sombras visíveis e suaves. */
        public val Light: MnsElevation = MnsElevation(
            level0 = 0.dp, level1 = 1.dp, level2 = 3.dp,
            level3 = 6.dp, level4 = 8.dp, level5 = 12.dp,
            shadowAlpha = 0.10f,
            ambientAlpha = 0.06f,
        )

        /** Elevações para temas escuros — sombra quase nula, hierarquia via cor. */
        public val Dark: MnsElevation = Light.copy(
            shadowAlpha = 0.36f,
            ambientAlpha = 0.24f,
        )

        /** Design "flat": nenhuma sombra, hierarquia só por borda e cor. */
        public val Flat: MnsElevation = MnsElevation(
            level0 = 0.dp, level1 = 0.dp, level2 = 0.dp,
            level3 = 0.dp, level4 = 0.dp, level5 = 0.dp,
            shadowAlpha = 0f,
            ambientAlpha = 0f,
        )
    }
}
