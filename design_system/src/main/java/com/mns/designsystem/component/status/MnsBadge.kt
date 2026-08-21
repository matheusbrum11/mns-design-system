package com.mns.designsystem.component.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/**
 * Contador ou ponto de notificação, ancorado a outro elemento.
 *
 * ```kotlin
 * MnsBadgedBox(badge = { MnsBadge(count = 3) }) {
 *     MnsIcon(Icons.Filled.Notifications, "Notificações")
 * }
 * ```
 *
 * @param count valor exibido. `null` renderiza apenas o ponto — use quando
 *   existe novidade mas o número exato não importa.
 * @param max acima deste valor exibe `"$max+"`. Impede o badge de esticar e
 *   quebrar o alinhamento do ícone.
 * @param status intenção semântica; define a cor.
 * @param contentDescription texto anunciado por leitores de tela. O default é o
 *   próprio rótulo (`"12"`, `"99+"`), porque um badge pode contar qualquer
 *   coisa — mensagens, itens no carrinho, filtros ativos. Diga o que ele conta.
 */
@Composable
public fun MnsBadge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    max: Int = 99,
    status: MnsStatus = MnsStatus.DANGER,
    contentDescription: String? = null,
) {
    val (container, content) = MnsTheme.colors.solidFor(status)
    val label = when {
        count == null -> null
        count > max -> "$max+"
        else -> count.toString()
    }

    if (label == null) {
        Box(
            modifier = modifier
                .size(8.dp)
                .background(container, CircleShape)
                .clearAndSetSemantics {
                    this.contentDescription = contentDescription ?: "Novidade"
                },
        )
        return
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
            .background(container, MnsTheme.shapes.badge)
            .padding(horizontal = 5.dp)
            .clearAndSetSemantics { this.contentDescription = contentDescription ?: label },
        contentAlignment = Alignment.Center,
    ) {
        MnsText(text = label, style = MnsTheme.typography.labelSmall, color = content)
    }
}

/**
 * Ancora um [MnsBadge] no canto superior direito de [content].
 *
 * @param badge slot do badge.
 * @param content elemento decorado (ícone, avatar, aba).
 */
@Composable
public fun MnsBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        content()
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 6.dp, y = (-6).dp),
        ) {
            badge()
        }
    }
}

/**
 * Rótulo estático de estado — "Confirmado", "Pendente", "Esgotado".
 *
 * Diferente de `MnsChip`, uma tag **não é interativa**: ela descreve, não filtra.
 * Se o usuário pode tocar para mudar algo, o componente certo é o chip.
 *
 * @param text texto da tag.
 * @param status intenção semântica; define o par de cores.
 * @param icon ícone opcional antes do texto.
 * @param solid usa a cor sólida em vez do container suave. Reserve para o caso
 *   em que a tag precisa competir com uma imagem de fundo.
 */
@Composable
public fun MnsTag(
    text: String,
    modifier: Modifier = Modifier,
    status: MnsStatus = MnsStatus.NEUTRAL,
    icon: ImageVector? = null,
    solid: Boolean = false,
    shape: Shape = MnsTheme.shapes.chip,
) {
    val colors = MnsTheme.colors
    val (container, content) = if (solid) colors.solidFor(status) else colors.containerFor(status)

    Row(
        modifier = modifier
            .background(container, shape)
            .padding(horizontal = MnsTheme.spacing.sm, vertical = MnsTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xs),
    ) {
        if (icon != null) {
            MnsIcon(
                imageVector = icon,
                contentDescription = null,
                size = MnsTheme.sizing.iconXs,
                tint = content,
            )
        }
        MnsText(text = text, style = MnsTheme.typography.labelSmall, color = content)
    }
}
