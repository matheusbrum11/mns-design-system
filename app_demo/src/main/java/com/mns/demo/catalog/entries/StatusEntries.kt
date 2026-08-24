package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.status.MnsAlert
import com.mns.designsystem.component.status.MnsBadge
import com.mns.designsystem.component.status.MnsBadgedBox
import com.mns.designsystem.component.status.MnsCircularProgress
import com.mns.designsystem.component.status.MnsEmptyState
import com.mns.designsystem.component.status.MnsLinearProgress
import com.mns.designsystem.component.status.MnsRating
import com.mns.designsystem.component.status.MnsTag
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/** Entradas do catálogo para a categoria [DemoCategory.STATUS]. */
internal fun statusEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-badge",
        name = "MnsBadge",
        category = DemoCategory.STATUS,
        summary = "Contador ou ponto de novidade ancorado a um ícone.",
        docPath = "docs/components/status/mns-badge.md",
        icon = Icons.Filled.NotificationsActive,
        knobs = listOf(
            DemoKnob.NumberKnob("count", "count", 0f..150f, "Valor exibido.", 3f, 150, { "${it.toInt()}" }),
            DemoKnob.NumberKnob("max", "max", 9f..99f, "Acima disso vira \"max+\".", 99f, 90, { "${it.toInt()}" }),
            DemoKnob.BoolKnob("dot", "sem contador", "Renderiza apenas o ponto.", false),
            DemoKnob.OptionKnob(
                key = "status",
                label = "status",
                options = MnsStatus.entries.map { it.name },
                description = "Intenção semântica; define a cor.",
                default = MnsStatus.DANGER.name,
            ),
        ),
    ) { knobs ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MnsBadgedBox(
                badge = {
                    MnsBadge(
                        count = if (knobs.bool("dot")) null else knobs.int("count", 3),
                        max = knobs.int("max", 99),
                        status = knobs.enum("status", MnsStatus.entries.toTypedArray(), MnsStatus.DANGER),
                    )
                },
            ) {
                MnsIcon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificações",
                    size = MnsTheme.sizing.iconLg,
                )
            }
            MnsBadge(
                count = if (knobs.bool("dot")) null else knobs.int("count", 3),
                max = knobs.int("max", 99),
                status = knobs.enum("status", MnsStatus.entries.toTypedArray(), MnsStatus.DANGER),
            )
        }
    },

    DemoComponent(
        id = "mns-tag",
        name = "MnsTag",
        category = DemoCategory.STATUS,
        summary = "Rótulo estático de estado. Não é interativo — para isso use MnsChip.",
        docPath = "docs/components/status/mns-tag.md",
        icon = Icons.Filled.Sell,
        knobs = listOf(
            DemoKnob.TextKnob("text", "text", "Texto da tag.", "Confirmado"),
            DemoKnob.BoolKnob("solid", "solid", "Usa a cor sólida em vez do container suave.", false),
            DemoKnob.BoolKnob("icon", "icon", "Ícone antes do texto.", false),
        ),
    ) { knobs ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MnsStatus.entries.forEach { status ->
                MnsTag(
                    text = if (status == MnsStatus.NEUTRAL) knobs.text("text", "Tag") else status.name.lowercase(),
                    status = status,
                    solid = knobs.bool("solid"),
                    icon = Icons.Filled.Star.takeIf { knobs.bool("icon") },
                )
            }
        }
    },

    DemoComponent(
        id = "mns-alert",
        name = "MnsAlert",
        category = DemoCategory.STATUS,
        summary = "Mensagem contextual anunciada automaticamente por leitores de tela.",
        docPath = "docs/components/status/mns-alert.md",
        icon = Icons.Filled.WarningAmber,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "Título em negrito.", "Pagamento pendente"),
            DemoKnob.TextKnob(
                key = "message",
                label = "message",
                description = "Texto principal.",
                default = "Conclua o pagamento em até 15 minutos para garantir os assentos.",
            ),
            DemoKnob.OptionKnob(
                key = "status",
                label = "status",
                options = MnsStatus.entries.map { it.name },
                description = "Define cor e ícone padrão.",
                default = MnsStatus.WARNING.name,
            ),
            DemoKnob.BoolKnob("dismiss", "onDismiss", "Exibe o botão de fechar.", true),
            DemoKnob.BoolKnob("action", "action", "Exibe uma ação embutida.", true),
        ),
    ) { knobs ->
        MnsAlert(
            message = knobs.text("message", "Mensagem"),
            title = knobs.text("title").ifBlank { null },
            status = knobs.enum("status", MnsStatus.entries.toTypedArray(), MnsStatus.WARNING),
            onDismiss = if (knobs.bool("dismiss", true)) ({}) else null,
            action = if (knobs.bool("action", true)) {
                {
                    MnsButton(
                        text = "Pagar agora",
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
        id = "mns-progress",
        name = "MnsCircularProgress · MnsLinearProgress",
        category = DemoCategory.STATUS,
        summary = "Progresso determinado ou indeterminado, com semântica correta.",
        docPath = "docs/components/status/mns-progress.md",
        icon = Icons.Filled.HourglassEmpty,
        knobs = listOf(
            DemoKnob.BoolKnob("indeterminate", "progress = null", "Modo indeterminado.", false),
            DemoKnob.NumberKnob(
                key = "progress",
                label = "progress",
                range = 0f..1f,
                description = "Fração concluída.",
                default = 0.62f,
                steps = 20,
                format = { "%.0f%%".format(it * 100) },
            ),
        ),
    ) { knobs ->
        val progress = if (knobs.bool("indeterminate")) null else knobs.number("progress", 0.62f)
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.lg)) {
            MnsLinearProgress(progress = progress, contentDescription = "Progresso do check-in")
            Row(
                horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MnsCircularProgress(progress = progress, size = MnsTheme.sizing.iconLg)
                MnsCircularProgress(progress = progress, size = MnsTheme.sizing.iconXl)
                MnsCircularProgress(progress = progress, size = MnsTheme.sizing.avatarLg, strokeWidth = MnsTheme.borders.thick * 3)
            }
        }
    },

    DemoComponent(
        id = "mns-rating",
        name = "MnsRating",
        category = DemoCategory.STATUS,
        summary = "Avaliação por estrelas, somente leitura ou interativa.",
        docPath = "docs/components/status/mns-rating.md",
        icon = Icons.Filled.Star,
        knobs = listOf(
            DemoKnob.NumberKnob("rating", "rating", 0f..5f, "Nota atual.", 4.6f, 50, { "%.1f".format(it) }),
            DemoKnob.BoolKnob("showValue", "showValue", "Exibe a nota numérica.", true),
            DemoKnob.BoolKnob("interactive", "onRatingChange", "Torna as estrelas tocáveis.", false),
        ),
    ) { knobs ->
        var nota by remember { mutableFloatStateOf(4.6f) }
        MnsRating(
            rating = if (knobs.bool("interactive")) nota else knobs.number("rating", 4.6f),
            showValue = knobs.bool("showValue", true),
            onRatingChange = if (knobs.bool("interactive")) ({ nota = it.toFloat() }) else null,
        )
    },

    DemoComponent(
        id = "mns-empty-state",
        name = "MnsEmptyState",
        category = DemoCategory.STATUS,
        summary = "Estado vazio que responde o quê, por quê e qual o próximo passo.",
        docPath = "docs/components/status/mns-empty-state.md",
        icon = Icons.Filled.EventBusy,
        knobs = listOf(
            DemoKnob.TextKnob("title", "title", "O que está vazio.", "Nenhum evento por aqui"),
            DemoKnob.TextKnob(
                key = "description",
                label = "description",
                description = "Explicação e próximo passo.",
                default = "Nenhum evento corresponde aos filtros selecionados.",
            ),
            DemoKnob.BoolKnob("icon", "icon", "Exibe a ilustração.", true),
            DemoKnob.BoolKnob("action", "action", "Exibe a ação de saída.", true),
        ),
    ) { knobs ->
        MnsEmptyState(
            title = knobs.text("title", "Nada aqui"),
            description = knobs.text("description").ifBlank { null },
            icon = Icons.Filled.EventBusy.takeIf { knobs.bool("icon", true) },
            action = if (knobs.bool("action", true)) {
                { MnsButton(text = "Limpar filtros", onClick = {}, variant = MnsButtonVariant.SECONDARY) }
            } else {
                null
            },
        )
    },
)
