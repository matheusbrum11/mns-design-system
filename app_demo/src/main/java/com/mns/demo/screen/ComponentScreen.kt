package com.mns.demo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnobState
import com.mns.demo.playground.KnobPanel
import com.mns.demo.playground.ThemeController
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.layout.MnsCard
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsScaffold
import com.mns.designsystem.component.layout.MnsTopBar
import com.mns.designsystem.component.layout.MnsTopBarAlignment
import com.mns.designsystem.component.status.MnsTag
import com.mns.designsystem.component.text.MnsSectionHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/** Tag de teste da área de preview — usada pelos testes de integração. */
internal const val PreviewAreaTestTag: String = "component-preview-area"

/**
 * Tela de um componente.
 *
 * Três blocos, na ordem em que a pessoa precisa deles:
 * **cabeçalho** (o que é e quando usar) → **preview ao vivo** → **parâmetros**.
 * Mexer em qualquer parâmetro recompõe apenas o preview, então a resposta é
 * imediata mesmo em componentes pesados como o QR Code.
 */
@Composable
internal fun ComponentScreen(
    component: DemoComponent,
    theme: ThemeController,
    onBack: () -> Unit,
    onOpenDocs: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val knobState = remember(component.id) { DemoKnobState(component.knobs) }

    MnsScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MnsTopBar(
                title = component.name,
                subtitle = component.category.label,
                alignment = MnsTopBarAlignment.START,
                onNavigateBack = onBack,
                actions = {
                    MnsIconButton(
                        icon = if (theme.darkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = if (theme.darkMode) "Usar tema claro" else "Usar tema escuro",
                        onClick = { theme.darkMode = !theme.darkMode },
                        variant = MnsButtonVariant.TEXT,
                    )
                    MnsIconButton(
                        icon = Icons.Filled.MenuBook,
                        contentDescription = "Ver documentação",
                        onClick = { onOpenDocs(component.docPath) },
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
            Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm)) {
                MnsText(
                    text = component.summary,
                    style = MnsTheme.typography.bodyLarge,
                    color = MnsTheme.colors.textSecondary,
                )
                MnsTag(text = component.docPath, status = MnsStatus.NEUTRAL)
            }

            MnsSectionHeader(title = "Preview")

            MnsCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(PreviewAreaTestTag),
                variant = MnsCardVariant.OUTLINED,
                contentPadding = if (component.fullWidthPreview) {
                    MnsTheme.spacing.none
                } else {
                    MnsTheme.spacing.base
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    component.preview(knobState)
                }
            }

            KnobPanel(knobs = component.knobs, state = knobState)
        }
    }
}
