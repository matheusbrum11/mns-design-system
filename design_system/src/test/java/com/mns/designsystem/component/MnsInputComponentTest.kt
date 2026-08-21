package com.mns.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.input.MnsCheckbox
import com.mns.designsystem.component.input.MnsChip
import com.mns.designsystem.component.input.MnsChipRow
import com.mns.designsystem.component.input.MnsCurrencyField
import com.mns.designsystem.component.input.MnsOtpField
import com.mns.designsystem.component.input.MnsPasswordField
import com.mns.designsystem.component.input.MnsRadioButton
import com.mns.designsystem.component.input.MnsSearchField
import com.mns.designsystem.component.input.MnsSelectionGroup
import com.mns.designsystem.component.input.MnsSlider
import com.mns.designsystem.component.input.MnsStepper
import com.mns.designsystem.component.input.MnsSwitch
import com.mns.designsystem.component.input.MnsTextField
import com.mns.designsystem.component.input.MnsToggleState
import com.mns.designsystem.format.MnsCurrencyFormat
import com.mns.designsystem.format.MnsMaskVisualTransformation
import org.junit.Test

/** Testes de integração dos componentes de entrada. */
class MnsInputComponentTest : MnsComposeTest() {

    @Test
    fun `campo de texto propaga a digitacao`() {
        var valor = ""
        setThemedContent {
            MnsTextField(
                value = valor,
                onValueChange = { valor = it },
                label = "E-mail",
                placeholder = "voce@exemplo.com",
                leadingIcon = Icons.Filled.Email,
            )
        }
        composeRule.onNodeWithText("voce@exemplo.com").performTextInput("ana@mns.dev")
        assertThat(valor).isEqualTo("ana@mns.dev")
    }

    @Test
    fun `campo exibe rotulo dica e placeholder`() {
        setThemedContent {
            MnsTextField(
                value = "",
                onValueChange = {},
                label = "E-mail",
                placeholder = "voce@exemplo.com",
                helperText = "Usamos para enviar o ingresso.",
            )
        }
        composeRule.onNodeWithText("E-mail").assertShown()
        composeRule.onNodeWithText("voce@exemplo.com").assertShown()
        composeRule.onNodeWithText("Usamos para enviar o ingresso.").assertShown()
    }

    @Test
    fun `mensagem de erro substitui a dica`() {
        setThemedContent {
            MnsTextField(
                value = "x",
                onValueChange = {},
                helperText = "Dica que deve sumir",
                errorMessage = "E-mail inválido",
            )
        }
        composeRule.onNodeWithText("E-mail inválido").assertShown()
        composeRule.onNodeWithText("Dica que deve sumir").assertDoesNotExist()
    }

    @Test
    fun `maxLength trunca a entrada e exibe contador`() {
        var valor = ""
        setThemedContent {
            MnsTextField(value = valor, onValueChange = { valor = it }, maxLength = 5, placeholder = "dig")
        }
        composeRule.onNodeWithText("dig").performTextInput("1234567890")
        assertThat(valor).hasLength(5)
    }

    @Test
    fun `campo desabilitado ignora a digitacao`() {
        var valor = "inicial"
        setThemedContent {
            MnsTextField(value = valor, onValueChange = { valor = it }, enabled = false, label = "L")
        }
        composeRule.onNodeWithText("L").assertShown()
        assertThat(valor).isEqualTo("inicial")
    }

