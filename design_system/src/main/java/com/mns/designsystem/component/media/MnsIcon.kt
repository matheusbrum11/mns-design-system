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
