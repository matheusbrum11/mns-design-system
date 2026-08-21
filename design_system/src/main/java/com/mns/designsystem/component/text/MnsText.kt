package com.mns.designsystem.component.text

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.mns.designsystem.theme.LocalMnsTextStyle

/**
 * Texto base do design system.
 *
 * Toda string visível no app deveria passar por aqui, e não por `Text` do
 * Material. Três motivos:
 *
 * 1. o estilo default vem de [LocalMnsTextStyle], então um container pode
 *    definir a tipografia dos filhos sem repetir parâmetro;
 * 2. a cor cai para `LocalContentColor`, o que faz o texto se adaptar sozinho
 *    ao trocar de superfície (card claro → banner escuro);
 * 3. centraliza o ponto onde plugaríamos i18n, pseudo-localização ou auditoria
 *    de strings hard-coded.
 *
 * @param text conteúdo a exibir.
 * @param style papel tipográfico. Use `MnsTheme.typography.*`.
 * @param color cor do texto. `Color.Unspecified` (default) herda do contexto.
 * @param maxLines número máximo de linhas antes de aplicar [overflow].
 * @param minLines número mínimo de linhas reservadas — evita "pulo" de layout
 *   quando o texto chega de forma assíncrona.
 * @param overflow o que fazer quando o texto não cabe.
 * @param textAlign alinhamento horizontal; `null` usa o do [style].
 * @param decoration sublinhado/tachado; útil para preço "de/por".
 * @param softWrap se `false`, o texto nunca quebra linha.
 * @param onTextLayout callback com as métricas medidas — usado por componentes
 *   que precisam saber se houve truncamento (ex.: botão "ver mais").
 */
@Composable
public fun MnsText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMnsTextStyle.current,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    decoration: TextDecoration? = null,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.resolve(color, textAlign, decoration),
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
    )
}

/**
 * Sobrecarga de [MnsText] para [AnnotatedString] — use quando parte do texto
 * tem estilo próprio (destaque de busca, "43 resultados" em negrito, link).
 */
@Composable
public fun MnsText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMnsTextStyle.current,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    decoration: TextDecoration? = null,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.resolve(color, textAlign, decoration),
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
    )
}

@Composable
private fun TextStyle.resolve(
    color: Color,
    textAlign: TextAlign?,
    decoration: TextDecoration?,
): TextStyle {
    val resolvedColor = when {
        color.isSpecified -> color
        this.color.isSpecified -> this.color
        else -> LocalContentColor.current
    }
    return copy(
        color = resolvedColor,
        textAlign = textAlign ?: this.textAlign,
        textDecoration = decoration ?: this.textDecoration,
    )
}

private val Color.isSpecified: Boolean get() = this != Color.Unspecified
