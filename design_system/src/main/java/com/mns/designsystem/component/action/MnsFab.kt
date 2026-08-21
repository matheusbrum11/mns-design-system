package com.mns.designsystem.component.action

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Botão de ação flutuante.
 *
 * Existe para **uma** ação: a mais provável da tela. Duas FABs na mesma tela é
 * sinal de que a hierarquia não foi decidida.
 *
 * @param icon ícone da ação.
 * @param contentDescription descrição obrigatória para acessibilidade.
 * @param expanded quando `true` e [label] não é nulo, o FAB se expande em
 *   pílula mostrando o rótulo. Anime este valor com o scroll da lista para o
 *   comportamento "encolhe ao rolar".
 * @param label rótulo exibido no estado expandido.
 * @param containerColor cor de fundo; default `colors.primary`.
 */
@Composable
public fun MnsFab(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    label: String? = null,
    containerColor: Color = MnsTheme.colors.primary,
    contentColor: Color = MnsTheme.colors.onPrimary,
    size: Dp = MnsTheme.sizing.fabSize,
    shape: Shape = MnsTheme.shapes.full,
    elevation: Dp = MnsTheme.elevation.level3,
) {
    val spacing = MnsTheme.spacing
    val showLabel = expanded && label != null

    MnsSurface(
        modifier = modifier
            .height(size)
            .defaultMinSize(minWidth = size),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        onClick = onClick,
        role = Role.Button,
    ) {
        Row(
            modifier = Modifier
                .height(size)
                .padding(horizontal = if (showLabel) spacing.lg else MnsTheme.spacing.none),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterHorizontally),
        ) {
            MnsIcon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(MnsTheme.sizing.iconLg),
                tint = contentColor,
            )
            AnimatedVisibility(
                visible = showLabel,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally(),
            ) {
                if (label != null) {
                    MnsText(
                        text = label,
                        style = MnsTheme.typography.labelLarge,
                        color = contentColor,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
