package com.mns.designsystem.token

import androidx.compose.runtime.Immutable

/**
 * **Semantic tokens de opacidade.**
 *
 * Centraliza os alphas de estado. Sem isso cada componente inventa o seu
 * `0.38f` e o app fica com cinco cinzas de "desabilitado" ligeiramente
 * diferentes.
 */
@Immutable
public data class MnsOpacity(
    /** Elemento desabilitado — conteúdo e container. */
    val disabled: Float,
    /** Sobreposição no estado pressionado. */
    val pressed: Float,
    /** Sobreposição no estado hover (teclado/mouse/TV). */
    val hovered: Float,
    /** Sobreposição no estado focado. */
    val focused: Float,
    /** Sobreposição no estado arrastado. */
    val dragged: Float,
    /** Véu do scrim atrás de modais. */
    val scrim: Float,
    /** Camada sobre imagens para legibilidade de texto. */
    val overlay: Float,
    /** Ênfase reduzida de texto/ícone secundário. */
    val subtle: Float,
    /** Divisores e traços muito discretos. */
    val faint: Float,
) {
    public companion object {
        public val Default: MnsOpacity = MnsOpacity(
            disabled = 0.38f,
            pressed = 0.12f,
            hovered = 0.08f,
            focused = 0.12f,
            dragged = 0.16f,
            scrim = 0.48f,
            overlay = 0.32f,
            subtle = 0.70f,
            faint = 0.12f,
        )
    }
}
