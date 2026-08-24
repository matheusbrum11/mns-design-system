package com.mns.designsystem.token

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * **Semantic tokens de forma.**
 *
 * Divide-se em duas partes:
 *  - a **escala** (`none` → `full`), que é o vocabulário cru de raios;
 *  - os **papéis de componente** (`button`, `card`, …), que é o que os
 *    componentes consomem.
 *
 * Essa separação é o que permite um redesenho do tipo *"cards ficam mais
 * arredondados, botões continuam iguais"* mexendo em um campo só.
 */
@Immutable
public data class MnsShapes(
    // ── Escala ───────────────────────────────────────────────────────────────
    /** Canto reto (0dp). */
    val none: CornerBasedShape,
    /** Arredondamento mínimo — 4dp. Tags e badges densos. */
    val extraSmall: CornerBasedShape,
    /** 8dp. Inputs compactos, chips. */
    val small: CornerBasedShape,
    /** 12dp. Padrão de botões e campos. */
    val medium: CornerBasedShape,
    /** 16dp. Cards e containers. */
    val large: CornerBasedShape,
    /** 24dp. Bottom sheets, dialogs, cards-herói. */
    val extraLarge: CornerBasedShape,
    /** 32dp. Superfícies muito arredondadas (estilo "squircle"). */
    val huge: CornerBasedShape,
    /** Pílula/círculo — 50%. */
    val full: CornerBasedShape,

    // ── Papéis de componente ─────────────────────────────────────────────────
    /** Forma de `MnsButton` e `MnsIconButton`. */
    val button: CornerBasedShape,
    /** Forma de `MnsTextField` e derivados. */
    val input: CornerBasedShape,
    /** Forma de `MnsCard`. */
    val card: CornerBasedShape,
    /** Forma de `MnsChip` e `MnsTag`. */
    val chip: CornerBasedShape,
    /** Forma do topo de `MnsBottomSheet`. */
    val bottomSheet: CornerBasedShape,
    /** Forma de `MnsDialog`. */
    val dialog: CornerBasedShape,
    /** Forma padrão de imagens e thumbnails. */
    val image: CornerBasedShape,
    /** Forma de `MnsAvatar`. */
    val avatar: CornerBasedShape,
    /** Forma de `MnsBadge`. */
    val badge: CornerBasedShape,
    /** Forma da moldura de `MnsQrCode`. */
    val qrFrame: CornerBasedShape,
    /** Forma dos blocos de `MnsShimmer`. */
    val shimmer: CornerBasedShape,

    // ── Metadados ────────────────────────────────────────────────────────────
    /**
     * Raio que originou a escala. Não afeta a renderização: existe para que o
     * Design Contract consiga exportar a escala de volta para JSON sem precisar
     * inspecionar `Shape`, que depende de densidade e não é reversível.
     */
    val baseRadius: Dp = 16.dp,

    /** Raio dos botões, pelo mesmo motivo de [baseRadius]. `null` = pílula. */
    val buttonRadius: Dp? = 12.dp,
) {
    /** Resolve um papel de forma pelo nome — usado pelo Design Contract. */
    public fun byName(name: String): Shape = when (name.lowercase()) {
        "none" -> none
        "extrasmall", "xs" -> extraSmall
        "small", "sm" -> small
        "medium", "md" -> medium
        "large", "lg" -> large
        "extralarge", "xl" -> extraLarge
        "huge", "xxl" -> huge
        "full", "pill", "circle" -> full
        "button" -> button
        "input" -> input
        "card" -> card
        "chip" -> chip
        "bottomsheet" -> bottomSheet
        "dialog" -> dialog
        "image" -> image
        "avatar" -> avatar
        "badge" -> badge
        else -> error("Forma desconhecida: '$name'.")
    }

    public companion object {
        /**
         * Constrói a escala inteira a partir de um raio-base, mantendo as
         * proporções do MNS. É o caminho de menor esforço para tokenizar um
         * design novo: identifique o raio dos cards no print e passe aqui.
         *
         * @param base raio de referência (o dos cards). Padrão 16dp.
         * @param buttonRadius raio próprio dos botões. `null` deriva de [base].
         *   Ignorado quando [pillButtons] é `true`.
         * @param pillButtons botões em pílula (50%). Muitos designs usam pílula
         *   nos botões e cantos moderados nos cards.
         */
        public fun fromBaseRadius(
            base: Dp = 16.dp,
            buttonRadius: Dp? = null,
            pillButtons: Boolean = false,
        ): MnsShapes {
            val xs = (base * 0.25f).coerceAtLeast(2.dp)
            val sm = (base * 0.5f).coerceAtLeast(4.dp)
            val md = (base * 0.75f).coerceAtLeast(6.dp)
            val lg = base
            val xl = base * 1.5f
            val xxl = base * 2f
            val pill = RoundedCornerShape(percent = 50)
            val resolvedButtonRadius = if (pillButtons) null else (buttonRadius ?: md)
            return MnsShapes(
                none = RoundedCornerShape(0.dp),
                extraSmall = RoundedCornerShape(xs),
                small = RoundedCornerShape(sm),
                medium = RoundedCornerShape(md),
                large = RoundedCornerShape(lg),
                extraLarge = RoundedCornerShape(xl),
                huge = RoundedCornerShape(xxl),
                full = pill,
                button = resolvedButtonRadius?.let { RoundedCornerShape(it) } ?: pill,
                input = RoundedCornerShape(md),
                card = RoundedCornerShape(lg),
                chip = pill,
                bottomSheet = RoundedCornerShape(topStart = xl, topEnd = xl),
                dialog = RoundedCornerShape(xl),
                image = RoundedCornerShape(lg),
                avatar = pill,
                badge = pill,
                qrFrame = RoundedCornerShape(lg),
                shimmer = RoundedCornerShape(sm),
                baseRadius = base,
                buttonRadius = resolvedButtonRadius,
            )
        }
    }
}
