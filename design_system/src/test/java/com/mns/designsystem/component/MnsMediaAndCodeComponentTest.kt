package com.mns.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.code.MnsQrCode
import com.mns.designsystem.component.code.MnsQrDotStyle
import com.mns.designsystem.component.code.MnsQrEncoder
import com.mns.designsystem.component.code.MnsQrErrorCorrection
import com.mns.designsystem.component.code.MnsQrMatrix
import com.mns.designsystem.component.code.MnsTicketCard
import com.mns.designsystem.component.code.MnsTicketPerforation
import com.mns.designsystem.component.loading.MnsShimmerBox
import com.mns.designsystem.component.loading.MnsShimmerCard
import com.mns.designsystem.component.loading.MnsShimmerListItem
import com.mns.designsystem.component.loading.MnsShimmerParagraph
import com.mns.designsystem.component.loading.mnsShimmer
import com.mns.designsystem.component.media.MnsCover
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.media.MnsIcons
import com.mns.designsystem.component.text.MnsText
import org.junit.Test

/** Testes de integração de carregamento, mídia e códigos. */
class MnsMediaAndCodeComponentTest : MnsComposeTest() {

    // ── Shimmer ──────────────────────────────────────────────────────────────

    @Test
    fun `esqueletos anunciam estado de carregamento`() {
        setThemedContent {
            Column {
                MnsShimmerBox(width = 120.dp, height = 16.dp)
                MnsShimmerBox()
                MnsShimmerParagraph(lines = 3)
                MnsShimmerListItem()
                MnsShimmerCard()
            }
        }
        advance(600)
        // O parágrafo aparece duas vezes: solto e dentro do MnsShimmerCard.
        composeRule.onAllNodesWithContentDescription("Carregando conteúdo")
            .assertCountEquals(2)
        composeRule.onNodeWithContentDescription("Carregando item").assertShown()
        composeRule.onNodeWithContentDescription("Carregando card").assertShown()
    }

