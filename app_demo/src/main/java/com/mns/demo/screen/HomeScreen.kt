package com.mns.demo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mns.demo.catalog.DemoCatalog
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.ThemeController
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.input.MnsSearchField
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsScaffold
import com.mns.designsystem.component.layout.MnsTab
import com.mns.designsystem.component.layout.MnsTabBar
import com.mns.designsystem.component.layout.MnsTopBar
import com.mns.designsystem.component.list.MnsListAction
import com.mns.designsystem.component.list.MnsListLeading
import com.mns.designsystem.component.status.MnsEmptyState
import com.mns.designsystem.component.text.MnsHeading
import com.mns.designsystem.component.text.MnsHeadingLevel
import com.mns.designsystem.theme.MnsTheme

/**
 * Tela inicial do catálogo.
 *
 * Estrutura pedida no briefing: uma barra de abas com os **tipos** de
 * componente e, dentro de cada aba, a lista dos componentes daquele tipo.
 * A busca é global — atravessa todas as abas — porque quem procura "moeda"
 * não sabe (nem deveria precisar saber) se isso é `input` ou `text`.
 */
@Composable
internal fun HomeScreen(
    theme: ThemeController,
    onComponentClick: (DemoComponent) -> Unit,
    onOpenTokens: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val categories = DemoCatalog.categories
    val searching = query.isNotBlank()
    val visible = remember(query, tabIndex, categories) {
        if (searching) {
            DemoCatalog.search(query)
        } else {
            DemoCatalog.byCategory(categories[tabIndex.coerceIn(categories.indices)])
        }
    }

    MnsScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MnsTopBar(
                title = "MNS Design System",
                subtitle = "${DemoCatalog.components.size} componentes · tema ${theme.provider.displayName}",
                actions = {
                    MnsIconButton(
                        icon = if (theme.darkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = if (theme.darkMode) "Usar tema claro" else "Usar tema escuro",
                        onClick = { theme.darkMode = !theme.darkMode },
                        variant = MnsButtonVariant.TEXT,
                    )
                    MnsIconButton(
                        icon = Icons.Filled.Tune,
                        contentDescription = "Abrir tokens do tema",
                        onClick = onOpenTokens,
                        variant = MnsButtonVariant.SECONDARY,
                    )
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            MnsSearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Buscar componente",
                modifier = Modifier.padding(
                    horizontal = MnsTheme.spacing.screenHorizontal,
                    vertical = MnsTheme.spacing.sm,
                ),
            )

            if (!searching) {
                MnsTabBar(
                    tabs = categories.map { MnsTab(it.id, it.label, DemoCatalog.byCategory(it).size) },
                    selectedIndex = tabIndex.coerceIn(categories.indices),
                    onSelect = { tabIndex = it },
                )
            }

            if (visible.isEmpty()) {
                MnsEmptyState(
                    title = "Nenhum componente encontrado",
                    description = "Nada corresponde a \"$query\". Tente pelo nome da função, como MnsButton.",
                )
                return@Column
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = MnsTheme.spacing.screenHorizontal,
                    end = MnsTheme.spacing.screenHorizontal,
                    top = MnsTheme.spacing.sm,
                    bottom = MnsTheme.spacing.xxl,
                ),
                verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
            ) {
                if (!searching) {
                    item(key = "header-${categories[tabIndex.coerceIn(categories.indices)].id}") {
                        MnsHeading(
                            text = categories[tabIndex.coerceIn(categories.indices)].label,
                            level = MnsHeadingLevel.H3,
                            subtitle = categories[tabIndex.coerceIn(categories.indices)].description,
                            modifier = Modifier.padding(vertical = MnsTheme.spacing.sm),
                        )
                    }
                }
                items(items = visible, key = { it.id }) { component ->
                    MnsListAction(
                        title = component.name,
                        subtitle = component.summary,
                        overline = if (searching) component.category.label else null,
                        leading = component.icon?.let { MnsListLeading.Icon(it) } ?: MnsListLeading.None,
                        showChevron = true,
                        onClick = { onComponentClick(component) },
                        containerColor = MnsTheme.colors.surface,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

/** Variante de card usada nos itens da home; extraído para os testes referenciarem. */
internal val HomeItemCardVariant: MnsCardVariant = MnsCardVariant.FILLED
