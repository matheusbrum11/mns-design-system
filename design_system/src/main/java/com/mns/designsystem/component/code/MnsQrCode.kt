package com.mns.designsystem.component.code

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Matriz booleana de um QR Code: `true` = módulo escuro.
 *
 * O componente de exibição depende apenas deste tipo, nunca de uma biblioteca
 * de codificação específica. Assim o app pode gerar a matriz no backend, com
 * ZXing, ou com qualquer outro codificador, e o design system continua sendo
 * responsável só pela aparência.
 *
 * @property size lado da matriz em módulos.
 */
@Immutable
public class MnsQrMatrix(
    public val size: Int,
    private val modules: BooleanArray,
) {
    init {
        require(size > 0) { "A matriz do QR precisa ter lado maior que zero." }
        require(modules.size == size * size) {
            "Esperado ${size * size} módulos para um QR de lado $size, recebido ${modules.size}."
        }
    }

    /** `true` se o módulo em ([x], [y]) é escuro. Fora dos limites devolve `false`. */
    public operator fun get(x: Int, y: Int): Boolean =
        if (x in 0 until size && y in 0 until size) modules[y * size + x] else false

    public companion object {
        /**
         * Constrói a matriz a partir de linhas de texto, onde qualquer caractere
         * diferente de espaço/`0`/`.` conta como módulo escuro. É o formato
         * usado nos testes e nos previews — legível no diff.
         */
        public fun fromRows(rows: List<String>): MnsQrMatrix {
            require(rows.isNotEmpty()) { "Informe ao menos uma linha." }
            val side = rows.size
            require(rows.all { it.length == side }) { "A matriz precisa ser quadrada." }
            val flags = BooleanArray(side * side)
            rows.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    flags[y * side + x] = c != ' ' && c != '0' && c != '.'
                }
            }
            return MnsQrMatrix(side, flags)
        }
    }
}

/** Forma de cada módulo do QR. Afeta só a estética, nunca a leitura. */
public enum class MnsQrDotStyle {
    /** Módulos quadrados — leitura mais robusta em impressão. */
    SQUARE,

    /** Cantos levemente arredondados. */
    ROUNDED,

    /** Círculos — visual mais leve, exige contraste alto. */
    DOT,
}

/**
 * Exibe um QR Code a partir de uma [MnsQrMatrix].
 *
 * ```kotlin
 * val matriz = MnsQrEncoder.encode("MNS-TICKET-8842")
 * MnsQrCode(matrix = matriz, caption = "Apresente na entrada")
 * ```
 *
 * @param matrix módulos do código.
 * @param size lado da área de desenho.
 * @param foreground cor dos módulos escuros.
 * @param background cor do fundo. **Mantenha alto contraste** — leitores falham
 *   abaixo de ~3:1, e QR "estilizado" ilegível é pior que QR feio.
 * @param dotStyle forma dos módulos.
 * @param quietZoneModules margem branca ao redor, em módulos. O padrão ISO é 4;
 *   abaixo disso a taxa de leitura cai.
 * @param caption legenda abaixo do código.
 * @param contentDescription descrição para leitores de tela. Descreva **o que o
 *   código representa** ("Ingresso do evento X"), não "QR Code".
 */
@Composable
public fun MnsQrCode(
    matrix: MnsQrMatrix,
    modifier: Modifier = Modifier,
    size: Dp = MnsTheme.sizing.qrSize,
    foreground: Color = MnsTheme.colors.onSurface,
    background: Color = MnsTheme.colors.surface,
    dotStyle: MnsQrDotStyle = MnsQrDotStyle.SQUARE,
    quietZoneModules: Int = 4,
    shape: Shape = MnsTheme.shapes.qrFrame,
    caption: String? = null,
    contentDescription: String = "Código QR",
    logo: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
    ) {
        MnsSurface(
            shape = shape,
            color = background,
            contentColor = foreground,
            elevation = MnsTheme.elevation.level0,
        ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .padding(MnsTheme.spacing.md),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(
                    modifier = Modifier
                        .size(size)
                        .semantics { this.contentDescription = contentDescription },
                ) {
                    val total = matrix.size + quietZoneModules * 2
                    val module = this.size.minDimension / total
                    val origin = quietZoneModules * module
                    for (y in 0 until matrix.size) {
                        for (x in 0 until matrix.size) {
                            if (!matrix[x, y]) continue
                            val left = origin + x * module
                            val top = origin + y * module
                            when (dotStyle) {
                                MnsQrDotStyle.SQUARE -> drawRect(
                                    color = foreground,
                                    topLeft = Offset(left, top),
                                    size = Size(module, module),
                                )
                                MnsQrDotStyle.ROUNDED -> drawRoundRect(
                                    color = foreground,
                                    topLeft = Offset(left, top),
                                    size = Size(module, module),
                                    cornerRadius = CornerRadius(module * 0.3f, module * 0.3f),
                                )
                                MnsQrDotStyle.DOT -> drawCircle(
                                    color = foreground,
                                    radius = module * 0.45f,
                                    center = Offset(left + module / 2f, top + module / 2f),
                                )
                            }
                        }
                    }
                }
                if (logo != null) logo()
            }
        }
        if (caption != null) {
            MnsText(
                text = caption,
                style = MnsTheme.typography.bodySmall,
                color = MnsTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Sobrecarga que codifica [content] usando o [MnsQrEncoder] padrão.
 *
 * Prefira esta versão em telas simples. Em listas, gere a matriz uma vez fora
 * da composição — codificar QR em cada recomposição de item é caro.
 *
 * @param content texto a codificar (URL, id de ingresso, payload de check-in).
 * @param errorCorrection nível de correção de erro — ver [MnsQrErrorCorrection].
 */
@Composable
public fun MnsQrCode(
    content: String,
    modifier: Modifier = Modifier,
    size: Dp = MnsTheme.sizing.qrSize,
    errorCorrection: MnsQrErrorCorrection = MnsQrErrorCorrection.MEDIUM,
    foreground: Color = MnsTheme.colors.onSurface,
    background: Color = MnsTheme.colors.surface,
    dotStyle: MnsQrDotStyle = MnsQrDotStyle.SQUARE,
    quietZoneModules: Int = 4,
    shape: Shape = MnsTheme.shapes.qrFrame,
    caption: String? = null,
    contentDescription: String = "Código QR",
    logo: (@Composable () -> Unit)? = null,
) {
    val matrix = remember(content, errorCorrection) {
        MnsQrEncoder.encode(content, errorCorrection)
    }
    MnsQrCode(
        matrix = matrix,
        modifier = modifier,
        size = size,
        foreground = foreground,
        background = background,
        dotStyle = dotStyle,
        quietZoneModules = quietZoneModules,
        shape = shape,
        caption = caption,
        contentDescription = contentDescription,
        logo = logo,
    )
}
