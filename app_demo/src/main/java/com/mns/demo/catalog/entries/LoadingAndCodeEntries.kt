package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.code.MnsQrCode
import com.mns.designsystem.component.code.MnsQrDotStyle
import com.mns.designsystem.component.code.MnsQrErrorCorrection
import com.mns.designsystem.component.code.MnsTicketCard
import com.mns.designsystem.component.loading.MnsShimmerBox
import com.mns.designsystem.component.loading.MnsShimmerCard
import com.mns.designsystem.component.loading.MnsShimmerListItem
import com.mns.designsystem.component.loading.MnsShimmerParagraph
import com.mns.designsystem.component.loading.mnsShimmer
import com.mns.designsystem.component.media.MnsCover
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.media.MnsIcons
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/** Entradas do catálogo para carregamento, códigos e mídia. */
internal fun loadingAndCodeEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-shimmer",
        name = "mnsShimmer · MnsShimmerBox",
        category = DemoCategory.LOADING,
        summary = "Modifier de shimmer aplicável a qualquer elemento, com bloco pronto.",
        docPath = "docs/components/loading/mns-shimmer.md",
        icon = Icons.Filled.ViewDay,
        knobs = listOf(
            DemoKnob.BoolKnob("visible", "visible", "Desliga o efeito sem remover o modifier.", true),
            DemoKnob.NumberKnob("height", "height (dp)", 8f..48f, "Altura do bloco.", 16f, 10),
            DemoKnob.OptionKnob(
                key = "shape",
                label = "shape",
                options = listOf("shimmer", "small", "medium", "large", "full"),
                description = "Recorte do bloco.",
                default = "shimmer",
            ),
        ),
    ) { knobs ->
        val shapes = MnsTheme.shapes
        val shape = when (knobs.option("shape", "shimmer")) {
            "small" -> shapes.small
            "medium" -> shapes.medium
            "large" -> shapes.large
            "full" -> shapes.full
            else -> shapes.shimmer
        }
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md)) {
            MnsShimmerBox(
                height = knobs.number("height", 16f).dp,
                shape = shape,
                visible = knobs.bool("visible", true),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .mnsShimmer(visible = knobs.bool("visible", true), shape = shapes.avatar),
                )
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 64.dp)
                        .mnsShimmer(visible = knobs.bool("visible", true), shape = shape),
                )
            }
        }
    },

    DemoComponent(
        id = "mns-skeletons",
        name = "MnsShimmerParagraph · ListItem · Card",
        category = DemoCategory.LOADING,
        summary = "Esqueletos que espelham o layout real, evitando salto ao carregar.",
        docPath = "docs/components/loading/mns-skeletons.md",
        icon = Icons.Filled.ViewAgenda,
        knobs = listOf(
            DemoKnob.NumberKnob("lines", "lines", 1f..6f, "Linhas do parágrafo.", 3f, 5),
            DemoKnob.BoolKnob("avatar", "showAvatar", "Círculo do avatar no item de lista.", true),
            DemoKnob.BoolKnob("visible", "visible", "Liga/desliga a animação.", true),
        ),
    ) { knobs ->
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xl)) {
            MnsShimmerParagraph(lines = knobs.int("lines", 3), visible = knobs.bool("visible", true))
            MnsShimmerListItem(showAvatar = knobs.bool("avatar", true), visible = knobs.bool("visible", true))
            MnsShimmerCard(visible = knobs.bool("visible", true))
        }
    },

    DemoComponent(
        id = "mns-qr-code",
        name = "MnsQrCode",
        category = DemoCategory.CODE,
        summary = "QR Code renderizado com tokens, a partir de texto ou de matriz pronta.",
        docPath = "docs/components/code/mns-qr-code.md",
        icon = Icons.Filled.QrCode2,
        knobs = listOf(
            DemoKnob.TextKnob("content", "content", "Texto codificado.", "MNS-TICKET-8842"),
            DemoKnob.OptionKnob(
                key = "dotStyle",
                label = "dotStyle",
                options = MnsQrDotStyle.entries.map { it.name },
                description = "Forma dos módulos. Afeta só a estética.",
                default = MnsQrDotStyle.SQUARE.name,
            ),
            DemoKnob.OptionKnob(
                key = "ec",
                label = "errorCorrection",
                options = MnsQrErrorCorrection.entries.map { it.name },
                description = "Quanto o código sobrevive a sujeira e logo sobreposto.",
                default = MnsQrErrorCorrection.MEDIUM.name,
            ),
            DemoKnob.NumberKnob("quiet", "quietZoneModules", 0f..8f, "Margem branca, em módulos. ISO manda 4.", 4f, 8),
            DemoKnob.NumberKnob("size", "size (dp)", 140f..280f, "Lado do QR.", 220f, 14),
            DemoKnob.TextKnob("caption", "caption", "Legenda abaixo.", "Apresente na entrada"),
        ),
    ) { knobs ->
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            MnsQrCode(
                content = knobs.text("content", "MNS").ifBlank { "MNS" },
                size = knobs.number("size", 220f).dp,
                errorCorrection = knobs.enum(
                    key = "ec",
                    entries = MnsQrErrorCorrection.entries.toTypedArray(),
                    fallback = MnsQrErrorCorrection.MEDIUM,
                ),
                dotStyle = knobs.enum("dotStyle", MnsQrDotStyle.entries.toTypedArray(), MnsQrDotStyle.SQUARE),
                quietZoneModules = knobs.int("quiet", 4),
                caption = knobs.text("caption").ifBlank { null },
                contentDescription = "Ingresso de demonstração",
            )
        }
    },

    DemoComponent(
        id = "mns-ticket-card",
        name = "MnsTicketCard",
        category = DemoCategory.CODE,
        summary = "Ingresso completo: cabeçalho, detalhes, picote e QR.",
        docPath = "docs/components/code/mns-ticket-card.md",
        icon = Icons.Filled.ConfirmationNumber,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "Nome do evento.", "Newport Beach Jazz Festival"),
            DemoKnob.TextKnob("subtitle", "subtitle", "Local e data.", "Sydney, Australia · 19 Oct 2024"),
            DemoKnob.TextKnob("qr", "qrContent", "Conteúdo codificado.", "MNS-TICKET-8842"),
            DemoKnob.TextKnob("footnote", "footnote", "Instrução abaixo do código.", "Apresente este código na entrada"),
            DemoKnob.BoolKnob("details", "details", "Exibe a grade de detalhes.", true),
        ),
    ) { knobs ->
        MnsTicketCard(
            title = knobs.text("title", "Evento"),
            subtitle = knobs.text("subtitle").ifBlank { null },
            qrContent = knobs.text("qr", "MNS").ifBlank { "MNS" },
            footnote = knobs.text("footnote").ifBlank { null },
            details = if (knobs.bool("details", true)) {
                listOf(
                    "Assento" to "D17, D18",
                    "Portão" to "G2",
                    "Tipo" to "E-Ticket",
                    "Pedido" to "#88420",
                )
            } else {
                emptyList()
            },
        )
    },

    DemoComponent(
        id = "mns-icon",
        name = "MnsIcon · MnsIcons",
        category = DemoCategory.MEDIA,
        summary = "Ícone que herda a cor da superfície, mais o conjunto próprio da lib.",
        docPath = "docs/components/media/mns-icon.md",
        icon = Icons.Filled.Insights,
        knobs = listOf(
            DemoKnob.OptionKnob(
                key = "size",
                label = "size",
                options = listOf("iconXs", "iconSm", "iconMd", "iconLg", "iconXl", "iconXxl"),
                description = "Token de dimensão.",
                default = "iconLg",
            ),
            DemoKnob.ColorRoleKnob(
                key = "tint",
                label = "tint",
                roles = listOf("onSurface", "primary", "accent", "success", "warning", "danger", "textTertiary"),
                description = "Papel semântico da cor.",
                default = "onSurface",
            ),
        ),
    ) { knobs ->
        val s = MnsTheme.sizing
        val size = when (knobs.option("size", "iconLg")) {
            "iconXs" -> s.iconXs
            "iconSm" -> s.iconSm
            "iconMd" -> s.iconMd
            "iconXl" -> s.iconXl
            "iconXxl" -> s.iconXxl
            else -> s.iconLg
        }
        val tint = MnsTheme.colors.byRole(knobs.option("tint", "onSurface"))
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md)) {
            MnsText("Conjunto próprio (MnsIcons)", style = MnsTheme.typography.labelMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.base)) {
                MnsIcon(MnsIcons.Visibility, "Mostrar", size = size, tint = tint)
                MnsIcon(MnsIcons.VisibilityOff, "Ocultar", size = size, tint = tint)
                MnsIcon(MnsIcons.Minus, "Diminuir", size = size, tint = tint)
                MnsIcon(MnsIcons.Bookmark, "Salvar", size = size, tint = tint)
                MnsIcon(MnsIcons.QrCode, "Código", size = size, tint = tint)
            }
        }
    },

    DemoComponent(
        id = "mns-cover",
        name = "MnsCover",
        category = DemoCategory.MEDIA,
        summary = "Área de capa com placeholder shimmer, véu de legibilidade e overlay.",
        docPath = "docs/components/media/mns-cover.md",
        icon = Icons.Filled.Image,
        knobs = listOf(
            DemoKnob.NumberKnob("height", "height (dp)", 100f..280f, "Altura da capa.", 180f, 18),
            DemoKnob.BoolKnob("scrim", "scrim", "Gradiente para legibilidade do texto.", true),
            DemoKnob.BoolKnob("overlay", "overlay", "Conteúdo sobreposto.", true),
        ),
    ) { knobs ->
        MnsCover(
            painter = null,
            contentDescription = "Capa do evento",
            height = knobs.number("height", 180f).dp,
            scrim = knobs.bool("scrim", true),
            overlay = if (knobs.bool("overlay", true)) {
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(knobs.number("height", 180f).dp),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        MnsText(
                            text = "North Van Hiking",
                            modifier = Modifier.padding(MnsTheme.spacing.base),
                            style = MnsTheme.typography.headlineSmall,
                            color = MnsTheme.colors.textInverse,
                        )
                    }
                }
            } else {
                null
            },
        )
    },
)
