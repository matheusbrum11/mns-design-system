package com.mns.designsystem.component.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.media.MnsIcons
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme

/**
 * Seletor numérico de incremento/decremento — quantidade de ingressos,
 * passageiros, itens no carrinho.
 *
 * Prefira o stepper a um campo numérico quando o intervalo é pequeno (até ~10):
 * dois toques batem digitar em teclado numérico, e não há estado inválido.
 *
 * @param value quantidade atual.
 * @param onValueChange chamado com o novo valor, já respeitando [range] e [step].
 * @param range faixa permitida. Os botões desabilitam nos extremos.
 * @param step incremento por toque.
 * @param label rótulo à esquerda do controle.
 * @param formatValue formatação do número exibido (ex.: "2 pessoas").
 */
@Composable
public fun MnsStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..99,
    step: Int = 1,
    enabled: Boolean = true,
    label: String? = null,
    formatValue: (Int) -> String = { it.toString() },
) {
    val canDecrease = enabled && value - step >= range.first
    val canIncrease = enabled && value + step <= range.last

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
    ) {
        if (label != null) {
            MnsText(
                text = label,
                modifier = Modifier.weight(1f),
                style = MnsTheme.typography.bodyMedium,
                color = if (enabled) MnsTheme.colors.textPrimary else MnsTheme.colors.textDisabled,
            )
        }
        MnsIconButton(
            icon = MnsIcons.Minus,
            contentDescription = "Diminuir",
            onClick = { onValueChange((value - step).coerceIn(range)) },
            variant = MnsButtonVariant.OUTLINED,
            enabled = canDecrease,
            size = 36.dp,
            iconSize = MnsTheme.sizing.iconSm,
        )
        MnsText(
            text = formatValue(value),
            modifier = Modifier
                .widthIn(min = 40.dp)
                .clearAndSetSemantics { contentDescription = formatValue(value) },
            style = MnsTheme.typography.titleMedium,
            color = if (enabled) MnsTheme.colors.textPrimary else MnsTheme.colors.textDisabled,
            textAlign = TextAlign.Center,
        )
        MnsIconButton(
            icon = Icons.Filled.Add,
            contentDescription = "Aumentar",
            onClick = { onValueChange((value + step).coerceIn(range)) },
            variant = MnsButtonVariant.OUTLINED,
            enabled = canIncrease,
            size = 36.dp,
            iconSize = MnsTheme.sizing.iconSm,
        )
    }
}
