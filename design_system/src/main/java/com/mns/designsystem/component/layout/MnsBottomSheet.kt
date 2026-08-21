package com.mns.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Bottom sheet modal tokenizado.
 *
 * Envolve o `ModalBottomSheet` do Material 3 apenas para aplicar forma, cores,
 * scrim e alça vindos dos tokens — e para padronizar o cabeçalho (título +
 * botão de fechar), que é onde as implementações costumam divergir.
 *
 * ```kotlin
 * if (mostrarFiltros) {
 *     MnsBottomSheet(title = "Filtros", onDismissRequest = { mostrarFiltros = false }) {
 *         MnsChipRow(opcoes, selecionados, ::alternar)
 *         MnsButton("Aplicar", ::aplicar, fillMaxWidth = true)
 *     }
 * }
 * ```
 *
 * @param onDismissRequest chamado ao arrastar para baixo, tocar no scrim ou
 *   acionar o botão de fechar.
 * @param title título do cabeçalho. `null` remove o cabeçalho inteiro.
 * @param subtitle linha de apoio sob o título.
 * @param showHandle exibe a alça de arraste no topo.
 * @param showCloseButton exibe o "✕" no cabeçalho. Mantenha ligado: nem todo
 *   usuário descobre o gesto de arrastar.
 * @param sheetState estado do sheet; injete para controlar expansão programática.
 * @param content conteúdo do sheet. Já recebe padding lateral dos tokens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MnsBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    showHandle: Boolean = true,
    showCloseButton: Boolean = true,
    containerColor: Color = MnsTheme.colors.surfaceElevated,
    scrimColor: Color = MnsTheme.colors.scrim.copy(alpha = MnsTheme.opacity.scrim),
    shape: Shape = MnsTheme.shapes.bottomSheet,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = colors.onSurface,
        scrimColor = scrimColor,
        tonalElevation = MnsTheme.elevation.level0,
        dragHandle = if (showHandle) {
            { MnsSheetHandle() }
        } else {
            null
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = spacing.sheetPadding)
                .padding(bottom = spacing.xl),
            verticalArrangement = Arrangement.spacedBy(spacing.base),
        ) {
            if (title != null) {
                MnsSheetHeader(
                    title = title,
                    subtitle = subtitle,
                    onClose = if (showCloseButton) onDismissRequest else null,
                )
            }
            content()
        }
    }
}

/** Alça de arraste do bottom sheet. */
@Composable
public fun MnsSheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MnsTheme.spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = MnsTheme.sizing.sheetHandleWidth,
                    height = MnsTheme.sizing.sheetHandleHeight,
                )
                .background(MnsTheme.colors.outline, MnsTheme.shapes.full),
        )
    }
}

/** Cabeçalho padrão de sheets e dialogs: título, subtítulo e fechar. */
@Composable
public fun MnsSheetHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClose: (() -> Unit)? = null,
) {
    val colors = MnsTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xxs),
        ) {
            MnsText(
                text = title,
                style = MnsTheme.typography.headlineSmall,
                color = colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle != null) {
                MnsText(
                    text = subtitle,
                    style = MnsTheme.typography.bodySmall,
                    color = colors.textSecondary,
                )
            }
        }
        if (onClose != null) {
            MnsIconButton(
                icon = Icons.Filled.Close,
                contentDescription = "Fechar",
                onClick = onClose,
                variant = MnsButtonVariant.SECONDARY,
                size = MnsTheme.sizing.iconXl,
                iconSize = MnsTheme.sizing.iconSm,
            )
        }
    }
}
