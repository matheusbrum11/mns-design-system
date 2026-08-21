package com.mns.designsystem.component.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.status.MnsBadge
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/**
 * Uma aba da [MnsTabBar].
 *
 * @property id chave estável.
 * @property label rótulo exibido.
 * @property badgeCount contador opcional exibido **ao lado** do rótulo.
 * @property badgeStatus intenção do contador. O default é [MnsStatus.NEUTRAL]
 *   de propósito: contar itens não é alerta, e badge vermelho em aba de
 *   catálogo treina o usuário a ignorar vermelho onde ele importa.
 */
public data class MnsTab(
    val id: String,
    val label: String,
    val badgeCount: Int? = null,
    val badgeStatus: MnsStatus = MnsStatus.NEUTRAL,
)

/**
 * Barra de abas rolável.
 *
 * A barra rola sozinha para trazer a aba ativa ao campo de visão — sem isso,
 * navegar por teclado ou por deep link deixa a aba selecionada fora da tela.
 *
 * @param tabs abas, na ordem de exibição.
 * @param selectedIndex índice ativo.
 * @param onSelect chamado com o índice tocado.
 * @param indicatorColor cor do sublinhado da aba ativa.
 * @param edgePadding recuo nas extremidades da lista.
 */
@Composable
public fun MnsTabBar(
    tabs: List<MnsTab>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    indicatorColor: Color = MnsTheme.colors.primary,
    edgePadding: Dp = MnsTheme.spacing.screenHorizontal,
) {
    require(tabs.isNotEmpty()) { "MnsTabBar exige ao menos uma aba." }
    val colors = MnsTheme.colors
    val motion = MnsTheme.motion
    val safeIndex = selectedIndex.coerceIn(0, tabs.lastIndex)
    val listState = rememberLazyListState()

    LaunchedEffect(safeIndex) {
        listState.animateScrollToItem(safeIndex)
    }

    Column(modifier = modifier.fillMaxWidth().background(containerColor)) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { selectableGroup() },
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.lg),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = edgePadding),
        ) {
            itemsIndexed(items = tabs, key = { _, tab -> tab.id }) { index, tab ->
                val selected = index == safeIndex
                val contentColor by animateColorAsState(
                    targetValue = if (selected) colors.textPrimary else colors.textTertiary,
                    animationSpec = motion.tween(motion.durationFast),
                    label = "mnsTabColor",
                )
                val indicatorWidth by animateDpAsState(
                    targetValue = if (selected) 24.dp else 0.dp,
                    animationSpec = motion.tween(motion.durationNormal),
                    label = "mnsTabIndicator",
                )
                Column(
                    modifier = Modifier
                        .selectable(
                            selected = selected,
                            role = Role.Tab,
                            onClick = { onSelect(index) },
                        )
                        .padding(vertical = MnsTheme.spacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                ) {
                    // O contador fica **em linha** com o rótulo, não sobreposto:
                    // em abas de texto, um badge ancorado no canto invade o
                    // rótulo vizinho assim que a fonte cresce.
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xs),
                    ) {
                        MnsText(
                            text = tab.label,
                            style = if (selected) {
                                MnsTheme.typography.labelLarge
                            } else {
                                MnsTheme.typography.labelMedium
                            },
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (tab.badgeCount != null) {
                            MnsBadge(
                                count = tab.badgeCount,
                                status = tab.badgeStatus,
                                contentDescription = "${tab.badgeCount} em ${tab.label}",
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(indicatorWidth)
                            .height(3.dp)
                            .background(indicatorColor, MnsTheme.shapes.full),
                    )
                }
            }
        }
    }
}

/**
 * Variante "segmentada" da barra de abas: as abas dividem a largura igualmente
 * e a ativa recebe fundo em vez de sublinhado. Boa para 2–3 abas fixas.
 */
@Composable
public fun MnsFixedTabBar(
    tabs: List<MnsTab>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(tabs.isNotEmpty()) { "MnsFixedTabBar exige ao menos uma aba." }
    val colors = MnsTheme.colors
    val safeIndex = selectedIndex.coerceIn(0, tabs.lastIndex)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, MnsTheme.shapes.full)
            .padding(4.dp)
            .semantics { selectableGroup() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = index == safeIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(MnsTheme.sizing.chipHeight + 8.dp)
                    .background(
                        color = if (selected) colors.surface else Color.Transparent,
                        shape = MnsTheme.shapes.full,
                    )
                    .selectable(
                        selected = selected,
                        role = Role.Tab,
                        onClick = { onSelect(index) },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xs),
                ) {
                    MnsText(
                        text = tab.label,
                        style = MnsTheme.typography.labelMedium,
                        color = if (selected) colors.textPrimary else colors.textTertiary,
                        maxLines = 1,
                    )
                    if (tab.badgeCount != null) {
                        MnsBadge(
                            count = tab.badgeCount,
                            status = tab.badgeStatus,
                            contentDescription = "${tab.badgeCount} em ${tab.label}",
                        )
                    }
                }
            }
        }
    }
}
