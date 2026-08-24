package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * **Semantic tokens de traço.**
 *
 * Espessuras de borda e de divisor. Separar isso do espaçamento evita o erro
 * clássico de um redesenho de grade engrossar todas as linhas do app.
 */
@Immutable
public data class MnsBorders(
    /** 0dp — sem traço. */
    val none: Dp,
    /** 0.5dp — fio de cabelo: divisores de lista em telas densas. */
    val hairline: Dp,
    /** 1dp — **padrão**: contorno de card e de input. */
    val thin: Dp,
    /** 1.5dp — input em foco. */
    val medium: Dp,
    /** 2dp — item selecionado, contorno de erro. */
    val thick: Dp,
    /** 3dp — anel de foco de acessibilidade. */
    val focus: Dp,
) {
    public companion object {
        public val Default: MnsBorders = MnsBorders(
            none = 0.dp,
            hairline = 0.5.dp,
            thin = 1.dp,
            medium = 1.5.dp,
            thick = 2.dp,
            focus = 3.dp,
        )
    }
}
