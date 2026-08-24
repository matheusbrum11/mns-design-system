package com.mns.designsystem.component.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.media.MnsIcons
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.format.MnsCurrencyFormat
import com.mns.designsystem.format.MnsCurrencyFormatter
import com.mns.designsystem.format.MnsCurrencyVisualTransformation
import com.mns.designsystem.foundation.rememberMnsInteractionSource
import com.mns.designsystem.theme.MnsTheme

/**
 * Campo de busca — ícone de lupa, forma em pílula e botão de limpar que só
 * aparece quando há texto.
 *
 * @param value texto atual.
 * @param onValueChange chamado a cada alteração.
 * @param onSearch chamado quando o usuário aciona a busca no teclado.
 * @param placeholder texto de dica.
 * @param showClearButton exibe o botão de limpar quando há conteúdo.
 */
@Composable
public fun MnsSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar",
    enabled: Boolean = true,
    showClearButton: Boolean = true,
    onSearch: (String) -> Unit = {},
    shape: Shape = MnsTheme.shapes.full,
    interactionSource: MutableInteractionSource = rememberMnsInteractionSource(),
) {
    MnsTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        leadingIcon = Icons.Filled.Search,
        trailingIcon = {
            AnimatedVisibility(visible = showClearButton && value.isNotEmpty()) {
                MnsIconButton(
                    icon = Icons.Filled.Clear,
                    contentDescription = "Limpar busca",
                    onClick = { onValueChange("") },
                    variant = MnsButtonVariant.TEXT,
                    size = MnsTheme.sizing.iconLg,
                    iconSize = MnsTheme.sizing.iconSm,
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(value) }),
        shape = shape,
        interactionSource = interactionSource,
    )
}

/**
 * Campo monetário com formatação em tempo real.
 *
 * O estado externo é **sempre em centavos** (`Long`). O usuário digita apenas
 * dígitos e o campo cuida da máscara; nenhum parse de string formatada
 * acontece do lado do chamador.
 *
 * ```kotlin
 * var valor by remember { mutableLongStateOf(0L) }
 * MnsCurrencyField(cents = valor, onCentsChange = { valor = it }, label = "Valor")
 * ```
 *
 * @param cents valor atual em centavos.
 * @param onCentsChange recebe o novo valor em centavos.
 * @param format moeda e locale — ver [MnsCurrencyFormat].
 * @param maxCents teto do valor. Digitações acima disso são ignoradas.
 */
@Composable
public fun MnsCurrencyField(
    cents: Long,
    onCentsChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    helperText: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    format: MnsCurrencyFormat = MnsCurrencyFormat.BRL,
    maxCents: Long = 99_999_999_99L,
    leadingIcon: ImageVector? = null,
) {
    // O texto bruto guardado é a sequência de dígitos; a exibição é derivada.
    val raw = remember(cents) { if (cents == 0L) "" else cents.toString() }

    MnsTextField(
        value = raw,
        onValueChange = { input ->
            val parsed = MnsCurrencyFormatter.parseDigitsToCents(input)
            onCentsChange(parsed.coerceAtMost(maxCents))
        },
        modifier = modifier,
        label = label,
        placeholder = MnsCurrencyFormatter.formatCents(0, format),
        helperText = helperText,
        errorMessage = errorMessage,
        enabled = enabled,
        leadingIcon = leadingIcon,
        singleLine = true,
        visualTransformation = remember(format) { MnsCurrencyVisualTransformation(format) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done,
        ),
        textStyle = MnsTheme.typography.titleLarge,
    )
}

/**
 * Campo de senha com alternância de visibilidade.
 *
 * @param value senha atual.
 * @param onValueChange chamado a cada alteração.
 * @param initiallyVisible começa com a senha revelada. Deixe `false` — revelar
 *   por padrão expõe a senha a quem estiver olhando a tela.
 */
@Composable
public fun MnsPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = "Senha",
    placeholder: String? = null,
    helperText: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    initiallyVisible: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
) {
    var visible by remember { mutableStateOf(initiallyVisible) }
    MnsTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorMessage = errorMessage,
        enabled = enabled,
        trailingIcon = {
            MnsIconButton(
                icon = if (visible) MnsIcons.VisibilityOff else MnsIcons.Visibility,
                contentDescription = if (visible) "Ocultar senha" else "Mostrar senha",
                onClick = { visible = !visible },
                variant = MnsButtonVariant.TEXT,
                size = MnsTheme.sizing.iconLg,
                iconSize = MnsTheme.sizing.iconMd,
            )
        },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
    )
}

/**
 * Campo de código (OTP / PIN) com uma caixa por dígito.
 *
 * Mantém **um único** campo de texto invisível por baixo, em vez de N campos
 * com foco encadeado. Isso preserva colar código inteiro, autofill de SMS e
 * backspace atravessando caixas — três coisas que a implementação com N campos
 * quebra.
 *
 * @param value código digitado até agora.
 * @param onValueChange chamado com o novo código, já limitado a [length].
 * @param length número de dígitos.
 * @param onCompleted chamado uma vez quando o código atinge [length].
 * @param isError pinta as caixas com a cor de erro.
 */
@Composable
public fun MnsOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
    boxSize: Dp = 48.dp,
    onCompleted: (String) -> Unit = {},
) {
    val colors = MnsTheme.colors
    val borders = MnsTheme.borders
    val focusRequester = remember { FocusRequester() }
    val interactionSource = rememberMnsInteractionSource()

    Box(modifier = modifier) {
        // Campo real, transparente e do tamanho das caixas — recebe o teclado.
        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = { input ->
                if (!enabled) return@BasicTextField
                val digits = input.filter(Char::isDigit).take(length)
                onValueChange(digits)
                if (digits.length == length) onCompleted(digits)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(boxSize)
                .focusRequester(focusRequester)
                .semantics { contentDescription = "Código de $length dígitos" },
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            interactionSource = interactionSource,
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
                ) {
                    repeat(length) { index ->
                        val char = value.getOrNull(index)
                        val filled = char != null
                        val borderColor = when {
                            isError -> colors.danger
                            filled -> colors.primary
                            else -> colors.outline
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(boxSize)
                                .background(colors.surfaceVariant, MnsTheme.shapes.input)
                                .border(
                                    width = if (filled || isError) borders.thick else borders.thin,
                                    color = borderColor,
                                    shape = MnsTheme.shapes.input,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            MnsText(
                                text = char?.toString() ?: "",
                                style = MnsTheme.typography.headlineSmall,
                                color = colors.textPrimary,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            },
        )
    }
}
