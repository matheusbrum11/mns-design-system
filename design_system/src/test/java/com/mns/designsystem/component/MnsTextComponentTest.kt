package com.mns.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.text.MnsCompactNumberText
import com.mns.designsystem.component.text.MnsCurrencyText
import com.mns.designsystem.component.text.MnsHeading
import com.mns.designsystem.component.text.MnsHeadingLevel
import com.mns.designsystem.component.text.MnsPercentText
import com.mns.designsystem.component.text.MnsSectionHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.format.MnsCurrencyFormat
import com.mns.designsystem.format.MnsPercentFormat
import com.mns.designsystem.theme.MnsTheme
import org.junit.Test

/** Testes de integração dos componentes de texto. */
class MnsTextComponentTest : MnsComposeTest() {

    @Test
    fun `texto simples e anotado sao renderizados`() {
        setThemedContent {
            Column {
                MnsText(text = "Texto simples")
                MnsText(text = AnnotatedString("Texto anotado"))
            }
        }
        composeRule.onNodeWithText("Texto simples").assertShown()
        composeRule.onNodeWithText("Texto anotado").assertShown()
    }

    @Test
    fun `texto aceita cor explicita e truncamento`() {
        setThemedContent {
            MnsText(
                text = "Texto longo que sera truncado em uma linha",
                color = Color.Red,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                minLines = 1,
                softWrap = false,
            )
        }
        composeRule.onNodeWithText("Texto longo que sera truncado em uma linha").assertShown()
    }

    @Test
    fun `heading renderiza overline titulo e subtitulo em todos os niveis`() {
        setThemedContent {
            Column {
                MnsHeadingLevel.entries.forEach { nivel ->
                    MnsHeading(
                        text = "Titulo ${nivel.name}",
                        level = nivel,
                        overline = "over-${nivel.name}",
                        subtitle = "sub-${nivel.name}",
                    )
                }
            }
        }
        MnsHeadingLevel.entries.forEach { nivel ->
            composeRule.onNodeWithText("Titulo ${nivel.name}").assertShown()
            composeRule.onNodeWithText("OVER-${nivel.name}").assertShown()
            composeRule.onNodeWithText("sub-${nivel.name}").assertShown()
        }
    }

    @Test
    fun `cabecalho de secao exibe acao opcional`() {
        setThemedContent {
            Column {
                MnsSectionHeader(title = "Sem acao")
                MnsSectionHeader(
                    title = "Com acao",
                    subtitle = "12 eventos",
                    action = { MnsButton("See All", {}, variant = MnsButtonVariant.TEXT) },
                )
            }
        }
        composeRule.onNodeWithText("Sem acao").assertShown()
        composeRule.onNodeWithText("12 eventos").assertShown()
        composeRule.onNodeWithText("See All").assertShown()
    }

    @Test
    fun `texto monetario expoe o valor formatado na semantica`() {
        setThemedContent {
            MnsCurrencyText(cents = 12550, style = MnsTheme.typography.headlineMedium)
        }
        composeRule.onNodeWithContentDescription("R$ 1.234,56").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("R$ 125,50").assertShown()
    }

    @Test
    fun `texto monetario com simbolo enfatizado separa simbolo e numero`() {
        setThemedContent {
            MnsCurrencyText(
                cents = 12550,
                emphasizeSymbol = true,
                format = MnsCurrencyFormat.USD,
            )
        }
        composeRule.onNodeWithContentDescription("$125.50").assertShown()
    }

    @Test
    fun `texto monetario colore conforme o sinal`() {
        setThemedContent {
            Column {
                MnsCurrencyText(cents = -500, colorizeSign = true)
                MnsCurrencyText(cents = 500, colorizeSign = true)
                MnsCurrencyText(cents = 0, colorizeSign = true)
            }
        }
        composeRule.onNodeWithContentDescription("R$ 5,00").assertShown()
    }

    @Test
    fun `texto percentual usa os presets de formato`() {
        setThemedContent {
            Column {
                MnsPercentText(value = 0.184, format = MnsPercentFormat.Signed, colorizeSign = true)
                MnsPercentText(value = -0.05, format = MnsPercentFormat.Whole, colorizeSign = true)
                MnsPercentText(value = 0.0, format = MnsPercentFormat.Default)
            }
        }
        composeRule.onNodeWithContentDescription("+18,4%").assertShown()
        composeRule.onNodeWithContentDescription("-5%").assertShown()
    }

    @Test
    fun `numero compacto guarda o valor cheio na acessibilidade`() {
        setThemedContent {
            MnsCompactNumberText(value = 12400)
        }
        composeRule.onNodeWithContentDescription("12400").assertShown()
    }
}
