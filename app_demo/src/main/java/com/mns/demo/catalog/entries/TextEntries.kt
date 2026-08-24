package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.ViewHeadline
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.text.MnsCompactNumberText
import com.mns.designsystem.component.text.MnsCurrencyText
import com.mns.designsystem.component.text.MnsHeading
import com.mns.designsystem.component.text.MnsHeadingLevel
import com.mns.designsystem.component.text.MnsPercentText
import com.mns.designsystem.component.text.MnsSectionHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.format.MnsCurrencyFormat
import com.mns.designsystem.format.MnsPercentFormat
import com.mns.designsystem.theme.MnsTheme

/** Entradas do catálogo para a categoria [DemoCategory.TEXT]. */
internal fun textEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-text",
        name = "MnsText",
        category = DemoCategory.TEXT,
        summary = "Texto base: herda estilo do container e cor da superfície.",
        docPath = "docs/components/text/mns-text.md",
        icon = Icons.Filled.ViewHeadline,
        knobs = listOf(
            DemoKnob.TextKnob(
                key = "text",
                label = "text",
                description = "Conteúdo exibido.",
                default = "O MNS entrega tipografia consistente sem repetir estilo em cada tela.",
            ),
            DemoKnob.OptionKnob(
                key = "style",
                label = "style",
                options = listOf(
                    "displayLarge", "displayMedium", "displaySmall",
                    "headlineLarge", "headlineMedium", "headlineSmall",
                    "titleLarge", "titleMedium", "titleSmall",
                    "bodyLarge", "bodyMedium", "bodySmall",
                    "labelLarge", "labelMedium", "labelSmall",
                    "caption", "overline", "mono",
                ),
                description = "Papel tipográfico do token.",
                default = "bodyMedium",
            ),
            DemoKnob.ColorRoleKnob(
                key = "color",
                label = "color",
                roles = listOf("textPrimary", "textSecondary", "textTertiary", "primary", "danger", "success"),
                description = "Papel semântico da cor — nunca um hex cru.",
                default = "textPrimary",
            ),
            DemoKnob.NumberKnob("maxLines", "maxLines", 1f..6f, "Limite de linhas.", 3f, 5),
        ),
    ) { knobs ->
        val t = MnsTheme.typography
        val style = when (knobs.option("style", "bodyMedium")) {
            "displayLarge" -> t.displayLarge
            "displayMedium" -> t.displayMedium
            "displaySmall" -> t.displaySmall
            "headlineLarge" -> t.headlineLarge
            "headlineMedium" -> t.headlineMedium
            "headlineSmall" -> t.headlineSmall
            "titleLarge" -> t.titleLarge
            "titleMedium" -> t.titleMedium
            "titleSmall" -> t.titleSmall
            "bodyLarge" -> t.bodyLarge
            "bodySmall" -> t.bodySmall
            "labelLarge" -> t.labelLarge
            "labelMedium" -> t.labelMedium
            "labelSmall" -> t.labelSmall
            "caption" -> t.caption
            "overline" -> t.overline
            "mono" -> t.mono
            else -> t.bodyMedium
        }
        MnsText(
            text = knobs.text("text", "Texto de exemplo"),
            style = style,
            color = MnsTheme.colors.byRole(knobs.option("color", "textPrimary")),
            maxLines = knobs.int("maxLines", 3),
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        )
    },

    DemoComponent(
        id = "mns-heading",
        name = "MnsHeading",
        category = DemoCategory.TEXT,
        summary = "Título com semântica de cabeçalho para leitores de tela.",
        docPath = "docs/components/text/mns-heading.md",
        icon = Icons.Filled.Title,
        knobs = listOf(
            DemoKnob.TextKnob("text", "text", "Texto do título.", "Today Events"),
            DemoKnob.OptionKnob(
                key = "level",
                label = "level",
                options = MnsHeadingLevel.entries.map { it.name },
                description = "Nível hierárquico; afeta estilo e navegação por títulos.",
                default = MnsHeadingLevel.H1.name,
            ),
            DemoKnob.TextKnob("overline", "overline", "Rótulo em caixa alta acima.", "Vancouver"),
            DemoKnob.TextKnob("subtitle", "subtitle", "Linha de apoio abaixo.", "12 eventos perto de você"),
        ),
    ) { knobs ->
        MnsHeading(
            text = knobs.text("text", "Today Events"),
            level = knobs.enum("level", MnsHeadingLevel.entries.toTypedArray(), MnsHeadingLevel.H1),
            overline = knobs.text("overline").ifBlank { null },
            subtitle = knobs.text("subtitle").ifBlank { null },
        )
    },

    DemoComponent(
        id = "mns-section-header",
        name = "MnsSectionHeader",
        category = DemoCategory.TEXT,
        summary = "Título de seção com ação à direita — o padrão \"… See All\".",
        docPath = "docs/components/text/mns-section-header.md",
        icon = Icons.Filled.Title,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "Título da seção.", "Popular Events"),
            DemoKnob.TextKnob("subtitle", "subtitle", "Linha de apoio.", ""),
            DemoKnob.BoolKnob("action", "action", "Exibe a ação à direita.", true),
        ),
    ) { knobs ->
        MnsSectionHeader(
            title = knobs.text("title", "Popular Events"),
            subtitle = knobs.text("subtitle").ifBlank { null },
            action = if (knobs.bool("action", true)) {
                {
                    MnsButton(
                        text = "See All",
                        onClick = {},
                        variant = MnsButtonVariant.TEXT,
                        size = MnsButtonSize.SMALL,
                    )
                }
            } else {
                null
            },
        )
    },

    DemoComponent(
        id = "mns-currency-text",
        name = "MnsCurrencyText",
        category = DemoCategory.TEXT,
        summary = "Valor monetário a partir de centavos, sem erro de arredondamento.",
        docPath = "docs/components/text/mns-currency-text.md",
        icon = Icons.Filled.AttachMoney,
        knobs = listOf(
            DemoKnob.NumberKnob(
                key = "cents",
                label = "cents",
                range = -50000f..500000f,
                description = "Valor em centavos. Negativo demonstra o estilo de sinal.",
                default = 12550f,
                steps = 100,
                format = { "${it.toLong()}" },
            ),
            DemoKnob.OptionKnob(
                key = "currency",
                label = "format",
                options = listOf("BRL", "USD", "EUR"),
                description = "Moeda e locale.",
                default = "BRL",
            ),
            DemoKnob.BoolKnob("colorize", "colorizeSign", "Verde/vermelho conforme o sinal.", false),
            DemoKnob.BoolKnob("emphasize", "emphasizeSymbol", "Símbolo menor que o número.", true),
        ),
    ) { knobs ->
        MnsCurrencyText(
            cents = knobs.number("cents", 12550f).toLong(),
            format = when (knobs.option("currency", "BRL")) {
                "USD" -> MnsCurrencyFormat.USD
                "EUR" -> MnsCurrencyFormat.EUR
                else -> MnsCurrencyFormat.BRL
            },
            style = MnsTheme.typography.displaySmall,
            colorizeSign = knobs.bool("colorize"),
            emphasizeSymbol = knobs.bool("emphasize", true),
        )
    },

    DemoComponent(
        id = "mns-percent-text",
        name = "MnsPercentText",
        category = DemoCategory.TEXT,
        summary = "Percentual formatado, com sinal e cor opcionais.",
        docPath = "docs/components/text/mns-percent-text.md",
        icon = Icons.Filled.Percent,
        knobs = listOf(
            DemoKnob.NumberKnob(
                key = "value",
                label = "value",
                range = -1f..1f,
                description = "Fração (0.42 = 42%).",
                default = 0.184f,
                steps = 40,
                format = { "%.3f".format(it) },
            ),
            DemoKnob.OptionKnob(
                key = "format",
                label = "format",
                options = listOf("Default", "Whole", "Signed"),
                description = "Preset de casas decimais e sinal.",
                default = "Signed",
            ),
            DemoKnob.BoolKnob("colorize", "colorizeSign", "Verde/vermelho conforme o sinal.", true),
        ),
    ) { knobs ->
        MnsPercentText(
            value = knobs.number("value", 0.184f).toDouble(),
            format = when (knobs.option("format", "Signed")) {
                "Whole" -> MnsPercentFormat.Whole
                "Signed" -> MnsPercentFormat.Signed
                else -> MnsPercentFormat.Default
            },
            style = MnsTheme.typography.headlineMedium,
            colorizeSign = knobs.bool("colorize", true),
        )
    },

    DemoComponent(
        id = "mns-compact-number-text",
        name = "MnsCompactNumberText",
        category = DemoCategory.TEXT,
        summary = "Número grande abreviado, com valor completo na acessibilidade.",
        docPath = "docs/components/text/mns-compact-number-text.md",
        icon = Icons.Filled.Tag,
        knobs = listOf(
            DemoKnob.NumberKnob(
                key = "value",
                label = "value",
                range = 0f..5_000_000f,
                description = "Número a formatar.",
                default = 12400f,
                steps = 200,
                format = { "${it.toLong()}" },
            ),
        ),
    ) { knobs ->
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm)) {
            MnsCompactNumberText(
                value = knobs.number("value", 12400f).toLong(),
                style = MnsTheme.typography.headlineMedium,
            )
            MnsText(
                text = "participantes confirmados",
                style = MnsTheme.typography.bodySmall,
                color = MnsTheme.colors.textSecondary,
            )
        }
    },
)
