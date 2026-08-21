package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsFab
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.action.MnsSegment
import com.mns.designsystem.component.action.MnsSegmentedControl
import com.mns.designsystem.theme.MnsTheme

/** Entradas do catálogo para a categoria [DemoCategory.ACTION]. */
internal fun actionEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-button",
        name = "MnsButton",
        category = DemoCategory.ACTION,
        summary = "Botão com 5 níveis de ênfase, 3 tamanhos, ícones e estado de carregamento.",
        docPath = "docs/components/action/mns-button.md",
        icon = Icons.Filled.TouchApp,
        knobs = listOf(
            DemoKnob.TextKnob("text", "text", "Rótulo do botão.", "Confirmar"),
            DemoKnob.OptionKnob(
                key = "variant",
                label = "variant",
                options = MnsButtonVariant.entries.map { it.name },
                description = "Nível de ênfase. Use uma única PRIMARY por tela.",
                default = MnsButtonVariant.PRIMARY.name,
            ),
            DemoKnob.OptionKnob(
                key = "size",
                label = "size",
                options = MnsButtonSize.entries.map { it.name },
                description = "Altura e padding vindos dos tokens de sizing.",
                default = MnsButtonSize.MEDIUM.name,
            ),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia o clique e reduz a opacidade.", true),
            DemoKnob.BoolKnob("loading", "loading", "Troca o ícone por spinner e bloqueia o clique.", false),
            DemoKnob.BoolKnob("leadingIcon", "leadingIcon", "Ícone antes do rótulo.", false),
            DemoKnob.BoolKnob("trailingIcon", "trailingIcon", "Ícone depois do rótulo.", false),
            DemoKnob.BoolKnob("fillMaxWidth", "fillMaxWidth", "Ocupa toda a largura disponível.", true),
        ),
    ) { knobs ->
        MnsButton(
            text = knobs.text("text", "Confirmar"),
            onClick = {},
            variant = knobs.enum("variant", MnsButtonVariant.entries.toTypedArray()),
            size = knobs.enum("size", MnsButtonSize.entries.toTypedArray(), MnsButtonSize.MEDIUM),
            enabled = knobs.bool("enabled", true),
            loading = knobs.bool("loading"),
            leadingIcon = Icons.Filled.Add.takeIf { knobs.bool("leadingIcon") },
            trailingIcon = Icons.Filled.Share.takeIf { knobs.bool("trailingIcon") },
            fillMaxWidth = knobs.bool("fillMaxWidth", true),
        )
    },

    DemoComponent(
        id = "mns-icon-button",
        name = "MnsIconButton",
        category = DemoCategory.ACTION,
        summary = "Ação representada só por ícone, com alvo de toque garantido em 48dp.",
        docPath = "docs/components/action/mns-icon-button.md",
        icon = Icons.Filled.Favorite,
        knobs = listOf(
            DemoKnob.OptionKnob(
                key = "variant",
                label = "variant",
                options = MnsButtonVariant.entries.map { it.name },
                description = "Mesma escala de ênfase do MnsButton.",
                default = MnsButtonVariant.TEXT.name,
            ),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia o clique.", true),
            DemoKnob.NumberKnob(
                key = "size",
                label = "size (dp)",
                range = 32f..64f,
                description = "Lado do alvo de toque. Nunca abaixo de 48dp em produção.",
                default = 48f,
                steps = 7,
            ),
        ),
    ) { knobs ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MnsIconButton(
                icon = Icons.Filled.Favorite,
                contentDescription = "Favoritar",
                onClick = {},
                variant = knobs.enum(
                    key = "variant",
                    entries = MnsButtonVariant.entries.toTypedArray(),
                    fallback = MnsButtonVariant.TEXT,
                ),
                enabled = knobs.bool("enabled", true),
                size = knobs.number("size", 48f).dp,
            )
            MnsIconButton(
                icon = Icons.Filled.Share,
                contentDescription = "Compartilhar",
                onClick = {},
                variant = MnsButtonVariant.SECONDARY,
                enabled = knobs.bool("enabled", true),
                size = knobs.number("size", 48f).dp,
            )
            MnsIconButton(
                icon = Icons.Filled.Add,
                contentDescription = "Adicionar",
                onClick = {},
                variant = MnsButtonVariant.OUTLINED,
                enabled = knobs.bool("enabled", true),
                size = knobs.number("size", 48f).dp,
            )
        }
    },

    DemoComponent(
        id = "mns-fab",
        name = "MnsFab",
        category = DemoCategory.ACTION,
        summary = "Ação flutuante única da tela, com estado expandido opcional.",
        docPath = "docs/components/action/mns-fab.md",
        icon = Icons.Filled.Add,
        knobs = listOf(
            DemoKnob.BoolKnob("expanded", "expanded", "Expande em pílula exibindo o rótulo.", false),
            DemoKnob.TextKnob("label", "label", "Rótulo do estado expandido.", "Novo evento"),
        ),
    ) { knobs ->
        MnsFab(
            icon = Icons.Filled.Add,
            contentDescription = "Criar",
            onClick = {},
            expanded = knobs.bool("expanded"),
            label = knobs.text("label", "Novo evento"),
        )
    },

    DemoComponent(
        id = "mns-segmented-control",
        name = "MnsSegmentedControl",
        category = DemoCategory.ACTION,
        summary = "Alternância entre 2 e 4 modos exclusivos, com indicador deslizante.",
        docPath = "docs/components/action/mns-segmented-control.md",
        icon = Icons.Filled.SwapHoriz,
        knobs = listOf(
            DemoKnob.NumberKnob(
                key = "count",
                label = "nº de segmentos",
                range = 2f..4f,
                description = "Acima de 4, prefira MnsTabBar.",
                default = 2f,
                steps = 1,
            ),
            DemoKnob.BoolKnob("icons", "com ícones", "Exibe ícone à esquerda do rótulo.", true),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a troca.", true),
        ),
    ) { knobs ->
        var selected by remember { mutableIntStateOf(0) }
        val labels = listOf("Só ida", "Ida e volta", "Multi-trecho", "Aberto")
        val count = knobs.int("count", 2).coerceIn(2, 4)
        Column {
            MnsSegmentedControl(
                segments = labels.take(count).map {
                    MnsSegment(
                        label = it,
                        icon = Icons.Filled.FlightTakeoff.takeIf { _ -> knobs.bool("icons", true) },
                    )
                },
                selectedIndex = selected.coerceAtMost(count - 1),
                onSelect = { selected = it },
                enabled = knobs.bool("enabled", true),
            )
        }
    },
)
