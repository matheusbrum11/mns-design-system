package com.mns.designsystem.component.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.loading.mnsShimmer
import com.mns.designsystem.theme.MnsTheme

/**
 * Área de imagem de capa, com placeholder, véu de legibilidade e slot de
 * conteúdo sobreposto.
 *
 * O véu ([scrim]) não é decoração: texto branco sobre foto arbitrária falha
 * contraste em metade das imagens. O gradiente resolve isso sem escurecer a
 * imagem inteira.
 *
 * @param painter imagem a exibir. `null` mostra o placeholder de carregamento.
 * @param contentDescription descrição da imagem; `null` a marca como decorativa.
 * @param height altura da capa.
 * @param scrim aplica gradiente escuro da base ao topo.
 * @param overlay conteúdo desenhado sobre a imagem (título, tags, botões).
 */
@Composable
public fun MnsCover(
    painter: Painter?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    height: Dp = MnsTheme.sizing.coverHeight,
    shape: Shape = MnsTheme.shapes.image,
    scrim: Boolean = false,
    placeholderColor: Color = MnsTheme.colors.surfaceVariant,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(placeholderColor),
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .let {
                        if (contentDescription != null) {
                            it.semantics { this.contentDescription = contentDescription }
                        } else {
                            it
                        }
                    },
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().mnsShimmer(shape = shape))
        }

        if (scrim) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MnsTheme.colors.overlay.copy(alpha = MnsTheme.opacity.overlay),
                            ),
                        ),
                    ),
            )
        }

        if (overlay != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                overlay()
            }
        }
    }
}
