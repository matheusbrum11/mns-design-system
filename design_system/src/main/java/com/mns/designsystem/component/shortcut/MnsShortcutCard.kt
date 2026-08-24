package com.mns.designsystem.component.shortcut

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.status.MnsBadge
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.foundation.mnsPressScale
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.MnsTheme

/**
 * Um atalho do [MnsShortcutGrid].
 *
 * @property id chave estável — usada como `key` da grade, evitando recomposição
 *   desnecessária ao reordenar.
 * @property label rótulo exibido.
 * @property icon ícone do atalho.
 * @property badgeCount contador exibido no canto; `null` esconde o badge.
 * @property enabled desabilita o atalho.
 */
public data class MnsShortcut(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int? = null,
    val enabled: Boolean = true,
)

/**
 * Card de atalho — o quadrado "Art / Business / Travel …" do design de eventos.
 *
 * @param shortcut dados do atalho.
 * @param selected estado de seleção; usa `primaryContainer` quando ativo.
 * @param onClick chamado ao tocar.
 * @param aspectRatio proporção do card. 1f = quadrado.
 */
@Composable
public fun MnsShortcutCard(
    shortcut: MnsShortcut,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    aspectRatio: Float = 1f,
    shape: Shape = MnsTheme.shapes.card,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val motion = MnsTheme.motion
    val interactionSource = rememberMnsInteractionSource()

    val container by animateColorAsState(
        targetValue = when {
            !shortcut.enabled -> colors.surfaceVariant
            selected -> colors.primaryContainer
            else -> colors.surface
        },
        animationSpec = motion.tween(motion.durationNormal),
        label = "mnsShortcutContainer",
    )
    val contentColor = when {
        !shortcut.enabled -> colors.textDisabled
        selected -> colors.onPrimaryContainer
        else -> colors.onSurface
    }

    MnsSurface(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .mnsPressScale(interactionSource),
        shape = shape,
        color = container,
        contentColor = contentColor,
        borderWidth = if (selected) MnsTheme.borders.none else MnsTheme.borders.none,
        onClick = onClick,
        enabled = shortcut.enabled,
        interactionSource = interactionSource,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterVertically),
            ) {
                MnsIcon(
                    imageVector = shortcut.icon,
                    contentDescription = null,
                    size = MnsTheme.sizing.iconXl,
                    tint = contentColor,
                )
                MnsText(
                    text = shortcut.label,
                    style = MnsTheme.typography.labelMedium,
                    color = contentColor,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (shortcut.badgeCount != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing.sm),
                ) {
                    MnsBadge(count = shortcut.badgeCount)
                }
            }
        }
    }
}

/**
 * Grade de atalhos.
 *
 * @param shortcuts atalhos a exibir.
 * @param selectedIds ids atualmente selecionados.
 * @param onShortcutClick chamado com o atalho tocado.
 * @param columns número de colunas.
 */
@Composable
public fun MnsShortcutGrid(
    shortcuts: List<MnsShortcut>,
    onShortcutClick: (MnsShortcut) -> Unit,
    modifier: Modifier = Modifier,
    selectedIds: Set<String> = emptySet(),
    columns: Int = 2,
    aspectRatio: Float = 1.15f,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
    ) {
        items(items = shortcuts, key = { it.id }) { shortcut ->
            MnsShortcutCard(
                shortcut = shortcut,
                onClick = { onShortcutClick(shortcut) },
                selected = shortcut.id in selectedIds,
                aspectRatio = aspectRatio,
            )
        }
    }
}

/** Cor transparente reutilizada. */
internal val ShortcutTransparent: Color = Color.Transparent
