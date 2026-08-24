package com.mns.designsystem.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/**
 * Dialog modal tokenizado com slot livre de conteúdo.
 *
 * @param onDismissRequest chamado ao tocar fora ou apertar "voltar".
 * @param dismissOnClickOutside permite fechar tocando no scrim. Desligue em
 *   decisões destrutivas — fechar sem querer não pode ser possível ali.
 * @param content conteúdo do dialog.
 */
@Composable
public fun MnsDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    containerColor: Color = MnsTheme.colors.surfaceElevated,
    shape: Shape = MnsTheme.shapes.dialog,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        ),
    ) {
        MnsSurface(
            modifier = modifier
                .padding(MnsTheme.spacing.xl)
                .widthIn(max = 420.dp),
            shape = shape,
            color = containerColor,
            contentColor = MnsTheme.colors.onSurface,
            elevation = MnsTheme.elevation.level5,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MnsTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.base),
                content = content,
            )
        }
    }
}

/**
 * Dialog de confirmação pronto: ícone, título, mensagem e duas ações.
 *
 * A ação destrutiva **nunca** vira a ação primária visual quando [status] é
 * `DANGER` sem confirmação explícita — o botão de confirmar usa a variante
 * `DANGER`, e o de cancelar fica com o peso visual maior. É uma barreira barata
 * contra exclusão acidental.
 *
 * @param title pergunta ou afirmação curta.
 * @param message detalhamento e consequência da ação.
 * @param confirmText rótulo do botão de confirmação.
 * @param dismissText rótulo do botão de cancelamento; `null` esconde o botão.
 * @param status intenção; `DANGER` troca o botão de confirmação para destrutivo.
 * @param icon ícone acima do título.
 */
@Composable
public fun MnsConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = "Cancelar",
    status: MnsStatus = MnsStatus.NEUTRAL,
    icon: ImageVector? = null,
    loading: Boolean = false,
) {
    val colors = MnsTheme.colors
    val (iconContainer, iconContent) = colors.containerFor(status)

    MnsDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        dismissOnClickOutside = status != MnsStatus.DANGER,
    ) {
        if (icon != null) {
            MnsSurface(
                modifier = Modifier.size(MnsTheme.sizing.avatarLg),
                shape = MnsTheme.shapes.full,
                color = iconContainer,
                contentColor = iconContent,
            ) {
                Box(
                    modifier = Modifier.size(MnsTheme.sizing.avatarLg),
                    contentAlignment = Alignment.Center,
                ) {
                    MnsIcon(
                        imageVector = icon,
                        contentDescription = null,
                        size = MnsTheme.sizing.iconLg,
                        tint = iconContent,
                    )
                }
            }
        }
        MnsText(
            text = title,
            style = MnsTheme.typography.headlineSmall,
            color = colors.textPrimary,
        )
        MnsText(
            text = message,
            style = MnsTheme.typography.bodyMedium,
            color = colors.textSecondary,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
        ) {
            if (dismissText != null) {
                Box(modifier = Modifier.weight(1f)) {
                    MnsButton(
                        text = dismissText,
                        onClick = onDismissRequest,
                        variant = MnsButtonVariant.OUTLINED,
                        fillMaxWidth = true,
                        enabled = !loading,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                MnsButton(
                    text = confirmText,
                    onClick = onConfirm,
                    variant = if (status == MnsStatus.DANGER) {
                        MnsButtonVariant.DANGER
                    } else {
                        MnsButtonVariant.PRIMARY
                    },
                    fillMaxWidth = true,
                    loading = loading,
                )
            }
        }
    }
}

/** Alinhamento de texto reutilizado por dialogs centralizados. */
internal val DialogTextAlign: TextAlign = TextAlign.Center
