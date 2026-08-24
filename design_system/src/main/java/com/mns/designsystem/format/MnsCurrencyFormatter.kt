package com.mns.designsystem.format

import androidx.compose.runtime.Immutable
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Configuração de formatação monetária.
 *
 * Trabalhamos com **centavos em `Long`** como representação canônica. Isso é
 * deliberado: `Double` para dinheiro produz `0.1 + 0.2 == 0.30000000000000004`
 * e, em app de venda de ingresso, isso vira divergência de centavo no fechamento.
 *
 * @property locale locale usado para separador de milhar/decimal e posição do símbolo.
 * @property currencyCode código ISO-4217 (`BRL`, `USD`, `EUR`).
 * @property showSymbol quando `false`, formata só o número (`1.234,56`).
 * @property fractionDigits casas decimais; `null` usa o padrão da moeda.
 * @property negativeStyle como representar valores negativos.
 */
@Immutable
public data class MnsCurrencyFormat(
    val locale: Locale = Locale("pt", "BR"),
    val currencyCode: String = "BRL",
    val showSymbol: Boolean = true,
    val fractionDigits: Int? = null,
    val negativeStyle: MnsNegativeStyle = MnsNegativeStyle.MINUS_SIGN,
) {
    public companion object {
        /** Real brasileiro — `R$ 1.234,56`. */
        public val BRL: MnsCurrencyFormat = MnsCurrencyFormat()

        /** Dólar americano — `$1,234.56`. */
        public val USD: MnsCurrencyFormat =
            MnsCurrencyFormat(locale = Locale.US, currencyCode = "USD")

        /** Euro (Alemanha) — `1.234,56 €`. */
        public val EUR: MnsCurrencyFormat =
            MnsCurrencyFormat(locale = Locale.GERMANY, currencyCode = "EUR")
    }
}

/** Como um valor negativo é apresentado. */
public enum class MnsNegativeStyle {
    /** `-R$ 10,00` */
    MINUS_SIGN,

    /** `(R$ 10,00)` — convenção contábil. */
    PARENTHESES,

    /** `R$ 10,00` — o sinal é comunicado por cor, não por texto. */
    NONE,
}

/**
 * Formatador monetário do design system.
 *
 * É stateless e thread-safe: cada chamada cria seu próprio [NumberFormat],
 * porque `NumberFormat` **não** é thread-safe e compartilhá-lo entre
 * composições concorrentes produz strings corrompidas.
 */
public object MnsCurrencyFormatter {

    /**
     * Formata um valor em **centavos**.
     *
     * ```kotlin
     * MnsCurrencyFormatter.formatCents(123456)            // "R$ 1.234,56"
     * MnsCurrencyFormatter.formatCents(-500, MnsCurrencyFormat.USD) // "-$5.00"
     * ```
     */
    public fun formatCents(
        cents: Long,
        format: MnsCurrencyFormat = MnsCurrencyFormat.BRL,
    ): String = format(BigDecimal(cents).movePointLeft(2), format)

    /** Formata um [BigDecimal] já em unidades inteiras da moeda. */
    public fun format(
        value: BigDecimal,
        format: MnsCurrencyFormat = MnsCurrencyFormat.BRL,
    ): String {
        val currency = runCatching { Currency.getInstance(format.currencyCode) }.getOrNull()
        val digits = format.fractionDigits ?: currency?.defaultFractionDigits ?: 2
        val nf = if (format.showSymbol) {
            NumberFormat.getCurrencyInstance(format.locale).apply {
                if (currency != null) setCurrency(currency)
            }
        } else {
            NumberFormat.getNumberInstance(format.locale)
        }
        nf.minimumFractionDigits = digits
        nf.maximumFractionDigits = digits
        nf.roundingMode = RoundingMode.HALF_UP

        val absolute = value.abs()
        // O NBSP que o ICU insere entre símbolo e número quebra os asserts de
        // teste e a busca textual do usuário; normalizamos para espaço comum.
        val body = nf.format(absolute).replace(' ', ' ').replace(' ', ' ').trim()

        val isNegative = value.signum() < 0
        return when {
            !isNegative -> body
            format.negativeStyle == MnsNegativeStyle.PARENTHESES -> "($body)"
            format.negativeStyle == MnsNegativeStyle.NONE -> body
            else -> "-$body"
        }
    }

    /**
     * Extrai apenas os dígitos de [input] e os interpreta como centavos. É o
     * motor por trás de `MnsCurrencyField`: o usuário digita `1`, `2`, `3` e vê
     * `R$ 0,01`, `R$ 0,12`, `R$ 1,23`.
     *
     * @return o valor em centavos, saturado em [Long.MAX_VALUE] se o usuário
     *   colar um texto absurdamente longo.
     */
    public fun parseDigitsToCents(input: String): Long {
        val digits = input.filter(Char::isDigit)
        if (digits.isEmpty()) return 0L
        return digits.take(18).toLongOrNull() ?: Long.MAX_VALUE
    }
}
