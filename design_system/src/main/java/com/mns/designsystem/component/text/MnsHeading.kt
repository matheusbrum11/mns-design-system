package com.mns.designsystem.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.mns.designsystem.theme.MnsTheme

/**
 * Nível hierárquico de um [MnsHeading]. Mapeia 1:1 nos papéis tipográficos e,
 * mais importante, é o que alimenta a semântica de acessibilidade — leitores de
 * tela navegam por títulos, e usar o nível errado quebra essa navegação.
 */
public enum class MnsHeadingLevel {
    /** Título-herói de tela. Um por tela, no máximo. */
    DISPLAY,

    /** Título principal da tela. */
    H1,

    /** Título de seção. */
    H2,

    /** Título de subseção. */
    H3,

    /** Título de bloco menor (dentro de card). */
    H4,
}

/**
 * Título com semântica de cabeçalho.
 *
 * @param text texto do título.
 * @param level nível hierárquico — ver [MnsHeadingLevel].
 * @param overline rótulo curto em caixa alta acima do título (categoria, seção).
 * @param subtitle linha de apoio abaixo do título.
 * @param maxLines truncamento do título.
 * @param subtitleMaxLines truncamento do subtítulo. Existe para que um
 *   cabeçalho espremido por um irmão largo trunque em vez de crescer
 *   indefinidamente em altura.
 */
@Composable
public fun MnsHeading(
    text: String,
    modifier: Modifier = Modifier,
    level: MnsHeadingLevel = MnsHeadingLevel.H2,
    overline: String? = null,
    subtitle: String? = null,
    maxLines: Int = Int.MAX_VALUE,
    subtitleMaxLines: Int = 3,
) {
    val typography = MnsTheme.typography
    val colors = MnsTheme.colors
    val style = when (level) {
        MnsHeadingLevel.DISPLAY -> typography.displayMedium
        MnsHeadingLevel.H1 -> typography.headlineLarge
        MnsHeadingLevel.H2 -> typography.headlineMedium
        MnsHeadingLevel.H3 -> typography.headlineSmall
        MnsHeadingLevel.H4 -> typography.titleLarge
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xxs),
    ) {
        if (overline != null) {
            MnsText(
                text = overline.uppercase(),
                style = typography.overline,
                color = colors.textTertiary,
            )
        }
        MnsText(
            text = text,
            modifier = Modifier.semantics { heading() },
            style = style,
            color = colors.textPrimary,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        if (subtitle != null) {
            MnsText(
                text = subtitle,
                style = typography.bodyMedium,
                color = colors.textSecondary,
                maxLines = subtitleMaxLines,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * Cabeçalho de seção com ação à direita — o padrão "Today Events … See All".
 *
 * @param title título da seção.
 * @param subtitle linha de apoio opcional.
 * @param action slot da ação à direita. Costuma ser um `MnsButton` `TEXT`.
 */
@Composable
public fun MnsSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    level: MnsHeadingLevel = MnsHeadingLevel.H2,
    action: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
    ) {
        MnsHeading(
            text = title,
            modifier = Modifier.weight(1f),
            level = level,
            subtitle = subtitle,
            maxLines = 2,
        )
        if (action != null) action()
    }
}
