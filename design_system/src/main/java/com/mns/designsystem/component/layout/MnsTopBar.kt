package com.mns.designsystem.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/** Alinhamento do título na [MnsTopBar]. */
public enum class MnsTopBarAlignment {
    /** Título à esquerda, após a navegação. Padrão Android. */
    START,

    /** Título centralizado. Padrão dos designs de referência de ticketing. */
    CENTER,
}

/**
 * Barra superior.
 *
 * @param title título da tela. Trunca em uma linha.
 * @param subtitle segunda linha, menor — útil para contexto (nome do evento).
 * @param onNavigateBack quando não-nulo, exibe o botão de voltar.
 * @param navigationIcon slot que substitui completamente o botão de voltar.
 * @param actions ações à direita. Máximo recomendado: 2 ícones + overflow.
 * @param alignment posição do título — ver [MnsTopBarAlignment].
 * @param containerColor cor de fundo. `Color.Transparent` para barra sobreposta
 *   a uma imagem de capa.
 * @param applyStatusBarPadding reserva o espaço da status bar. Desligue quando
 *   a barra já estiver dentro de um container que aplicou o inset.
 */
@Composable
public fun MnsTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    alignment: MnsTopBarAlignment = MnsTopBarAlignment.START,
    containerColor: Color = MnsTheme.colors.background,
    contentColor: Color = MnsTheme.colors.onBackground,
    elevation: Dp = MnsTheme.elevation.level0,
    applyStatusBarPadding: Boolean = true,
) {
    val spacing = MnsTheme.spacing

    MnsSurface(
        modifier = modifier.fillMaxWidth(),
        shape = MnsTheme.shapes.none,
        color = containerColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .let { if (applyStatusBarPadding) it.statusBarsPadding() else it },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MnsTheme.sizing.topBarHeight)
                    .padding(horizontal = spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            ) {
                when {
                    navigationIcon != null -> navigationIcon()
                    onNavigateBack != null -> MnsIconButton(
                        icon = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        onClick = onNavigateBack,
                        variant = MnsButtonVariant.TEXT,
                    )
                    alignment == MnsTopBarAlignment.CENTER ->
                        Box(modifier = Modifier.size(MnsTheme.sizing.touchTarget))
                    else -> Box(modifier = Modifier.padding(start = spacing.sm))
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = if (alignment == MnsTopBarAlignment.CENTER) {
                        Alignment.CenterHorizontally
                    } else {
                        Alignment.Start
                    },
                ) {
                    if (title != null) {
                        MnsText(
                            text = title,
                            style = MnsTheme.typography.titleLarge,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = if (alignment == MnsTopBarAlignment.CENTER) {
                                TextAlign.Center
                            } else {
                                TextAlign.Start
                            },
                        )
                    }
                    if (subtitle != null) {
                        MnsText(
                            text = subtitle,
                            style = MnsTheme.typography.caption,
                            color = MnsTheme.colors.textTertiary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                if (actions != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.xxs),
                        content = actions,
                    )
                } else if (alignment == MnsTopBarAlignment.CENTER) {
                    Box(modifier = Modifier.size(MnsTheme.sizing.touchTarget))
                }
            }
        }
    }
}
