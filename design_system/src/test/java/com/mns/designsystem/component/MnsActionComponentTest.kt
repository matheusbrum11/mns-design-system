package com.mns.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsFab
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.action.MnsSegment
import com.mns.designsystem.component.action.MnsSegmentedControl
import com.mns.designsystem.component.text.MnsText
import org.junit.Test

/** Testes de integração dos componentes de ação. */
class MnsActionComponentTest : MnsComposeTest() {

    @Test
    fun `botao dispara onClick quando habilitado`() {
        var cliques = 0
        setThemedContent {
            MnsButton(text = "Confirmar", onClick = { cliques++ })
        }
        composeRule.onNodeWithText("Confirmar").assertShown().performClick()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `botao desabilitado nao dispara onClick`() {
        var cliques = 0
        setThemedContent {
            MnsButton(text = "Confirmar", onClick = { cliques++ }, enabled = false)
        }
        composeRule.onNodeWithContentDescription("Confirmar").assertIsNotEnabled()
        composeRule.onNodeWithText("Confirmar").performClick()
        assertThat(cliques).isEqualTo(0)
    }

    @Test
    fun `botao em carregamento bloqueia o clique e mantem o rotulo`() {
        var cliques = 0
        setThemedContent {
            MnsButton(text = "Pagar", onClick = { cliques++ }, loading = true)
        }
        composeRule.onNodeWithText("Pagar").assertShown().performClick()
        assertThat(cliques).isEqualTo(0)
    }

    @Test
    fun `botao renderiza todas as variantes e tamanhos`() {
        setThemedContent {
            androidx.compose.foundation.layout.Column {
                MnsButtonVariant.entries.forEach { variant ->
                    MnsButtonSize.entries.forEach { size ->
                        MnsButton(
                            text = "${variant.name}-${size.name}",
                            onClick = {},
                            variant = variant,
                            size = size,
                            leadingIcon = Icons.Filled.Add,
                            trailingIcon = Icons.Filled.Share,
                            fillMaxWidth = size == MnsButtonSize.LARGE,
                        )
                    }
                }
            }
        }
        MnsButtonVariant.entries.forEach { variant ->
            MnsButtonSize.entries.forEach { size ->
                composeRule.onNodeWithText("${variant.name}-${size.name}").assertShown()
            }
        }
    }

    @Test
    fun `contentDescription customizado sobrescreve o rotulo na semantica`() {
        setThemedContent {
            MnsButton(text = "OK", onClick = {}, contentDescription = "Confirmar pagamento")
        }
        composeRule.onNodeWithContentDescription("Confirmar pagamento").assertHasClickAction()
    }

    @Test
    fun `icon button expoe contentDescription e responde ao toque`() {
        var cliques = 0
        setThemedContent {
            androidx.compose.foundation.layout.Column {
                MnsButtonVariant.entries.forEach { variant ->
                    MnsIconButton(
                        icon = Icons.Filled.Share,
                        contentDescription = "Compartilhar ${variant.name}",
                        onClick = { cliques++ },
                        variant = variant,
                    )
                }
            }
        }
        composeRule.onNodeWithContentDescription("Compartilhar PRIMARY").performClick()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `icon button desabilitado nao aceita toque`() {
        var cliques = 0
        setThemedContent {
            MnsIconButton(
                icon = Icons.Filled.Share,
                contentDescription = "Compartilhar",
                onClick = { cliques++ },
                enabled = false,
            )
        }
        composeRule.onNodeWithContentDescription("Compartilhar").performClick()
        assertThat(cliques).isEqualTo(0)
    }

    @Test
    fun `fab exibe o rotulo somente quando expandido`() {
        setThemedContent {
            androidx.compose.foundation.layout.Column {
                MnsFab(Icons.Filled.Add, "Criar recolhido", {}, expanded = false, label = "Oculto")
                MnsFab(Icons.Filled.Add, "Criar expandido", {}, expanded = true, label = "Novo evento")
            }
        }
        advance(500)
        composeRule.onNodeWithContentDescription("Criar recolhido").assertShown()
        composeRule.onNodeWithText("Novo evento").assertShown()
    }

    @Test
    fun `fab dispara onClick`() {
        var cliques = 0
        setThemedContent {
            MnsFab(Icons.Filled.Add, "Criar", { cliques++ })
        }
        composeRule.onNodeWithContentDescription("Criar").performClick()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `controle segmentado seleciona o indice tocado`() {
        var selecionado = 0
        setThemedContent {
            MnsSegmentedControl(
                segments = listOf(
                    MnsSegment("Só ida"),
                    MnsSegment("Ida e volta", icon = Icons.Filled.Add),
                    MnsSegment("Bloqueado", enabled = false),
                ),
                selectedIndex = selecionado,
                onSelect = { selecionado = it },
            )
        }
        composeRule.onNodeWithText("Ida e volta").performClick()
        assertThat(selecionado).isEqualTo(1)
    }

    @Test
    fun `segmento desabilitado nao seleciona`() {
        var selecionado = 0
        setThemedContent {
            MnsSegmentedControl(
                segments = listOf(MnsSegment("A"), MnsSegment("B", enabled = false)),
                selectedIndex = selecionado,
                onSelect = { selecionado = it },
            )
        }
        composeRule.onNodeWithText("B").performClick()
        assertThat(selecionado).isEqualTo(0)
    }

    @Test
    fun `controle segmentado satura indice fora da faixa`() {
        setThemedContent {
            MnsSegmentedControl(
                segments = listOf(MnsSegment("A"), MnsSegment("B")),
                selectedIndex = 99,
                onSelect = {},
            )
        }
        composeRule.onNodeWithText("B").assertShown()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `controle segmentado exige ao menos um segmento`() {
        setThemedContent {
            MnsSegmentedControl(segments = emptyList(), selectedIndex = 0, onSelect = {})
        }
        composeRule.onNodeWithText("qualquer").assertIsEnabled()
    }

    /**
     * Regressão: `MnsButton` aplicava `fillMaxWidth()` internamente mesmo com o
     * parâmetro `false`, consumindo toda a largura oferecida pelo pai. Numa
     * `Row`, isso zerava o espaço do irmão com `weight(1f)` — o título de
     * `MnsSectionHeader` sumia e o subtítulo quebrava em dezenas de linhas.
     */
    @Test
    fun `botao sem fillMaxWidth nao rouba a largura do irmao com weight`() {
        setThemedContent {
            Row(modifier = Modifier.fillMaxWidth()) {
                MnsText(
                    text = "Parâmetros",
                    modifier = Modifier.weight(1f).testTag("titulo"),
                )
                MnsButton(
                    text = "Restaurar",
                    onClick = {},
                    variant = MnsButtonVariant.TEXT,
                    size = MnsButtonSize.SMALL,
                )
            }
        }

        val larguraTitulo = composeRule.onNodeWithTag("titulo")
            .fetchSemanticsNode().size.width
        val larguraBotao = composeRule.onNodeWithContentDescription("Restaurar")
            .fetchSemanticsNode().size.width

        assertThat(larguraTitulo).isGreaterThan(0)
        assertThat(larguraBotao).isGreaterThan(0)
        assertThat(larguraTitulo).isGreaterThan(larguraBotao)
    }

    @Test
    fun `botao com fillMaxWidth ocupa a largura oferecida`() {
        setThemedContent {
            Box(modifier = Modifier.fillMaxWidth().testTag("container")) {
                MnsButton(text = "Pagar", onClick = {}, fillMaxWidth = true)
            }
        }
        val container = composeRule.onNodeWithTag("container").fetchSemanticsNode().size.width
        val botao = composeRule.onNodeWithContentDescription("Pagar").fetchSemanticsNode().size.width
        assertThat(botao).isEqualTo(container)
    }
}