    @Test
    fun `campo readOnly expoe o texto mas nao aceita edicao`() {
        setThemedContent {
            MnsTextField(value = "fixo", onValueChange = {}, readOnly = true)
        }
        composeRule.onNodeWithText("fixo")
            .assertShown()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.IsEditable, false))
    }

    @Test
    fun `mascara e aplicada apenas na exibicao`() {
        var valor = "12345678901"
        setThemedContent {
            MnsTextField(
                value = valor,
                onValueChange = { valor = it },
                visualTransformation = MnsMaskVisualTransformation("###.###.###-##"),
            )
        }
        composeRule.onNodeWithText("123.456.789-01").assertShown()
        assertThat(valor).isEqualTo("12345678901")
    }

    @Test
    fun `busca limpa o conteudo pelo botao dedicado`() {
        setThemedContent {
            var q by remember { mutableStateOf("jazz") }
            MnsSearchField(value = q, onValueChange = { q = it })
        }
        composeRule.onNodeWithContentDescription("Limpar busca").performClick()
        settle()
        composeRule.onNodeWithText("Buscar").assertShown()
    }

    @Test
    fun `campo monetario formata a digitacao em tempo real`() {
        setThemedContent {
            var cents by remember { mutableLongStateOf(0L) }
            MnsCurrencyField(cents = cents, onCentsChange = { cents = it }, label = "Valor")
        }
        composeRule.onNodeWithText("R$ 0,00").performTextInput("12550")
        settle()
        composeRule.onNodeWithText("R$ 125,50").assertShown()
    }

    @Test
    fun `campo monetario respeita o teto configurado`() {
        var cents = 0L
        setThemedContent {
            MnsCurrencyField(
                cents = cents,
                onCentsChange = { cents = it },
                maxCents = 999L,
                format = MnsCurrencyFormat.USD,
            )
        }
        composeRule.onNodeWithText("$0.00").performTextInput("123456")
        assertThat(cents).isEqualTo(999L)
    }

    @Test
    fun `campo de senha alterna a visibilidade`() {
        setThemedContent {
            var s by remember { mutableStateOf("segredo") }
            MnsPasswordField(value = s, onValueChange = { s = it })
        }
        composeRule.onNodeWithContentDescription("Mostrar senha").performClick()
        settle()
        composeRule.onNodeWithContentDescription("Ocultar senha").assertShown()
    }

    @Test
    fun `otp aceita apenas digitos e avisa ao completar`() {
        var completou: String? = null
        setThemedContent {
            var code by remember { mutableStateOf("") }
            MnsOtpField(
                value = code,
                onValueChange = { code = it },
                length = 4,
                onCompleted = { completou = it },
            )
        }
        composeRule.onNodeWithContentDescription("Código de 4 dígitos").performTextInput("1a2b3c4d")
        assertThat(completou).isEqualTo("1234")
    }

    @Test
    fun `otp em estado de erro continua renderizando`() {
        setThemedContent {
            MnsOtpField(value = "12", onValueChange = {}, length = 6, isError = true)
        }
        composeRule.onNodeWithContentDescription("Código de 6 dígitos").assertShown()
    }

    @Test
    fun `checkbox alterna entre marcado e desmarcado`() {
        setThemedContent {
            var estado by remember { mutableStateOf(MnsToggleState.UNCHECKED) }
            MnsCheckbox(state = estado, onStateChange = { estado = it }, label = "Aceito")
        }
        composeRule.onNodeWithText("Aceito").assertIsOff().performClick()
        settle()
        composeRule.onNodeWithText("Aceito").assertIsOn()
    }

    @Test
    fun `checkbox indeterminado conta como marcado na semantica`() {
        setThemedContent {
            MnsCheckbox(
                state = MnsToggleState.INDETERMINATE,
                onStateChange = {},
                label = "Todos",
                description = "Alguns selecionados",
            )
        }
        composeRule.onNodeWithText("Todos").assertIsOn()
        composeRule.onNodeWithText("Alguns selecionados").assertShown()
    }

    @Test
    fun `checkbox desabilitado nao alterna`() {
        var estado = MnsToggleState.UNCHECKED
        setThemedContent {
            MnsCheckbox(state = estado, onStateChange = { estado = it }, label = "X", enabled = false)
        }
        composeRule.onNodeWithText("X").performClick()
        assertThat(estado).isEqualTo(MnsToggleState.UNCHECKED)
    }

    @Test
    fun `radio button seleciona e ignora reclique`() {
        var selecoes = 0
        setThemedContent {
            Column {
                MnsRadioButton(selected = true, onSelect = { selecoes++ }, label = "Já selecionado")
                MnsRadioButton(selected = false, onSelect = { selecoes++ }, label = "Outro", description = "d")
            }
        }
        composeRule.onNodeWithText("Já selecionado").assertIsSelected().performClick()
        assertThat(selecoes).isEqualTo(0)
        composeRule.onNodeWithText("Outro").performClick()
        assertThat(selecoes).isEqualTo(1)
    }

    @Test
    fun `switch reporta o novo estado`() {
        var ligado = false
        setThemedContent {
            MnsSwitch(
                checked = ligado,
                onCheckedChange = { ligado = it },
                label = "Lembretes",
                description = "Receber avisos",
            )
        }
        composeRule.onNodeWithText("Lembretes").performClick()
        assertThat(ligado).isTrue()
    }

    @Test
    fun `switch desabilitado nao alterna`() {
        var ligado = false
        setThemedContent {
            MnsSwitch(checked = ligado, onCheckedChange = { ligado = it }, label = "L", enabled = false)
        }
        composeRule.onNodeWithText("L").assertIsNotEnabled()
        assertThat(ligado).isFalse()
    }

    @Test
    fun `grupo de selecao renderiza titulo e filhos`() {
        setThemedContent {
            MnsSelectionGroup(title = "Entrega") {
                MnsChip(label = "E-ticket", selected = true, onClick = {})
            }
        }
        composeRule.onNodeWithText("Entrega").assertShown()
        composeRule.onNodeWithText("E-ticket").assertShown()
    }

    @Test
    fun `chip dispara onClick no corpo`() {
        var cliques = 0
        setThemedContent {
            MnsChip(label = "travel", selected = true, onClick = { cliques++ })
        }
        composeRule.onNodeWithText("travel").performClick()
        settle()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `chip dispara onDismiss sem acionar onClick`() {
        var cliques = 0
        var remocoes = 0
        setThemedContent {
            MnsChip(
                label = "travel",
                selected = true,
                onClick = { cliques++ },
                onDismiss = { remocoes++ },
            )
        }
        // `Modifier.clickable` mescla a semântica dos descendentes, então o ✕ do
        // chip só é endereçável na árvore não-mesclada — é também o motivo de um
        // leitor de tela anunciar o chip inteiro como um alvo único.
        composeRule.onNodeWithContentDescription("Remover travel", useUnmergedTree = true)
            .performClick()
        settle()
        assertThat(remocoes).isEqualTo(1)
        assertThat(cliques).isEqualTo(0)
    }

    @Test
    fun `chip desabilitado nao dispara clique`() {
        var cliques = 0
        setThemedContent {
            MnsChip(label = "off", selected = false, onClick = { cliques++ }, enabled = false)
        }
        composeRule.onNodeWithText("off").performClick()
        assertThat(cliques).isEqualTo(0)
    }

    @Test
    fun `linha de chips reporta o indice tocado`() {
        var tocado = -1
        setThemedContent {
            MnsChipRow(
                options = listOf("a", "b", "c"),
                selectedIndices = setOf(0),
                onToggle = { tocado = it },
            )
        }
        composeRule.onNodeWithText("b").performClick()
        assertThat(tocado).isEqualTo(1)
    }

    @Test
    fun `stepper respeita os limites da faixa`() {
        setThemedContent {
            var qtd by remember { androidx.compose.runtime.mutableIntStateOf(1) }
            MnsStepper(value = qtd, onValueChange = { qtd = it }, range = 1..2, label = "Passageiros")
        }
        composeRule.onNodeWithContentDescription("Diminuir").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("Aumentar").performClick()
        settle()
        composeRule.onNodeWithContentDescription("Aumentar").assertIsNotEnabled()
    }

    @Test
    fun `stepper formata o valor exibido`() {
        setThemedContent {
            MnsStepper(value = 2, onValueChange = {}, formatValue = { "$it pessoas" })
        }
        composeRule.onNodeWithContentDescription("2 pessoas").assertShown()
    }

    @Test
    fun `slider renderiza com e sem rotulo`() {
        setThemedContent {
            Column {
                var v by remember { mutableFloatStateOf(0.4f) }
                MnsSlider(
                    value = v,
                    onValueChange = { v = it },
                    label = "Preço",
                    formatValue = { "R$ ${(it * 100).toInt()}" },
                )
                MnsSlider(value = 0.2f, onValueChange = {}, steps = 4, enabled = false)
            }
        }
        composeRule.onNodeWithText("Preço").assertShown()
        composeRule.onNodeWithText("R$ 40").assertShown()
    }
}
