package com.mns.designsystem.component.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.status.MnsBadge
import com.mns.designsystem.component.status.MnsBadgedBox
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Item da [MnsBottomNavBar].
 *
 * @property id chave estável do destino.
 * @property label rótulo. Mesmo com [MnsBottomNavBar.showLabels] desligado, é
 *   usado como `contentDescription` — nunca fica sem rótulo acessível.
 * @property icon ícone do estado inativo.
 * @property selectedIcon ícone do estado ativo; `null` reutiliza [icon].
 * @property badgeCount contador exibido sobre o ícone.
 */
public data class MnsNavItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null,
    val badgeCount: Int? = null,
)

/**
 * Barra de navegação inferior.
 *
 * Suporta de 2 a 5 destinos. Acima disso a área de toque de cada item cai
 * abaixo do confortável e o padrão certo passa a ser um menu.
 *
 * @param items destinos.
 * @param selectedId id do destino ativo.
 * @param onSelect chamado com o item tocado.
 * @param showLabels exibe o rótulo sob o ícone.
 * @param applyNavigationBarPadding reserva o espaço da barra de gestos.
 */
@Composable
public fun MnsBottomNavBar(
    items: List<MnsNavItem>,
    selectedId: String,
    onSelect: (MnsNavItem) -> Unit,
    modifier: Modifier = Modifier,
    showLabels: Boolean = true,
    containerColor: Color = MnsTheme.colors.surface,
    elevation: Dp = MnsTheme.elevation.level2,
    applyNavigationBarPadding: Boolean = true,
) {
    require(items.isNotEmpty()) { "MnsBottomNavBar exige ao menos um item." }
    val colors = MnsTheme.colors
    val motion = MnsTheme.motion

    MnsSurface(
        modifier = modifier.fillMaxWidth(),
        shape = MnsTheme.shapes.none,
        color = containerColor,
        contentColor = colors.onSurface,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .let { if (applyNavigationBarPadding) it.navigationBarsPadding() else it }
                .height(MnsTheme.sizing.bottomBarHeight)
                .semantics { selectableGroup() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val selected = item.id == selectedId
                val tint by animateColorAsState(
                    targetValue = if (selected) colors.primary else colors.textTertiary,
                    animationSpec = motion.tween(motion.durationFast),
                    label = "mnsNavTint",
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(MnsTheme.sizing.bottomBarHeight)
                        .selectable(
                            selected = selected,
                            role = Role.Tab,
                            onClick = { onSelect(item) },
                        )
                        .padding(vertical = MnsTheme.spacing.sm),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        MnsTheme.spacing.xxs,
                        Alignment.CenterVertically,
                    ),
                ) {
                    MnsBadgedBox(
                        badge = { if (item.badgeCount != null) MnsBadge(count = item.badgeCount) },
                    ) {
                        MnsIcon(
                            imageVector = if (selected) item.selectedIcon ?: item.icon else item.icon,
                            contentDescription = if (showLabels) null else item.label,
                            size = MnsTheme.sizing.iconLg,
                            tint = tint,
                        )
                    }
                    if (showLabels) {
                        MnsText(
                            text = item.label,
                            style = MnsTheme.typography.labelSmall,
                            color = tint,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

/** Espaço reservado usado quando a barra não tem itens. */
internal val EmptyNavBarPlaceholder: @Composable () -> Unit = { Box(Modifier) }
