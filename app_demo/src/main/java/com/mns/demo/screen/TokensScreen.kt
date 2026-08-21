package com.mns.demo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mns.demo.playground.DemoDensity
import com.mns.demo.playground.ThemeController
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.input.MnsChip
import com.mns.designsystem.component.input.MnsSlider
import com.mns.designsystem.component.input.MnsSwitch
import com.mns.designsystem.component.layout.MnsBottomSheet
import com.mns.designsystem.component.layout.MnsCard
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsScaffold
import com.mns.designsystem.component.layout.MnsTopBar
import com.mns.designsystem.component.text.MnsSectionHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.theme.preset.MnsThemePresets
import com.mns.designsystem.token.MnsColors

/** Tag de teste do JSON exportado. */
internal const val ContractJsonTestTag: String = "contract-json"

/**
 * Playground global de tokens.
 *
 * É a tela que fecha o ciclo do design system: você troca o preset, mexe na
 * cor primária, no raio e na densidade, vê o app inteiro responder — e exporta
 * o resultado como Design Contract em JSON, pronto para virar tema no seu app
 * ou ser revisado em pull request.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun TokensScreen(
    theme: ThemeController,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var contractJson by remember { mutableStateOf<String?>(null) }

    MnsScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MnsTopBar(
                title = "Tokens",
                subtitle = "Edição ao vivo · exportação de contrato",
                onNavigateBack = onBack,
                actions = {
                    MnsIconButton(
                        icon = Icons.Filled.Restore,
                        contentDescription = "Restaurar tokens do preset",
                        onClick = { theme.reset() },
                        variant = MnsButtonVariant.TEXT,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = MnsTheme.spacing.screenHorizontal,
                    vertical = MnsTheme.spacing.base,
                ),
            verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.lg),
        ) {
            // ── Preset ───────────────────────────────────────────────────────
            MnsCard(variant = MnsCardVariant.OUTLINED) {
                MnsSectionHeader(
                    title = "Preset",
                    subtitle = "Cada preset foi extraído de um print de design real.",
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm)) {
                    MnsThemePresets.all.forEach { preset ->
                        MnsChip(
                            label = preset.displayName,
                            selected = theme.provider.id == preset.id,
                            onClick = { theme.selectProvider(preset) },
                        )
                    }
                }
                MnsSwitch(
                    checked = theme.darkMode,
                    onCheckedChange = { theme.darkMode = it },
                    label = "Modo escuro",
                    description = "Usa a variante dark do preset selecionado.",
                )
            }

            // ── Cor ──────────────────────────────────────────────────────────
            MnsCard(variant = MnsCardVariant.OUTLINED) {
                MnsSectionHeader(
                    title = "Cor primária",
                    subtitle = "Trocar aqui repinta ações, foco e links em todo o app.",
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                ) {
                    MnsChip(
                        label = "Do preset",
                        selected = theme.primaryOverride == null,
                        onClick = { theme.primaryOverride = null },
                    )
                    ThemeController.primaryChoices.forEach { (nome, cor) ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Swatch(cor)
                            MnsChip(
                                label = nome,
                                selected = theme.primaryOverride == cor,
                                onClick = { theme.primaryOverride = cor },
                            )
                        }
                    }
                }
            }

            // ── Forma e densidade ────────────────────────────────────────────
            MnsCard(variant = MnsCardVariant.OUTLINED) {
                MnsSectionHeader(title = "Forma e densidade")
                MnsSlider(
                    value = if (theme.baseRadius == ThemeController.DEFAULT_RADIUS_SENTINEL) {
                        MnsTheme.shapes.baseRadius.value
                    } else {
                        theme.baseRadius
                    },
                    onValueChange = { theme.baseRadius = it },
                    valueRange = 0f..32f,
                    steps = 32,
                    label = "shapes.baseRadius",
                    formatValue = { "${it.toInt()}dp" },
                )
                MnsSwitch(
                    checked = theme.pillButtons,
                    onCheckedChange = { theme.pillButtons = it },
                    label = "Botões em pílula",
                    description = "Força shapes.button para 50%.",
                )
                MnsText(
                    text = "spacing.density",
                    style = MnsTheme.typography.labelMedium,
                    color = MnsTheme.colors.textSecondary,
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm)) {
                    DemoDensity.entries.forEach { densidade ->
                        MnsChip(
                            label = densidade.label,
                            selected = theme.density == densidade,
                            onClick = { theme.density = densidade },
                        )
                    }
                }
                MnsSlider(
                    value = theme.typographyScale,
                    onValueChange = { theme.typographyScale = it },
                    valueRange = 0.85f..1.3f,
                    steps = 9,
                    label = "typography.scale",
                    formatValue = { "%.2f×".format(it) },
                )
            }

            // ── Elevação e movimento ─────────────────────────────────────────
            MnsCard(variant = MnsCardVariant.OUTLINED) {
                MnsSectionHeader(title = "Elevação e movimento")
                MnsSwitch(
                    checked = theme.flatElevation,
                    onCheckedChange = { theme.flatElevation = it },
                    label = "Elevação flat",
                    description = "Remove todas as sombras (MnsElevation.Flat).",
                )
                MnsSwitch(
                    checked = theme.reduceMotion,
                    onCheckedChange = { theme.reduceMotion = it },
                    label = "Reduzir movimento",
                    description = "Zera todas as durações — mesmo caminho da preferência de acessibilidade.",
                )
            }

            // ── Paleta resolvida ─────────────────────────────────────────────
            MnsCard(variant = MnsCardVariant.OUTLINED) {
                MnsSectionHeader(
                    title = "Paleta resolvida",
                    subtitle = "${MnsColors.roleNames.size} papéis semânticos em vigor.",
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                ) {
                    MnsColors.roleNames.forEach { role ->
                        Column(
                            modifier = Modifier.width(96.dp),
                            verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xxs),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(28.dp)
                                    .clip(MnsTheme.shapes.small)
                                    .background(MnsTheme.colors.byRole(role)),
                            )
                            MnsText(
                                text = role,
                                style = MnsTheme.typography.caption,
                                color = MnsTheme.colors.textTertiary,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }

            MnsButton(
                text = "Exportar Design Contract (JSON)",
                onClick = { contractJson = theme.exportContractJson() },
                leadingIcon = Icons.Filled.Code,
                fillMaxWidth = true,
                size = MnsButtonSize.LARGE,
            )
        }
    }

    val json = contractJson
    if (json != null) {
        MnsBottomSheet(
            onDismissRequest = { contractJson = null },
            title = "Design Contract",
            subtitle = "Cole em docs/contracts/ ou carregue via MnsDesignContractCodec.toProvider().",
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MnsTheme.shapes.medium)
                    .background(MnsTheme.colors.surfaceVariant)
                    .horizontalScroll(rememberScrollState())
                    .padding(MnsTheme.spacing.md),
            ) {
                MnsText(
                    text = json,
                    modifier = Modifier.testTag(ContractJsonTestTag),
                    style = MnsTheme.typography.mono,
                    color = MnsTheme.colors.textPrimary,
                    softWrap = false,
                )
            }
        }
    }
}

@Composable
private fun Swatch(color: Color) {
    Box(
        modifier = Modifier
            .size(MnsTheme.sizing.iconSm)
            .clip(MnsTheme.shapes.full)
            .background(color),
    )
}
