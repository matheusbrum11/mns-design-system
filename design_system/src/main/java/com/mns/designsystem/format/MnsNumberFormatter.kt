package com.mns.designsystem.format

import androidx.compose.runtime.Immutable
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

/**
 * Configuração de formatação percentual.
 *
 * @property locale define separador decimal e posição do `%`.
 * @property fractionDigits casas decimais exibidas.
 * @property showSign prefixa `+` em valores positivos — útil em variação.
 * @property inputIsFraction quando `true`, `0.42` vira `42%`; quando `false`,
 *   `42.0` vira `42%`. O default é `true` porque é o formato que APIs entregam.
 */
@Immutable
public data class MnsPercentFormat(
    val locale: Locale = Locale("pt", "BR"),
    val fractionDigits: Int = 1,
    val showSign: Boolean = false,
    val inputIsFraction: Boolean = true,
) {
    public companion object {
        /** `42,0%` */
        public val Default: MnsPercentFormat = MnsPercentFormat()

        /** `42%` — sem casas decimais. */
        public val Whole: MnsPercentFormat = MnsPercentFormat(fractionDigits = 0)

        /** `+3,2%` / `-1,8%` — para variação de indicador. */
        public val Signed: MnsPercentFormat = MnsPercentFormat(showSign = true)
    }
}

/** Formatador de percentuais. Ver [MnsPercentFormat] para as opções. */
public object MnsPercentFormatter {

    /**
     * ```kotlin
     * MnsPercentFormatter.format(0.4237)                        // "42,4%"
     * MnsPercentFormatter.format(0.032, MnsPercentFormat.Signed) // "+3,2%"
     * ```
     */
    public fun format(
        value: Double,
        format: MnsPercentFormat = MnsPercentFormat.Default,
    ): String {
        val fraction = if (format.inputIsFraction) value else value / 100.0
        val nf = NumberFormat.getPercentInstance(format.locale).apply {
            minimumFractionDigits = format.fractionDigits
            maximumFractionDigits = format.fractionDigits
            roundingMode = RoundingMode.HALF_UP
        }
        val body = nf.format(abs(fraction)).replace(' ', ' ').replace(' ', ' ').trim()
        return when {
            fraction < 0 -> "-$body"
            format.showSign -> "+$body"
            else -> body
        }
    }
}

/** Formatação compacta de números grandes (`1,2 mil`, `3,4 mi`). */
public object MnsCompactNumberFormatter {

    private val units = listOf(
        1_000_000_000L to "bi",
        1_000_000L to "mi",
        1_000L to "mil",
    )

    /**
     * ```kotlin
     * MnsCompactNumberFormatter.format(1_240)      // "1,2 mil"
     * MnsCompactNumberFormatter.format(980)        // "980"
     * ```
     *
     * @param threshold abaixo deste valor o número é exibido por extenso.
     */
    public fun format(
        value: Long,
        locale: Locale = Locale("pt", "BR"),
        threshold: Long = 1_000L,
    ): String {
        val magnitude = abs(value)
        if (magnitude < threshold) return NumberFormat.getIntegerInstance(locale).format(value)
        val (divisor, suffix) = units.first { magnitude >= it.first }
        val scaled = value.toDouble() / divisor
        val nf = NumberFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = if (abs(scaled) >= 100) 0 else 1
            minimumFractionDigits = 0
            roundingMode = RoundingMode.DOWN
        }
        return "${nf.format(scaled)} $suffix"
    }
}
