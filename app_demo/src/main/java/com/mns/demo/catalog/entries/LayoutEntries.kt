package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.icons.filled.VerticalSplit
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.layout.MnsBottomNavBar
import com.mns.designsystem.component.layout.MnsBottomSheet
import com.mns.designsystem.component.layout.MnsCard
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsConfirmDialog
import com.mns.designsystem.component.layout.MnsDivider
import com.mns.designsystem.component.layout.MnsFixedTabBar
import com.mns.designsystem.component.layout.MnsLabeledDivider
import com.mns.designsystem.component.layout.MnsNavItem
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.layout.MnsTab
import com.mns.designsystem.component.layout.MnsTabBar
import com.mns.designsystem.component.layout.MnsTopBar
import com.mns.designsystem.component.layout.MnsTopBarAlignment
import com.mns.designsystem.component.layout.MnsVerticalDivider
import com.mns.designsystem.component.text.MnsHeading
import com.mns.designsystem.component.text.MnsHeadingLevel
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/** Entradas do catálogo para a categoria [DemoCategory.LAYOUT]. */
@OptIn(ExperimentalMaterial3Api::class)
internal fun layoutEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-card",
        name = "MnsCard",
        category = DemoCategory.LAYOUT,
        summary = "Container de conteúdo agrupado, com 5 estratégias de separação visual.",
        docPath = "docs/components/layout/mns-card.md",
        icon = Icons.Filled.Dashboard,
        knobs = listOf(
            DemoKnob.OptionKnob(
                key = "variant",
                label = "variant",
                options = MnsCardVariant.entries.map { it.name },
                description = "Como o card se separa do fundo: sombra, cor, traço ou nada.",
                default = MnsCardVariant.ELEVATED.name,
            ),
            DemoKnob.BoolKnob("clickable", "onClick", "Torna o card acionável, com feedback de escala.", true),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia o clique.", true),
            DemoKnob.NumberKnob("padding", "contentPadding (dp)", 0f..32f, "Padding interno.", 16f, 8),
        ),
    ) { knobs ->
        MnsCard(
            variant = knobs.enum("variant", MnsCardVariant.entries.toTypedArray(), MnsCardVariant.ELEVATED),
            onClick = if (knobs.bool("clickable", true)) ({}) else null,
            enabled = knobs.bool("enabled", true),
            contentPadding = knobs.number("padding", 16f).dp,
        ) {
            MnsHeading("North Van Hiking", level = MnsHeadingLevel.H4, overline = "Mount Seymour")
            MnsText(
                text = "Vancouver Community Centre · MAR 20, 8:30 AM PDT",
                style = MnsTheme.typography.bodySmall,
                color = MnsTheme.colors.textSecondary,
            )
        }
    },

    DemoComponent(
        id = "mns-surface",
        name = "MnsSurface",
        category = DemoCategory.LAYOUT,
        summary = "Primitivo de todos os containers: forma, cor, borda, sombra e cor de conteúdo.",
        docPath = "docs/components/layout/mns-surface.md",
        icon = Icons.Filled.WebAsset,
        knobs = listOf(
            DemoKnob.ColorRoleKnob(
                key = "color",
                label = "color",
                roles = listOf("surface", "surfaceVariant", "primaryContainer", "accentContainer", "primary", "surfaceInverse"),
                description = "Cor de fundo.",
                default = "primaryContainer",
            ),
            DemoKnob.NumberKnob("elevation", "elevation (dp)", 0f..12f, "Altura da sombra.", 3f, 12),
            DemoKnob.NumberKnob("border", "borderWidth (dp)", 0f..4f, "Espessura da borda.", 0f, 8),
        ),
    ) { knobs ->
        val color = MnsTheme.colors.byRole(knobs.option("color", "primaryContainer"))
        MnsSurface(
            modifier = Modifier.fillMaxWidth(),
            color = color,
            contentColor = MnsTheme.colors.onPrimaryContainer,
            elevation = knobs.number("elevation", 3f).dp,
            borderWidth = knobs.number("border", 0f).dp,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(96.dp),
                contentAlignment = Alignment.Center,
            ) {
                MnsText("MnsSurface", style = MnsTheme.typography.titleMedium)
            }
        }
    },

    DemoComponent(
        id = "mns-top-bar",
        name = "MnsTopBar",
        category = DemoCategory.LAYOUT,
        summary = "Barra superior com navegação, título alinhável e ações.",
        docPath = "docs/components/layout/mns-top-bar.md",
        icon = Icons.Filled.VerticalSplit,
        fullWidthPreview = true,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "Título da tela.", "Checkout"),
            DemoKnob.TextKnob("subtitle", "subtitle", "Linha de contexto.", ""),
            DemoKnob.OptionKnob(
                key = "alignment",
                label = "alignment",
                options = MnsTopBarAlignment.entries.map { it.name },
                description = "Posição do título.",
                default = MnsTopBarAlignment.CENTER.name,
            ),
            DemoKnob.BoolKnob("back", "onNavigateBack", "Exibe o botão de voltar.", true),
            DemoKnob.BoolKnob("actions", "actions", "Exibe ações à direita.", true),
            DemoKnob.NumberKnob("elevation", "elevation (dp)", 0f..8f, "Sombra da barra.", 0f, 8),
        ),
    ) { knobs ->
        MnsTopBar(
            title = knobs.text("title", "Checkout").ifBlank { null },
            subtitle = knobs.text("subtitle").ifBlank { null },
            alignment = knobs.enum("alignment", MnsTopBarAlignment.entries.toTypedArray(), MnsTopBarAlignment.CENTER),
            onNavigateBack = if (knobs.bool("back", true)) ({}) else null,
            elevation = knobs.number("elevation", 0f).dp,
            applyStatusBarPadding = false,
            actions = if (knobs.bool("actions", true)) {
                {
                    MnsIconButton(Icons.Filled.Share, "Compartilhar", {}, variant = MnsButtonVariant.TEXT)
                    MnsIconButton(Icons.Filled.MoreVert, "Mais opções", {}, variant = MnsButtonVariant.TEXT)
                }
            } else {
                null
            },
        )
    },

    DemoComponent(
        id = "mns-bottom-nav-bar",
        name = "MnsBottomNavBar",
        category = DemoCategory.LAYOUT,
        summary = "Navegação inferior para 2 a 5 destinos, com badge por item.",
        docPath = "docs/components/layout/mns-bottom-nav-bar.md",
        icon = Icons.Filled.Home,
        fullWidthPreview = true,
        knobs = listOf(
            DemoKnob.BoolKnob("labels", "showLabels", "Exibe o rótulo sob o ícone.", true),
            DemoKnob.NumberKnob("count", "nº de destinos", 2f..4f, "Quantidade de abas.", 4f, 2),
            DemoKnob.BoolKnob("badge", "badgeCount", "Exibe contador no segundo item.", true),
        ),
    ) { knobs ->
        var selected by remember { mutableStateOf("home") }
        val todos = listOf(
            MnsNavItem("home", "Início", Icons.Outlined.Home, Icons.Filled.Home),
            MnsNavItem("agenda", "Agenda", Icons.Filled.CalendarMonth, badgeCount = 3.takeIf { knobs.bool("badge", true) }),
            MnsNavItem("tickets", "Ingressos", Icons.Filled.ConfirmationNumber),
            MnsNavItem("perfil", "Perfil", Icons.Filled.Person),
        )
        MnsBottomNavBar(
            items = todos.take(knobs.int("count", 4).coerceIn(2, 4)),
            selectedId = selected,
            onSelect = { selected = it.id },
            showLabels = knobs.bool("labels", true),
            applyNavigationBarPadding = false,
        )
    },

    DemoComponent(
        id = "mns-tab-bar",
        name = "MnsTabBar · MnsFixedTabBar",
        category = DemoCategory.LAYOUT,
        summary = "Abas roláveis com indicador, ou segmentadas de largura fixa.",
        docPath = "docs/components/layout/mns-tab-bar.md",
        icon = Icons.Filled.Tab,
        fullWidthPreview = true,
        knobs = listOf(
            DemoKnob.BoolKnob("fixed", "MnsFixedTabBar", "Alterna para a variante de largura fixa.", false),
            DemoKnob.BoolKnob("badge", "badgeCount", "Exibe contador na segunda aba.", true),
        ),
    ) { knobs ->
        var index by remember { mutableIntStateOf(0) }
        val abas = listOf(
            MnsTab("todos", "Todos"),
            MnsTab("hoje", "Hoje", badgeCount = 5.takeIf { knobs.bool("badge", true) }),
            MnsTab("semana", "Esta semana"),
            MnsTab("mes", "Este mês"),
        )
        if (knobs.bool("fixed")) {
            MnsFixedTabBar(
                tabs = abas.take(3),
                selectedIndex = index.coerceAtMost(2),
                onSelect = { index = it },
                modifier = Modifier.padding(horizontal = MnsTheme.spacing.base),
            )
        } else {
            MnsTabBar(tabs = abas, selectedIndex = index, onSelect = { index = it })
        }
    },

    DemoComponent(
        id = "mns-bottom-sheet",
        name = "MnsBottomSheet",
        category = DemoCategory.LAYOUT,
        summary = "Sheet modal com alça, cabeçalho e cores vindas dos tokens.",
        docPath = "docs/components/layout/mns-bottom-sheet.md",
        icon = Icons.Filled.HorizontalRule,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "Título do cabeçalho.", "Filtros"),
            DemoKnob.TextKnob("subtitle", "subtitle", "Linha de apoio.", "Refine os eventos exibidos"),
            DemoKnob.BoolKnob("handle", "showHandle", "Exibe a alça de arraste.", true),
            DemoKnob.BoolKnob("close", "showCloseButton", "Exibe o botão de fechar.", true),
        ),
    ) { knobs ->
        var aberto by remember { mutableStateOf(false) }
        MnsButton(text = "Abrir bottom sheet", onClick = { aberto = true }, fillMaxWidth = true)
        if (aberto) {
            MnsBottomSheet(
                onDismissRequest = { aberto = false },
                title = knobs.text("title", "Filtros").ifBlank { null },
                subtitle = knobs.text("subtitle").ifBlank { null },
                showHandle = knobs.bool("handle", true),
                showCloseButton = knobs.bool("close", true),
            ) {
                MnsText(
                    text = "O conteúdo do sheet é um slot livre: coloque filtros, formulários ou uma lista.",
                    style = MnsTheme.typography.bodyMedium,
                    color = MnsTheme.colors.textSecondary,
                )
                MnsButton(text = "Aplicar", onClick = { aberto = false }, fillMaxWidth = true)
            }
        }
    },

    DemoComponent(
        id = "mns-dialog",
        name = "MnsDialog · MnsConfirmDialog",
        category = DemoCategory.LAYOUT,
        summary = "Dialog modal e sua variante de confirmação com proteção contra exclusão acidental.",
        docPath = "docs/components/layout/mns-dialog.md",
        icon = Icons.Filled.CreditCard,
        knobs = listOf(
            DemoKnob.OptionKnob(
                key = "status",
                label = "status",
                options = MnsStatus.entries.map { it.name },
                description = "DANGER troca o botão de confirmar para destrutivo e desliga o fechar-ao-tocar-fora.",
                default = MnsStatus.DANGER.name,
            ),
            DemoKnob.BoolKnob("loading", "loading", "Estado de processamento no botão de confirmar.", false),
            DemoKnob.BoolKnob("dismissButton", "dismissText", "Exibe o botão de cancelar.", true),
        ),
    ) { knobs ->
        var aberto by remember { mutableStateOf(false) }
        MnsButton(text = "Abrir dialog", onClick = { aberto = true }, fillMaxWidth = true)
        if (aberto) {
            MnsConfirmDialog(
                title = "Cancelar ingresso?",
                message = "Esta ação não pode ser desfeita. O valor será estornado em até 7 dias úteis.",
                confirmText = "Cancelar ingresso",
                dismissText = "Voltar".takeIf { knobs.bool("dismissButton", true) },
                status = knobs.enum("status", MnsStatus.entries.toTypedArray(), MnsStatus.DANGER),
                icon = Icons.Filled.ConfirmationNumber,
                loading = knobs.bool("loading"),
                onConfirm = { aberto = false },
                onDismissRequest = { aberto = false },
            )
        }
    },

    DemoComponent(
        id = "mns-divider",
        name = "MnsDivider",
        category = DemoCategory.LAYOUT,
        summary = "Separadores horizontal, vertical e com rótulo.",
        docPath = "docs/components/layout/mns-divider.md",
        icon = Icons.Filled.HorizontalRule,
        knobs = listOf(
            DemoKnob.NumberKnob("thickness", "thickness (dp)", 0.5f..4f, "Espessura.", 1f, 7, { "%.1f".format(it) }),
            DemoKnob.NumberKnob("inset", "inset (dp)", 0f..32f, "Recuo lateral.", 0f, 8),
            DemoKnob.TextKnob("label", "rótulo", "Texto do MnsLabeledDivider.", "ou"),
        ),
    ) { knobs ->
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.lg)) {
            MnsDivider(
                thickness = knobs.number("thickness", 1f).dp,
                inset = knobs.number("inset", 0f).dp,
            )
            MnsLabeledDivider(text = knobs.text("label", "ou"))
            Row(modifier = Modifier.height(48.dp), verticalAlignment = Alignment.CenterVertically) {
                MnsText("Antes", style = MnsTheme.typography.bodySmall)
                MnsVerticalDivider(
                    modifier = Modifier.padding(horizontal = MnsTheme.spacing.base),
                    thickness = knobs.number("thickness", 1f).dp,
                )
                MnsText("Depois", style = MnsTheme.typography.bodySmall)
            }
        }
    },

    DemoComponent(
        id = "mns-scaffold",
        name = "MnsScaffold",
        category = DemoCategory.LAYOUT,
        summary = "Container de tela que mede as barras de verdade e devolve o padding exato.",
        docPath = "docs/components/layout/mns-scaffold.md",
        icon = Icons.Filled.Add,
        knobs = listOf(
            DemoKnob.BoolKnob("topBar", "topBar", "Inclui a barra superior.", true),
            DemoKnob.BoolKnob("bottomBar", "bottomBar", "Inclui a barra inferior.", true),
            DemoKnob.BoolKnob("fab", "floatingActionButton", "Inclui o FAB.", true),
            DemoKnob.OptionKnob(
                key = "fabPosition",
                label = "fabPosition",
                options = listOf("END", "CENTER"),
                description = "Ancoragem do FAB.",
                default = "END",
            ),
        ),
    ) { knobs ->
        MnsSurface(
            modifier = Modifier.fillMaxWidth().height(320.dp),
            color = MnsTheme.colors.surfaceVariant,
            borderWidth = MnsTheme.borders.thin,
        ) {
            com.mns.designsystem.component.layout.MnsScaffold(
                topBar = if (knobs.bool("topBar", true)) {
                    { MnsTopBar(title = "Tela", applyStatusBarPadding = false) }
                } else {
                    null
                },
                bottomBar = if (knobs.bool("bottomBar", true)) {
                    {
                        MnsBottomNavBar(
                            items = listOf(
                                MnsNavItem("a", "Início", Icons.Filled.Home),
                                MnsNavItem("b", "Perfil", Icons.Filled.Person),
                            ),
                            selectedId = "a",
                            onSelect = {},
                            applyNavigationBarPadding = false,
                        )
                    }
                } else {
                    null
                },
                floatingActionButton = if (knobs.bool("fab", true)) {
                    { com.mns.designsystem.component.action.MnsFab(Icons.Filled.Add, "Criar", {}) }
                } else {
                    null
                },
                fabPosition = if (knobs.option("fabPosition", "END") == "CENTER") {
                    com.mns.designsystem.component.layout.MnsFabPosition.CENTER
                } else {
                    com.mns.designsystem.component.layout.MnsFabPosition.END
                },
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .padding(MnsTheme.spacing.base),
                    verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                ) {
                    MnsText(
                        text = "O conteúdo recebe o PaddingValues medido das barras.",
                        style = MnsTheme.typography.bodySmall,
                        color = MnsTheme.colors.textSecondary,
                    )
                }
            }
        }
    },
)
