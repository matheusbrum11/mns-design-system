package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * **Camada 1 — Reference tokens (tokens primitivos).**
 *
 * São valores crus de cor, sem nenhum significado de interface. Nada na UI deve
 * consumir um [MnsPalette] diretamente: a UI consome [MnsColors] (camada 2), que
 * por sua vez aponta para estes primitivos. Essa indireção é o que permite
 * trocar a identidade visual inteira sem tocar em um único componente.
 *
 * ```
 * MnsPalette (cru)  →  MnsColors (semântico)  →  Componente
 * #6255F4           →  colors.primary          →  MnsButton
 * ```
 *
 * Cada rampa segue a escala 0–100 do Material (0 = mais escuro, 100 = branco),
 * mas sem qualquer acoplamento com o Material: são apenas nomes de degrau.
 */
@Immutable
public data class MnsPalette(
    val brand: MnsColorRamp,
    val neutral: MnsColorRamp,
    val accent: MnsColorRamp,
    val success: MnsColorRamp,
    val warning: MnsColorRamp,
    val danger: MnsColorRamp,
    val info: MnsColorRamp,
) {
    public companion object
}

/**
 * Rampa de 11 degraus de uma mesma matiz. Os degraus são intencionalmente
 * poucos: rampas grandes demais viram um catálogo que ninguém consegue manter
 * coerente entre light e dark.
 *
 * @property s0 degrau mais escuro da rampa — usado como texto sobre superfícies claras.
 * @property s10 quase preto da matiz.
 * @property s20 tom profundo, bom para estados `pressed` em tema escuro.
 * @property s30 tom escuro de suporte.
 * @property s40 tom escuro-médio, geralmente o `onContainer` no tema claro.
 * @property s50 tom médio — normalmente a cor de marca em tema escuro.
 * @property s60 **degrau base**: é daqui que sai `colors.primary` no tema claro.
 * @property s70 tom claro-médio, bom para bordas e ícones secundários.
 * @property s80 tom claro, usado em `container` de tema claro.
 * @property s90 tom muito claro, fundos de destaque sutil.
 * @property s95 quase branco da matiz — fundo de página tingido.
 * @property s100 branco puro da rampa.
 */
@Immutable
public data class MnsColorRamp(
    val s0: Color,
    val s10: Color,
    val s20: Color,
    val s30: Color,
    val s40: Color,
    val s50: Color,
    val s60: Color,
    val s70: Color,
    val s80: Color,
    val s90: Color,
    val s95: Color,
    val s100: Color,
) {
    /** Retorna o degrau pelo número (0, 10, 20, … 100). Útil para o Design Contract. */
    public fun step(value: Int): Color = when (value) {
        0 -> s0
        10 -> s10
        20 -> s20
        30 -> s30
        40 -> s40
        50 -> s50
        60 -> s60
        70 -> s70
        80 -> s80
        90 -> s90
        95 -> s95
        100 -> s100
        else -> error("Degrau invalido: $value. Use 0,10,20,30,40,50,60,70,80,90,95,100.")
    }

    public companion object {
        /**
         * Cria uma rampa monocromática a partir de uma única cor, interpolando
         * em direção ao preto e ao branco. É o atalho usado pelo agente de
         * Design Contract quando o print fornece apenas a cor base da marca.
         */
        public fun fromSeed(seed: Color): MnsColorRamp = MnsColorRamp(
            s0 = seed.mixWith(Color.Black, 0.88f),
            s10 = seed.mixWith(Color.Black, 0.76f),
            s20 = seed.mixWith(Color.Black, 0.62f),
            s30 = seed.mixWith(Color.Black, 0.46f),
            s40 = seed.mixWith(Color.Black, 0.30f),
            s50 = seed.mixWith(Color.Black, 0.14f),
            s60 = seed,
            s70 = seed.mixWith(Color.White, 0.22f),
            s80 = seed.mixWith(Color.White, 0.46f),
            s90 = seed.mixWith(Color.White, 0.72f),
            s95 = seed.mixWith(Color.White, 0.86f),
            s100 = seed.mixWith(Color.White, 0.96f),
        )
    }
}

/** Mistura linear entre duas cores no espaço sRGB. [ratio] 0f = receiver, 1f = [other]. */
public fun Color.mixWith(other: Color, ratio: Float): Color {
    val t = ratio.coerceIn(0f, 1f)
    return Color(
        red = red + (other.red - red) * t,
        green = green + (other.green - green) * t,
        blue = blue + (other.blue - blue) * t,
        alpha = alpha + (other.alpha - alpha) * t,
    )
}

/**
 * Luminância relativa (WCAG 2.1). Base para [contrastRatio] e para decidir
 * automaticamente se o texto sobre uma cor deve ser claro ou escuro.
 */
public fun Color.relativeLuminance(): Float {
    fun channel(c: Float): Float =
        if (c <= 0.03928f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
    return 0.2126f * channel(red) + 0.7152f * channel(green) + 0.0722f * channel(blue)
}

/** Razão de contraste WCAG entre duas cores. Vai de 1.0 (idênticas) a 21.0 (preto/branco). */
public fun contrastRatio(a: Color, b: Color): Float {
    val la = a.relativeLuminance()
    val lb = b.relativeLuminance()
    val lighter = maxOf(la, lb)
    val darker = minOf(la, lb)
    return (lighter + 0.05f) / (darker + 0.05f)
}

/**
 * Escolhe entre [light] e [dark] o tom que tiver maior contraste sobre o
 * receiver. Usado por componentes que aceitam uma cor de fundo arbitrária e
 * ainda assim precisam garantir legibilidade.
 */
public fun Color.contentColorFor(
    light: Color = Color.White,
    dark: Color = Color.Black,
): Color = if (contrastRatio(this, light) >= contrastRatio(this, dark)) light else dark

private fun Float.pow(exp: Float): Float = Math.pow(toDouble(), exp.toDouble()).toFloat()
