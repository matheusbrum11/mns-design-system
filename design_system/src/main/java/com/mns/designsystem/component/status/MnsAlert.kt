package com.mns.designsystem.component.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.token.MnsStatus

/**
 * Bloco de mensagem contextual (banner de aviso, confirmação, erro de
 * formulário).
 *
 * É marcado como `liveRegion`, ou seja: quando aparece, leitores de tela
 * anunciam o conteúdo sem que o usuário precise navegar até ele. É a diferença
 * entre um erro percebido e um erro invisível.
 *
 * @param message texto principal.
 * @param title título opcional em negrito acima da mensagem.
 * @param status intenção semântica; define cor e ícone padrão.
 * @param icon sobrescreve o ícone derivado de [status].
 * @param onDismiss quando não-nulo, exibe o botão de fechar.
 * @param action slot para uma ação (ex.: "Tentar de novo").
 */
@Composable
public fun MnsAlert(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    status: MnsStatus = MnsStatus.INFO,
    icon: ImageVector? = null,
    onDismiss: (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val (container, content) = colors.containerFor(status)
    val resolvedIcon = icon ?: when (status) {
        MnsStatus.SUCCESS -> Icons.Filled.CheckCircle
        MnsStatus.WARNING, MnsStatus.DANGER -> Icons.Filled.Warning
        else -> Icons.Filled.Info
    }

    MnsSurface(
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        shape = MnsTheme.shapes.medium,
        color = container,
        contentColor = content,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(spacing.md),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            verticalAlignment = Alignment.Top,
        ) {
            MnsIcon(
                imageVector = resolvedIcon,
                contentDescription = null,
                size = MnsTheme.sizing.iconMd,
                tint = content,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
            ) {
                if (title != null) {
                    MnsText(text = title, style = MnsTheme.typography.titleSmall, color = content)
                }
                MnsText(text = message, style = MnsTheme.typography.bodySmall, color = content)
                if (action != null) action()
            }
            if (onDismiss != null) {
                MnsIconButton(
                    icon = Icons.Filled.Close,
                    contentDescription = "Fechar aviso",
                    onClick = onDismiss,
                    variant = MnsButtonVariant.TEXT,
                    size = MnsTheme.sizing.iconLg,
                    iconSize = MnsTheme.sizing.iconSm,
                )
            }
        }
    }
}
