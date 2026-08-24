package com.mns.designsystem.component.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.MnsTheme

/**
 * Campo de texto do design system.
 *
 * Diferente do `OutlinedTextField` do Material, o rótulo aqui fica **acima** do
 * campo e não flutua. É uma escolha deliberada: rótulo flutuante economiza
 * altura mas some quando o campo está preenchido, o que prejudica revisão de
 * formulário longo — exatamente o caso de checkout.
 *
 * ```kotlin
 * MnsTextField(
 *     value = email,
 *     onValueChange = { email = it },
 *     label = "E-mail",
 *     placeholder = "voce@exemplo.com",
 *     errorMessage = erro,
 * )
 * ```
 *
 * @param value texto atual do campo.
 * @param onValueChange chamado a cada alteração. Não é chamado quando
 *   [readOnly] ou [enabled] impedem a edição.
 * @param label rótulo acima do campo.
 * @param placeholder texto exibido quando [value] está vazio.
 * @param helperText linha de apoio abaixo do campo. É suprimida quando há
 *   [errorMessage] — mostrar dica e erro juntos compete por atenção.
 * @param errorMessage mensagem de erro. Quando não-nula, o campo entra em
 *   estado de erro (borda `danger`) e a mensagem é anunciada por leitores de tela.
 * @param leadingIcon ícone no início do campo.
 * @param trailingIcon slot livre no fim do campo (botão de limpar, olho de senha).
 * @param singleLine impede quebra de linha e troca Enter por "próximo".
 * @param maxLines limite de linhas quando [singleLine] é `false`.
 * @param maxLength limite de caracteres. Quando definido, exibe contador.
 * @param visualTransformation máscara visual — ver `MnsMaskVisualTransformation`.
 * @param keyboardOptions tipo de teclado e ação de IME.
 * @param readOnly permite seleção e cópia, bloqueia edição.
 */
@Composable
public fun MnsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 4,
    maxLength: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = MnsTheme.shapes.input,
    textStyle: TextStyle = MnsTheme.typography.bodyLarge,
    interactionSource: MutableInteractionSource = rememberMnsInteractionSource(),
) {
    val colors = MnsTheme.colors
    val spacing = MnsTheme.spacing
    val borders = MnsTheme.borders
    val motion = MnsTheme.motion

    val focused by interactionSource.collectIsFocusedAsState()
    val hasError = errorMessage != null

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.outlineVariant
            hasError -> colors.danger
            focused -> colors.primary
            else -> colors.outline
        },
        animationSpec = motion.tween(motion.durationFast),
        label = "mnsFieldBorder",
    )
    val borderWidth = when {
        hasError -> borders.thick
        focused -> borders.medium
        else -> borders.thin
    }
    val containerColor = if (enabled) colors.surfaceVariant else colors.surfaceVariant.copy(alpha = 0.5f)
    val contentColor = if (enabled) colors.textPrimary else colors.textDisabled

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        if (label != null) {
            MnsText(
                text = label,
                style = MnsTheme.typography.labelMedium,
                color = if (enabled) colors.textSecondary else colors.textDisabled,
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { new ->
                if (!enabled || readOnly) return@BasicTextField
                onValueChange(if (maxLength != null) new.take(maxLength) else new)
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = MnsTheme.sizing.inputHeight)
                .background(containerColor, shape)
                .border(borderWidth, borderColor, shape)
                .semantics { if (errorMessage != null) error(errorMessage) },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(color = contentColor),
            cursorBrush = SolidColor(if (hasError) colors.danger else colors.primary),
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.base, vertical = spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    if (leadingIcon != null) {
                        MnsIcon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = if (enabled) colors.textTertiary else colors.textDisabled,
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholder != null) {
                            MnsText(
                                text = placeholder,
                                style = textStyle,
                                color = colors.textTertiary,
                                maxLines = 1,
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) trailingIcon()
                }
            },
        )

        MnsFieldFooter(
            helperText = helperText,
            errorMessage = errorMessage,
            counter = maxLength?.let { "${value.length}/$it" },
            enabled = enabled,
        )
    }
}

/**
 * Linha de apoio de um campo: mensagem (dica ou erro) à esquerda, contador à
 * direita. Extraído para que todos os campos derivados (`MnsCurrencyField`,
 * `MnsSearchField`, …) tenham exatamente o mesmo rodapé.
 */
@Composable
internal fun MnsFieldFooter(
    helperText: String?,
    errorMessage: String?,
    counter: String?,
    enabled: Boolean,
) {
    val colors = MnsTheme.colors
    val message = errorMessage ?: helperText
    if (message == null && counter == null) return

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = MnsTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
        verticalAlignment = Alignment.Top,
    ) {
        if (message != null) {
            MnsText(
                text = message,
                modifier = Modifier.weight(1f),
                style = MnsTheme.typography.caption,
                color = when {
                    errorMessage != null -> colors.danger
                    !enabled -> colors.textDisabled
                    else -> colors.textTertiary
                },
            )
        } else {
            Box(modifier = Modifier.weight(1f))
        }
        if (counter != null) {
            MnsText(
                text = counter,
                style = MnsTheme.typography.caption,
                color = if (enabled) colors.textTertiary else colors.textDisabled,
            )
        }
    }
}

/** Cor transparente reutilizada internamente. */
internal val TransparentColor: Color = Color.Transparent
