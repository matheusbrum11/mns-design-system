package com.mns.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Separador horizontal.
 *
 * @param thickness espessura; default `borders.hairline`.
 * @param color cor; default `colors.outlineVariant`.
 * @param inset recuo lateral. Em listas com avatar, alinhe o recuo ao início do
 *   texto (não à borda da tela) para que a linha "amarre" a coluna de conteúdo.
 */
@Composable
public fun MnsDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = MnsTheme.borders.hairline,
    color: Color = MnsTheme.colors.outlineVariant,
    inset: Dp = MnsTheme.spacing.none,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = inset)
            .height(thickness)
            .background(color),
    )
}

/** Separador vertical — para dividir colunas dentro de uma `Row`. */
@Composable
public fun MnsVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = MnsTheme.borders.hairline,
    color: Color = MnsTheme.colors.outlineVariant,
    inset: Dp = MnsTheme.spacing.none,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = inset)
            .width(thickness)
            .background(color),
    )
}

/**
 * Separador com rótulo centralizado — o clássico "ou".
 *
 * @param text rótulo exibido no meio da linha.
 */
@Composable
public fun MnsLabeledDivider(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MnsTheme.colors.outlineVariant,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MnsDivider(modifier = Modifier.weight(1f), color = color)
        MnsText(
            text = text,
            modifier = Modifier.padding(horizontal = MnsTheme.spacing.md),
            style = MnsTheme.typography.labelSmall,
            color = MnsTheme.colors.textTertiary,
        )
        MnsDivider(modifier = Modifier.weight(1f), color = color)
    }
}
