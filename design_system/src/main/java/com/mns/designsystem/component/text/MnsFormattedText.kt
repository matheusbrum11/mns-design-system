package com.mns.designsystem.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.mns.designsystem.format.MnsCompactNumberFormatter
import com.mns.designsystem.format.MnsCurrencyFormat
import com.mns.designsystem.format.MnsCurrencyFormatter
import com.mns.designsystem.format.MnsPercentFormat
import com.mns.designsystem.format.MnsPercentFormatter
import com.mns.designsystem.theme.LocalMnsTextStyle
import com.mns.designsystem.theme.MnsTheme
import java.util.Locale

/**
 * Exibe um valor monetário já formatado, recebendo **centavos**.
 *
 * Usar `Long` de centavos em vez de `Double` não é preciosismo: é o que impede
 * o erro de arredondamento aparecer no total do carrinho.
 *
 * ```kotlin
 * MnsCurrencyText(cents = 12550)  // "R$ 125,50"
 * ```
 *
 * @param cents valor em centavos. Negativo é aceito e formatado conforme
 *   [MnsCurrencyFormat.negativeStyle].
 * @param format configuração de moeda/locale.
 * @param style papel tipográfico; default herda do container.
 * @param color cor do texto. Se `Color.Unspecified` e [colorizeSign] for `true`,
 *   valores negativos ficam em `colors.danger` e positivos em `colors.success`.
 * @param colorizeSign pinta o valor conforme o sinal. Desligado por padrão —
 *   ligue apenas em contextos de saldo/variação, nunca em preço.
 * @param emphasizeSymbol renderiza o símbolo da moeda menor que o número, num
 *   peso mais leve. É o tratamento usado nos cards de preço dos designs.
 */
@Composable
public fun MnsCurrencyText(
    cents: Long,
    modifier: Modifier = Modifier,
    format: MnsCurrencyFormat = MnsCurrencyFormat.BRL,
    style: TextStyle = LocalMnsTextStyle.current,
    color: Color = Color.Unspecified,
    colorizeSign: Boolean = false,
    emphasizeSymbol: Boolean = false,
) {
    val colors = MnsTheme.colors
    val formatted = remember(cents, format) { MnsCurrencyFormatter.formatCents(cents, format) }
    val resolvedColor = when {
        color != Color.Unspecified -> color
        !colorizeSign -> Color.Unspecified
        cents < 0 -> colors.danger
        cents > 0 -> colors.success
        else -> Color.Unspecified
    }

    if (!emphasizeSymbol) {
        MnsText(
            text = formatted,
            modifier = modifier.clearAndSetSemantics { contentDescription = formatted },
            style = style,
            color = resolvedColor,
        )
        return
    }

    // Separa o prefixo não numérico (símbolo/sinal) do corpo numérico.
    val splitIndex = formatted.indexOfFirst(Char::isDigit).coerceAtLeast(0)
    val symbol = formatted.take(splitIndex).trim()
    val amount = formatted.drop(splitIndex)
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = formatted },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xxs),
    ) {
        if (symbol.isNotEmpty()) {
            MnsText(
                text = symbol,
                style = style.copy(
                    fontSize = style.fontSize * 0.65f,
                    fontWeight = FontWeight.Medium,
                ),
                color = resolvedColor,
            )
        }
        MnsText(text = amount, style = style, color = resolvedColor)
    }
}

/**
 * Exibe um percentual formatado.
 *
 * ```kotlin
 * MnsPercentText(value = 0.184, format = MnsPercentFormat.Signed) // "+18,4%"
 * ```
 *
 * @param value fração (`0.42`) ou percentual (`42.0`), conforme
 *   [MnsPercentFormat.inputIsFraction].
 * @param colorizeSign pinta positivo em `success` e negativo em `danger` —
 *   o comportamento esperado em indicadores de variação.
 */
@Composable
public fun MnsPercentText(
    value: Double,
    modifier: Modifier = Modifier,
    format: MnsPercentFormat = MnsPercentFormat.Default,
    style: TextStyle = LocalMnsTextStyle.current,
    color: Color = Color.Unspecified,
    colorizeSign: Boolean = false,
) {
    val colors = MnsTheme.colors
    val formatted = remember(value, format) { MnsPercentFormatter.format(value, format) }
    val resolvedColor = when {
        color != Color.Unspecified -> color
        !colorizeSign -> Color.Unspecified
        value < 0 -> colors.danger
        value > 0 -> colors.success
        else -> Color.Unspecified
    }
    MnsText(
        text = formatted,
        modifier = modifier.clearAndSetSemantics { contentDescription = formatted },
        style = style,
        color = resolvedColor,
    )
}

/**
 * Número grande em forma compacta (`1,2 mil`), com o valor completo exposto na
 * semântica para leitores de tela.
 *
 * @param value número a exibir.
 * @param threshold abaixo deste valor exibe o número por extenso.
 */
@Composable
public fun MnsCompactNumberText(
    value: Long,
    modifier: Modifier = Modifier,
    locale: Locale = Locale("pt", "BR"),
    threshold: Long = 1_000L,
    style: TextStyle = LocalMnsTextStyle.current,
    color: Color = Color.Unspecified,
) {
    val formatted = remember(value, locale, threshold) {
        MnsCompactNumberFormatter.format(value, locale, threshold)
    }
    MnsText(
        text = formatted,
        modifier = modifier.clearAndSetSemantics { contentDescription = value.toString() },
        style = style,
        color = color,
    )
}
