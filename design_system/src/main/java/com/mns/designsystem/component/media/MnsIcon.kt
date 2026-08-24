package com.mns.designsystem.component.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.theme.MnsTheme

/**
 * Ícone tingido pelo design system.
 *
 * A cor default é `LocalContentColor`, o que faz o ícone acompanhar
 * automaticamente a superfície onde está (card claro, botão primário, banner
 * escuro) sem que o chamador precise passar `tint`.
 *
 * @param imageVector vetor do ícone.
 * @param contentDescription descrição para leitores de tela. Passe `null`
 *   quando o ícone é decorativo e o significado já está no texto ao lado —
 *   descrever duas vezes a mesma coisa polui a navegação por voz.
 * @param size lado do ícone. Use um degrau de `MnsTheme.sizing.icon*`.
 * @param tint cor de preenchimento. `Color.Unspecified` desliga o tingimento,
 *   preservando ícones multicoloridos (bandeira, logo de bandeira de cartão).
 */
@Composable
public fun MnsIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = MnsTheme.sizing.iconMd,
    tint: Color = LocalContentColor.current,
) {
    MnsIcon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        tint = tint,
    )
}

/** Sobrecarga de [MnsIcon] para um [Painter] arbitrário. */
@Composable
public fun MnsIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = MnsTheme.sizing.iconMd,
    tint: Color = LocalContentColor.current,
) {
    val colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
    val semantics = if (contentDescription != null) {
        Modifier.semantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .then(modifier)
            .then(semantics),
        colorFilter = colorFilter,
    )
}

/**
 * Ícone carregado de uma **URL remota**.
 *
 * Existe para catálogos em que a categoria, a bandeira do cartão ou o logo do
 * parceiro vêm do backend — casos em que o vetor não está no APK.
 *
 * Diferença importante em relação às outras sobrecargas: aqui o [tint] é
 * `Color.Unspecified` por padrão, ou seja, **não tinge**. Arte remota costuma
 * ser colorida (logo de bandeira, avatar de parceiro), e tingir por engano
 * transformaria tudo em uma silhueta chapada. Passe [tint] explicitamente
 * quando souber que o recurso é monocromático.
 *
 * @param imageUrl endereço da imagem.
 * @param contentDescription descrição para leitores de tela; `null` para ícone
 *   decorativo cujo significado já está no texto ao lado.
 * @param size lado do ícone. Use um degrau de `MnsTheme.sizing.icon*`.
 * @param tint cor de preenchimento. O default **não** tinge — ver acima.
 * @param fallback vetor local desenhado quando a carga falha. Sem ele, a área
 *   fica vazia.
 */
@Composable
public fun MnsIcon(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = MnsTheme.sizing.iconMd,
    tint: Color = Color.Unspecified,
    fallback: ImageVector? = null,
) {
    MnsAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = Modifier.size(size).then(modifier),
        shape = MnsTheme.shapes.none,
        contentScale = ContentScale.Fit,
        colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint),
        fallback = if (fallback != null) {
            {
                MnsIcon(
                    imageVector = fallback,
                    contentDescription = null,
                    size = size,
                    tint = if (tint == Color.Unspecified) LocalContentColor.current else tint,
                )
            }
        } else {
            null
        },
    )
}

