package com.mns.designsystem.component.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import java.util.Locale
import kotlin.math.round

/**
 * Estado vazio de uma lista ou tela.
 *
 * Um estado vazio bom responde três perguntas: *o que aconteceu*, *por que* e
 * *o que eu faço agora*. Por isso [title] e [description] são separados e há um
 * slot dedicado para a [action] — um "Nenhum resultado" solto não ajuda ninguém.
 *
 * @param title frase curta do que está vazio.
 * @param description explicação e/ou próximo passo.
 * @param icon ilustração/ícone acima do texto.
 * @param action slot da ação de saída (ex.: "Limpar filtros").
 */
@Composable
public fun MnsEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.xl, vertical = spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(MnsTheme.sizing.avatarXl)
                    .padding(spacing.base),
                contentAlignment = Alignment.Center,
            ) {
                MnsIcon(
                    imageVector = icon,
                    contentDescription = null,
                    size = MnsTheme.sizing.iconXxl,
                    tint = colors.textTertiary,
                )
            }
        }
        MnsText(
            text = title,
            style = MnsTheme.typography.titleLarge,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        if (description != null) {
            MnsText(
                text = description,
                style = MnsTheme.typography.bodyMedium,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
        if (action != null) action()
    }
}

/**
 * Linha de avaliação por estrelas.
 *
 * @param rating nota atual, de 0 até [max].
 * @param max número de estrelas.
 * @param showValue exibe a nota numérica ao lado das estrelas.
 * @param onRatingChange quando não-nulo, torna as estrelas tocáveis.
 */
@Composable
public fun MnsRating(
    rating: Float,
    modifier: Modifier = Modifier,
    max: Int = 5,
    showValue: Boolean = true,
    onRatingChange: ((Int) -> Unit)? = null,
) {
    val colors = MnsTheme.colors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xxs),
    ) {
        repeat(max) { index ->
            val filled = index < round(rating).toInt()
            MnsIcon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = if (onRatingChange != null) {
                    Modifier.clickable { onRatingChange(index + 1) }
                } else {
                    Modifier
                },
                size = MnsTheme.sizing.iconSm,
                tint = if (filled) colors.warning else colors.outline,
            )
        }
        if (showValue) {
            MnsText(
                text = String.format(Locale.getDefault(), "%.1f", rating),
                modifier = Modifier.padding(start = MnsTheme.spacing.xs),
                style = MnsTheme.typography.labelSmall,
                color = colors.textSecondary,
            )
        }
    }
}