    @Test
    fun `shimmer desligado nao anima mas mantem o layout`() {
        setThemedContent {
            Column {
                MnsShimmerParagraph(lines = 2, visible = false)
                MnsShimmerListItem(showAvatar = false, visible = false)
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxWidth().mnsShimmer(visible = false),
                )
            }
        }
        composeRule.onNodeWithContentDescription("Carregando conteúdo").assertShown()
    }

    // ── Mídia ────────────────────────────────────────────────────────────────

    @Test
    fun `icone aceita vetor e painter com e sem descricao`() {
        setThemedContent {
            Column {
                MnsIcon(Icons.Filled.Star, "Favorito")
                MnsIcon(Icons.Filled.Star, null)
                MnsIcon(ColorPainter(Color.Green), "Quadrado verde")
                MnsIcon(MnsIcons.Visibility, "Mostrar", tint = Color.Unspecified)
                MnsIcon(MnsIcons.VisibilityOff, "Ocultar")
                MnsIcon(MnsIcons.Minus, "Menos")
                MnsIcon(MnsIcons.Bookmark, "Salvar")
                MnsIcon(MnsIcons.QrCode, "Código")
            }
        }
        listOf("Favorito", "Quadrado verde", "Mostrar", "Ocultar", "Menos", "Salvar", "Código")
            .forEach { composeRule.onNodeWithContentDescription(it).assertShown() }
    }

    @Test
    fun `capa exibe placeholder imagem scrim e overlay`() {
        setThemedContent {
            Column {
                MnsCover(painter = null, contentDescription = null, height = 100.dp)
                MnsCover(
                    painter = ColorPainter(Color.Magenta),
                    contentDescription = "Capa do evento",
                    height = 120.dp,
                    scrim = true,
                    overlay = { MnsText("Sobreposto") },
                )
            }
        }
        advance(400)
        composeRule.onNodeWithContentDescription("Capa do evento").assertShown()
        composeRule.onNodeWithText("Sobreposto").assertShown()
    }

    // ── QR Code ──────────────────────────────────────────────────────────────

    @Test
    fun `encoder produz matriz quadrada e nao vazia`() {
        val matriz = MnsQrEncoder.encode("MNS-TICKET-8842")
        assertThat(matriz.size).isAtLeast(21)
        var escuros = 0
        for (y in 0 until matriz.size) {
            for (x in 0 until matriz.size) {
                if (matriz[x, y]) escuros++
            }
        }
        assertThat(escuros).isGreaterThan(0)
    }

    @Test
    fun `matriz devolve falso fora dos limites`() {
        val matriz = MnsQrEncoder.encode("abc")
        assertThat(matriz[-1, 0]).isFalse()
        assertThat(matriz[0, matriz.size]).isFalse()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `encoder recusa conteudo vazio`() {
        MnsQrEncoder.encode("")
    }

    @Test
    fun `niveis de correcao produzem matrizes validas`() {
        MnsQrErrorCorrection.entries.forEach { nivel ->
            assertThat(MnsQrEncoder.encode("MNS", nivel).size).isAtLeast(21)
        }
    }

    @Test
    fun `matriz a partir de linhas de texto`() {
        val matriz = MnsQrMatrix.fromRows(listOf("#.#", ".#.", "#.#"))
        assertThat(matriz.size).isEqualTo(3)
        assertThat(matriz[0, 0]).isTrue()
        assertThat(matriz[1, 0]).isFalse()
        assertThat(matriz[1, 1]).isTrue()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `matriz recusa linhas nao quadradas`() {
        MnsQrMatrix.fromRows(listOf("##", "#"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `matriz recusa lista vazia`() {
        MnsQrMatrix.fromRows(emptyList())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `matriz recusa contagem de modulos incoerente`() {
        MnsQrMatrix(size = 3, modules = BooleanArray(4))
    }

    @Test
    fun `qr code renderiza todos os estilos de modulo`() {
        setThemedContent {
            Column {
                MnsQrDotStyle.entries.forEach { estilo ->
                    MnsQrCode(
                        content = "MNS-$estilo",
                        size = 120.dp,
                        dotStyle = estilo,
                        contentDescription = "QR $estilo",
                        caption = "cap-$estilo",
                    )
                }
            }
        }
        MnsQrDotStyle.entries.forEach { estilo ->
            composeRule.onNodeWithContentDescription("QR $estilo").assertShown()
            composeRule.onNodeWithText("cap-$estilo").assertShown()
        }
    }

    @Test
    fun `qr code aceita matriz pronta e slot de logo`() {
        setThemedContent {
            MnsQrCode(
                matrix = MnsQrMatrix.fromRows(listOf("##.", ".#.", "..#")),
                size = 100.dp,
                quietZoneModules = 0,
                contentDescription = "QR manual",
                logo = { MnsText("logo") },
            )
        }
        composeRule.onNodeWithContentDescription("QR manual").assertShown()
        composeRule.onNodeWithText("logo").assertShown()
    }

    @Test
    fun `cartao de ingresso monta cabecalho detalhes e codigo`() {
        setThemedContent {
            MnsTicketCard(
                title = "Newport Beach Jazz Festival",
                subtitle = "Sydney, Australia · 19 Oct 2024",
                qrContent = "MNS-TICKET-8842",
                footnote = "Apresente na entrada",
                details = listOf(
                    "Assento" to "D17, D18",
                    "Portão" to "G2",
                    "Tipo" to "E-Ticket",
                ),
            )
        }
        composeRule.onNodeWithText("Newport Beach Jazz Festival").assertShown()
        composeRule.onNodeWithText("D17, D18").assertShown()
        composeRule.onNodeWithText("E-Ticket").assertShown()
        composeRule.onNodeWithText("Apresente na entrada").assertShown()
        composeRule
            .onNodeWithContentDescription("Código do ingresso para Newport Beach Jazz Festival")
            .assertShown()
    }

    @Test
    fun `cartao de ingresso sem detalhes tambem renderiza`() {
        setThemedContent {
            Column {
                MnsTicketCard(title = "Simples", qrContent = "X")
                MnsTicketPerforation()
            }
        }
        composeRule.onNodeWithText("Simples").assertShown()
    }
}
