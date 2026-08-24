package com.mns.demo.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.input.MnsChip
import com.mns.designsystem.component.input.MnsSlider
import com.mns.designsystem.component.input.MnsSwitch
import com.mns.designsystem.component.input.MnsTextField
import com.mns.designsystem.component.layout.MnsCard
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsDivider
import com.mns.designsystem.component.text.MnsSectionHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Painel que transforma a lista de [DemoKnob] de um componente em controles
 * reais, feitos com os próprios componentes do design system.
 *
 * Usar o design system para construir a ferramenta que documenta o design
 * system é intencional: qualquer regressão em `MnsSlider`, `MnsChip` ou
 * `MnsTextField` aparece imediatamente ao abrir qualquer tela do catálogo.
 *
 * @param knobs parâmetros do componente em exibição.
 * @param state estado mutável dos knobs.
 * @param onReset restaura os valores iniciais.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun KnobPanel(
    knobs: List<DemoKnob>,
    state: DemoKnobState,
    modifier: Modifier = Modifier,
    onReset: () -> Unit = { state.reset() },
) {
    if (knobs.isEmpty()) return
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing

    MnsCard(modifier = modifier.fillMaxWidth(), variant = MnsCardVariant.OUTLINED) {
        MnsSectionHeader(
            title = "Parâmetros",
            subtitle = "Cada controle abaixo é um parâmetro real do componente.",
            action = {
                MnsButton(
                    text = "Restaurar",
                    onClick = onReset,
                    variant = MnsButtonVariant.TEXT,
                    size = MnsButtonSize.SMALL,
                )
            },
        )
        knobs.forEachIndexed { index, knob ->
            if (index > 0) MnsDivider()
            Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
                when (knob) {
                    is DemoKnob.BoolKnob -> MnsSwitch(
                        checked = state.bool(knob.key, knob.default),
                        onCheckedChange = { state.set(knob.key, it) },
                        label = knob.label,
                        description = knob.description.ifBlank { null },
                    )

                    is DemoKnob.TextKnob -> MnsTextField(
                        value = state.text(knob.key, knob.default),
                        onValueChange = { state.set(knob.key, it) },
                        label = knob.label,
                        helperText = knob.description.ifBlank { null },
                        singleLine = true,
                    )

                    is DemoKnob.OptionKnob -> {
                        KnobLabel(knob.label, knob.description)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                            verticalArrangement = Arrangement.spacedBy(spacing.sm),
                        ) {
                            knob.options.forEach { option ->
                                MnsChip(
                                    label = option,
                                    selected = state.option(knob.key, knob.default) == option,
                                    onClick = { state.set(knob.key, option) },
                                )
                            }
                        }
                    }

                    is DemoKnob.ColorRoleKnob -> {
                        KnobLabel(knob.label, knob.description)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                            verticalArrangement = Arrangement.spacedBy(spacing.sm),
                        ) {
                            knob.roles.forEach { role ->
                                val selected = state.option(knob.key, knob.default) == role
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    ColorSwatch(colors.byRole(role))
                                    MnsChip(
                                        label = role,
                                        selected = selected,
                                        onClick = { state.set(knob.key, role) },
                                    )
                                }
                            }
                        }
                    }

                    is DemoKnob.NumberKnob -> MnsSlider(
                        value = state.number(knob.key, knob.default),
                        onValueChange = { state.set(knob.key, it) },
                        valueRange = knob.range,
                        steps = knob.steps,
                        label = knob.label,
                        formatValue = knob.format,
                    ).also {
                        if (knob.description.isNotBlank()) {
                            MnsText(
                                text = knob.description,
                                style = MnsTheme.typography.caption,
                                color = colors.textTertiary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KnobLabel(label: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xxs)) {
        MnsText(
            text = label,
            style = MnsTheme.typography.labelMedium,
            color = MnsTheme.colors.textSecondary,
        )
        if (description.isNotBlank()) {
            MnsText(
                text = description,
                style = MnsTheme.typography.caption,
                color = MnsTheme.colors.textTertiary,
            )
        }
    }
}

@Composable
private fun ColorSwatch(color: Color) {
    Box(
        modifier = Modifier
            .size(MnsTheme.sizing.iconSm)
            .clip(MnsTheme.shapes.full)
            .background(color),
    )
}
