package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.list.MnsAvatar
import com.mns.designsystem.component.list.MnsAvatarGroup
import com.mns.designsystem.component.list.MnsListAction
import com.mns.designsystem.component.list.MnsListLeading
import com.mns.designsystem.component.shortcut.MnsShortcut
import com.mns.designsystem.component.shortcut.MnsShortcutCard
import com.mns.designsystem.component.shortcut.MnsShortcutGrid
import com.mns.designsystem.component.text.MnsCurrencyText
import com.mns.designsystem.theme.MnsTheme

private val participantes = listOf("Alves Farhat", "Bruna Lima", "Caio Souza", "Dara Nunes", "Eli Prado", "Fabio Reis")

/** Entradas do catálogo para as categorias [DemoCategory.LIST] e [DemoCategory.SHORTCUT]. */
internal fun collectionEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-list-action",
        name = "MnsListAction",
        category = DemoCategory.LIST,
        summary = "Item de lista acionável com overline, subtítulo, metadados e slot final.",
        docPath = "docs/components/list/mns-list-action.md",
        icon = Icons.Filled.FormatListBulleted,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "Texto principal.", "North Van Hiking"),
            DemoKnob.TextKnob("overline", "overline", "Rótulo acima do título.", "Mount Seymour"),
            DemoKnob.TextKnob("subtitle", "subtitle", "Linha de apoio.", "Vancouver Community Centre"),
            DemoKnob.TextKnob("meta", "meta", "Terceira linha (data/hora/estado).", "MAR 20 · 8:30 AM PDT"),
            DemoKnob.OptionKnob(
                key = "leading",
                label = "leading",
                options = listOf("None", "Avatar", "Icon"),
                description = "Elemento inicial. Tipo selado: impossível combinar dois.",
                default = "Avatar",
            ),
            DemoKnob.OptionKnob(
                key = "trailing",
                label = "trailing",
                options = listOf("Nenhum", "Avatares", "Preço", "Ação"),
                description = "Slot final.",
                default = "Avatares",
            ),
            DemoKnob.BoolKnob("selected", "selected", "Pinta a linha como destacada.", false),
            DemoKnob.BoolKnob("chevron", "showChevron", "Exibe a seta de navegação.", false),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia o toque.", true),
        ),
    ) { knobs ->
        MnsListAction(
            title = knobs.text("title", "Item"),
            overline = knobs.text("overline").ifBlank { null },
            subtitle = knobs.text("subtitle").ifBlank { null },
            meta = knobs.text("meta").ifBlank { null },
            leading = when (knobs.option("leading", "Avatar")) {
                "Avatar" -> MnsListLeading.Avatar("North Van")
                "Icon" -> MnsListLeading.Icon(Icons.Filled.Hiking)
                else -> MnsListLeading.None
            },
            trailing = when (knobs.option("trailing", "Avatares")) {
                "Avatares" -> ({ MnsAvatarGroup(names = participantes, max = 3) })
                "Preço" -> ({ MnsCurrencyText(cents = 12550, style = MnsTheme.typography.titleMedium) })
                "Ação" -> ({
                    MnsIconButton(Icons.Filled.Bookmark, "Salvar", {}, variant = MnsButtonVariant.TEXT)
                })
                else -> null
            },
            selected = knobs.bool("selected"),
            showChevron = knobs.bool("chevron"),
            enabled = knobs.bool("enabled", true),
            onClick = {},
        )
    },

    DemoComponent(
        id = "mns-avatar",
        name = "MnsAvatar · MnsAvatarGroup",
        category = DemoCategory.LIST,
        summary = "Avatar com fallback determinístico de iniciais e pilha com contador.",
        docPath = "docs/components/list/mns-avatar.md",
        icon = Icons.Filled.Face,
        knobs = listOf(
            DemoKnob.TextKnob("name", "name", "Gera iniciais e cor estável.", "Alves Farhat"),
            DemoKnob.OptionKnob(
                key = "size",
                label = "size",
                options = listOf("avatarXs", "avatarSm", "avatarMd", "avatarLg", "avatarXl"),
                description = "Token de dimensão.",
                default = "avatarMd",
            ),
            DemoKnob.BoolKnob("icon", "icon", "Usa ícone no lugar das iniciais.", false),
            DemoKnob.NumberKnob("max", "max (grupo)", 1f..5f, "Avatares antes do contador.", 3f, 4),
            DemoKnob.NumberKnob("overlap", "overlap", 0f..0.6f, "Sobreposição da pilha.", 0.35f, 12, { "%.2f".format(it) }),
        ),
    ) { knobs ->
        val s = MnsTheme.sizing
        val size = when (knobs.option("size", "avatarMd")) {
            "avatarXs" -> s.avatarXs
            "avatarSm" -> s.avatarSm
            "avatarLg" -> s.avatarLg
            "avatarXl" -> s.avatarXl
            else -> s.avatarMd
        }
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.lg)) {
            Row(horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md)) {
                MnsAvatar(
                    name = knobs.text("name", "Alves Farhat"),
                    size = size,
                    icon = Icons.Filled.Face.takeIf { knobs.bool("icon") },
                )
                MnsAvatar(name = "Bruna Lima", size = size)
                MnsAvatar(name = "Caio Souza", size = size)
            }
            MnsAvatarGroup(
                names = participantes,
                max = knobs.int("max", 3),
                size = size,
                overlap = knobs.number("overlap", 0.35f),
            )
        }
    },

    DemoComponent(
        id = "mns-shortcut-card",
        name = "MnsShortcutCard",
        category = DemoCategory.SHORTCUT,
        summary = "Quadrado de categoria com ícone, rótulo e badge.",
        docPath = "docs/components/shortcut/mns-shortcut-card.md",
        icon = Icons.Filled.Palette,
        knobs = listOf(
            DemoKnob.TextKnob("label", "label", "Rótulo do atalho.", "Travel"),
            DemoKnob.BoolKnob("selected", "selected", "Estado selecionado.", true),
            DemoKnob.BoolKnob("badge", "badgeCount", "Exibe contador no canto.", false),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia o toque.", true),
            DemoKnob.NumberKnob("aspect", "aspectRatio", 0.8f..1.6f, "Proporção do card.", 1.15f, 8, { "%.2f".format(it) }),
        ),
    ) { knobs ->
        Row(
            modifier = Modifier.height(140.dp),
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
        ) {
            MnsShortcutCard(
                shortcut = MnsShortcut(
                    id = "travel",
                    label = knobs.text("label", "Travel"),
                    icon = Icons.Filled.Hiking,
                    badgeCount = 4.takeIf { knobs.bool("badge") },
                    enabled = knobs.bool("enabled", true),
                ),
                onClick = {},
                selected = knobs.bool("selected", true),
                aspectRatio = knobs.number("aspect", 1.15f),
                modifier = Modifier.weight(1f),
            )
            MnsShortcutCard(
                shortcut = MnsShortcut("music", "Music", Icons.Filled.MusicNote),
                onClick = {},
                aspectRatio = knobs.number("aspect", 1.15f),
                modifier = Modifier.weight(1f),
            )
        }
    },

    DemoComponent(
        id = "mns-shortcut-grid",
        name = "MnsShortcutGrid",
        category = DemoCategory.SHORTCUT,
        summary = "Grade de categorias com seleção múltipla ou única.",
        docPath = "docs/components/shortcut/mns-shortcut-grid.md",
        icon = Icons.Filled.Apps,
        knobs = listOf(
            DemoKnob.NumberKnob("columns", "columns", 2f..4f, "Colunas da grade.", 2f, 2),
            DemoKnob.NumberKnob("aspect", "aspectRatio", 0.8f..1.6f, "Proporção dos cards.", 1.15f, 8, { "%.2f".format(it) }),
        ),
    ) { knobs ->
        var selecionados by remember { mutableStateOf(setOf("travel")) }
        MnsShortcutGrid(
            shortcuts = listOf(
                MnsShortcut("art", "Art", Icons.Filled.Palette),
                MnsShortcut("business", "Business", Icons.Filled.Business),
                MnsShortcut("travel", "Travel", Icons.Filled.Hiking),
                MnsShortcut("family", "Family", Icons.Filled.Groups),
                MnsShortcut("sport", "Sport", Icons.Filled.SportsSoccer),
                MnsShortcut("hobbies", "Hobbies", Icons.Filled.Pets),
                MnsShortcut("tech", "Tech", Icons.Filled.Terminal),
                MnsShortcut("game", "Game", Icons.Filled.SportsEsports),
                MnsShortcut("education", "Education", Icons.Filled.School),
                MnsShortcut("music", "Music", Icons.Filled.MusicNote),
            ),
            onShortcutClick = { atalho ->
                selecionados = if (atalho.id in selecionados) {
                    selecionados - atalho.id
                } else {
                    selecionados + atalho.id
                }
            },
            selectedIds = selecionados,
            columns = knobs.int("columns", 2),
            aspectRatio = knobs.number("aspect", 1.15f),
            modifier = Modifier.height(560.dp),
        )
    },
)
