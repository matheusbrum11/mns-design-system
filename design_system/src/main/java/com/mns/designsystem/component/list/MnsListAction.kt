package com.mns.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * O que aparece no início de um [MnsListAction].
 *
 * É um tipo selado em vez de três parâmetros nuláveis para tornar impossível o
 * estado "avatar **e** ícone ao mesmo tempo", que renderizava layout quebrado.
 */
public sealed interface MnsListLeading {
    /** Sem elemento inicial — o texto começa na margem. */
    public data object None : MnsListLeading

    /** Avatar com iniciais/foto. */
    public data class Avatar(
        val name: String,
        val painter: Painter? = null,
        val size: Dp? = null,
    ) : MnsListLeading

    /** Ícone dentro de um container arredondado. */
    public data class Icon(
        val imageVector: ImageVector,
        val tint: Color? = null,
        val containerColor: Color? = null,
    ) : MnsListLeading

    /** Miniatura de imagem (capa de evento, thumbnail). */
    public data class Thumbnail(
        val painter: Painter,
        val size: Dp? = null,
    ) : MnsListLeading

    /** Slot totalmente livre. */
    public data class Custom(val content: @Composable () -> Unit) : MnsListLeading
}

/**
 * Item de lista acionável, altamente configurável.
 *
 * Cobre os padrões que aparecem nos três designs de referência: linha simples
 * de configuração, linha com avatar e metadados, e card de evento com
 * miniatura, participantes e ação à direita.
 *
 * ```kotlin
 * MnsListAction(
 *     title = "North Van Hiking",
 *     overline = "Mount Seymour",
 *     subtitle = "Vancouver Community Centre",
 *     meta = "MAR 20 · 8:30 AM PDT",
 *     leading = MnsListLeading.Avatar("North Van"),
 *     trailing = { MnsAvatarGroup(participantes) },
 *     onClick = ::abrirEvento,
 * )
 * ```
 *
 * @param title texto principal. Obrigatório.
 * @param overline rótulo pequeno acima do título (categoria, local).
 * @param subtitle linha de apoio abaixo do título.
 * @param meta terceira linha, para data/hora/estado.
 * @param leading elemento inicial — ver [MnsListLeading].
 * @param trailing slot final (badge, switch, grupo de avatares, preço).
 * @param onClick torna a linha acionável.
 * @param showChevron exibe a seta ">" indicando navegação. Só faz sentido com
 *   [onClick] definido e sem [trailing] concorrente.
 * @param selected pinta a linha com a cor de container selecionado.
 * @param containerColor sobrescreve a cor de fundo da linha.
 * @param contentPadding padding interno.
 */
@Composable
public fun MnsListAction(
    title: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    subtitle: String? = null,
    meta: String? = null,
    leading: MnsListLeading = MnsListLeading.None,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    showChevron: Boolean = false,
    selected: Boolean = false,
    containerColor: Color? = null,
    shape: Shape = MnsTheme.shapes.card,
    contentPadding: Dp = MnsTheme.spacing.listItemPadding,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val typography = MnsTheme.typography

    val container = containerColor ?: if (selected) colors.accentContainer else Color.Transparent
    val onContainer = if (selected) colors.onAccentContainer else colors.onSurface

    MnsSurface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = container,
        contentColor = onContainer,
        onClick = onClick,
        enabled = enabled,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = MnsTheme.sizing.listItemHeight)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            MnsListLeadingSlot(leading = leading, enabled = enabled)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                if (overline != null) {
                    MnsText(
                        text = overline,
                        style = typography.caption,
                        color = colors.textTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                MnsText(
                    text = title,
                    style = typography.titleMedium,
                    color = if (enabled) onContainer else colors.textDisabled,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle != null) {
                    MnsText(
                        text = subtitle,
                        style = typography.bodySmall,
                        color = colors.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (meta != null) {
                    MnsText(
                        text = meta,
                        style = typography.caption,
                        color = colors.textTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (trailing != null) trailing()

            if (showChevron) {
                MnsIcon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    size = MnsTheme.sizing.iconMd,
                    tint = colors.textTertiary,
                )
            }
        }
    }
}

@Composable
private fun MnsListLeadingSlot(leading: MnsListLeading, enabled: Boolean) {
    val colors = MnsTheme.colors
    when (leading) {
        MnsListLeading.None -> Unit
        is MnsListLeading.Avatar -> MnsAvatar(
            name = leading.name,
            painter = leading.painter,
            size = leading.size ?: MnsTheme.sizing.avatarMd,
        )
        is MnsListLeading.Icon -> MnsSurface(
            modifier = Modifier.size(MnsTheme.sizing.avatarMd),
            shape = MnsTheme.shapes.medium,
            color = leading.containerColor ?: colors.surfaceVariant,
            contentColor = leading.tint ?: colors.onSurfaceVariant,
        ) {
            Box(
                modifier = Modifier.size(MnsTheme.sizing.avatarMd),
                contentAlignment = Alignment.Center,
            ) {
                MnsIcon(
                    imageVector = leading.imageVector,
                    contentDescription = null,
                    size = MnsTheme.sizing.iconMd,
                    tint = if (enabled) {
                        leading.tint ?: colors.onSurfaceVariant
                    } else {
                        colors.textDisabled
                    },
                )
            }
        }
        is MnsListLeading.Thumbnail -> Image(
            painter = leading.painter,
            contentDescription = null,
            modifier = Modifier
                .size(leading.size ?: 56.dp)
                .clip(MnsTheme.shapes.image),
            contentScale = ContentScale.Crop,
        )
        is MnsListLeading.Custom -> leading.content()
    }
}
