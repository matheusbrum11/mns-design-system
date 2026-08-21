package com.mns.designsystem.format

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Exibe o conteúdo bruto de um campo (só dígitos) já formatado como moeda,
 * mantendo o cursor sempre ao final.
 *
 * O estado guardado continua sendo a string de dígitos — a formatação é
 * puramente visual. Isso evita o bug clássico de reparsear a máscara e perder
 * precisão a cada tecla.
 */
public class MnsCurrencyVisualTransformation(
    private val format: MnsCurrencyFormat = MnsCurrencyFormat.BRL,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val cents = MnsCurrencyFormatter.parseDigitsToCents(text.text)
        val formatted = MnsCurrencyFormatter.formatCents(cents, format)
        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = FixedEndOffsetMapping(
                originalLength = text.text.length,
                transformedLength = formatted.length,
            ),
        )
    }

    override fun equals(other: Any?): Boolean =
        other is MnsCurrencyVisualTransformation && other.format == format

    override fun hashCode(): Int = format.hashCode()
}

/**
 * Aplica uma máscara posicional a um campo de texto.
 *
 * O caractere `#` de [mask] é substituído por um dígito/letra digitado; todo o
 * resto é literal inserido automaticamente.
 *
 * ```kotlin
 * MnsMaskVisualTransformation("###.###.###-##")  // CPF
 * MnsMaskVisualTransformation("(##) #####-####") // telefone
 * MnsMaskVisualTransformation("##/##")           // validade de cartão
 * ```
 */
public class MnsMaskVisualTransformation(
    private val mask: String,
    private val placeholder: Char = '#',
) : VisualTransformation {

    /** Quantidade de caracteres que o usuário precisa digitar para preencher a máscara. */
    public val inputLength: Int = mask.count { it == placeholder }

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.take(inputLength)
        val builder = StringBuilder()
        var rawIndex = 0
        for (maskChar in mask) {
            if (rawIndex >= raw.length) break
            if (maskChar == placeholder) {
                builder.append(raw[rawIndex])
                rawIndex++
            } else {
                builder.append(maskChar)
            }
        }
        val out = builder.toString()
        return TransformedText(AnnotatedString(out), MaskOffsetMapping(mask, placeholder, raw.length, out.length))
    }

    override fun equals(other: Any?): Boolean =
        other is MnsMaskVisualTransformation && other.mask == mask && other.placeholder == placeholder

    override fun hashCode(): Int = 31 * mask.hashCode() + placeholder.hashCode()
}

/** Mapeamento que ancora o cursor no fim — usado pela transformação de moeda. */
internal class FixedEndOffsetMapping(
    private val originalLength: Int,
    private val transformedLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = transformedLength
    override fun transformedToOriginal(offset: Int): Int = originalLength
}

/** Mapeamento posicional para [MnsMaskVisualTransformation]. */
internal class MaskOffsetMapping(
    private val mask: String,
    private val placeholder: Char,
    private val rawLength: Int,
    private val transformedLength: Int,
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 0) return 0
        var consumed = 0
        mask.forEachIndexed { index, c ->
            if (c == placeholder) {
                consumed++
                if (consumed == offset) return (index + 1).coerceAtMost(transformedLength)
            }
        }
        return transformedLength
    }

    override fun transformedToOriginal(offset: Int): Int {
        val clamped = offset.coerceIn(0, mask.length)
        val consumed = mask.take(clamped).count { it == placeholder }
        return consumed.coerceAtMost(rawLength)
    }
}
