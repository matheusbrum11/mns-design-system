package com.mns.designsystem.component.code

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Cartão de ingresso com QR Code.
 *
 * Composto proposital do design system: reúne cabeçalho, pares chave/valor,
 * picote tracejado e QR num único componente, porque o layout de ingresso é
 * repetido em toda tela do fluxo (compra, carteira, check-in) e divergir entre
 * elas confunde na hora de validar na portaria.
 *
 * ```kotlin
 * MnsTicketCard(
 *     title = "Newport Beach Jazz Festival",
 *     subtitle = "Sydney, Australia · 19 Oct 2024",
 *     details = listOf("Assento" to "D17, D18", "Portão" to "G2"),
 *     qrContent = "MNS-TICKET-8842",
 *     footnote = "Apresente este código na entrada",
 * )
 * ```
 *
 * @param title nome do evento.
 * @param subtitle local e data.
 * @param details pares rótulo/valor exibidos em grade de duas colunas.
 * @param qrContent conteúdo a codificar no QR.
 * @param footnote instrução abaixo do código.
 * @param errorCorrection nível de correção do QR. Use [MnsQrErrorCorrection.QUARTILE]
 *   ou mais alto se o ingresso puder ser impresso.
 * @param qrSize lado do QR.
 */
@Composable
public fun MnsTicketCard(
    title: String,
    qrContent: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    details: List<Pair<String, String>> = emptyList(),
    footnote: String? = null,
    errorCorrection: MnsQrErrorCorrection = MnsQrErrorCorrection.QUARTILE,
    qrSize: Dp = MnsTheme.sizing.qrSize,
    shape: Shape = MnsTheme.shapes.extraLarge,
    containerColor: Color = MnsTheme.colors.surface,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing

    MnsSurface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = containerColor,
        contentColor = colors.onSurface,
        elevation = MnsTheme.elevation.level2,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.base),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
                MnsText(
                    text = title,
                    style = MnsTheme.typography.headlineSmall,
                    color = colors.textPrimary,
                )
                if (subtitle != null) {
                    MnsText(
                        text = subtitle,
                        style = MnsTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                }
            }

            if (details.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    details.chunked(2).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(spacing.base),
                        ) {
                            row.forEach { (label, value) ->
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(spacing.xxs),
                                ) {
                                    MnsText(
                                        text = label,
                                        style = MnsTheme.typography.caption,
                                        color = colors.textTertiary,
                                    )
                                    MnsText(
                                        text = value,
                                        style = MnsTheme.typography.titleMedium,
                                        color = colors.textPrimary,
                                    )
                                }
                            }
                            if (row.size == 1) Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            MnsTicketPerforation()

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                MnsQrCode(
                    content = qrContent,
                    size = qrSize,
                    errorCorrection = errorCorrection,
                    caption = footnote,
                    contentDescription = "Código do ingresso para $title",
                    background = containerColor,
                )
            }
        }
    }
}

/** Linha tracejada que imita o picote de um ingresso físico. */
@Composable
public fun MnsTicketPerforation(
    modifier: Modifier = Modifier,
    color: Color = MnsTheme.colors.outline,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 6.dp,
    thickness: Dp = 1.dp,
) {
    Canvas(modifier = modifier.fillMaxWidth().height(thickness * 2)) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength.toPx(), gapLength.toPx()),
            ),
        )
    }
}
