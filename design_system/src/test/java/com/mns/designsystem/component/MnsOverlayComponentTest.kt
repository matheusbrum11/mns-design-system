package com.mns.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.layout.MnsBottomSheet
import com.mns.designsystem.component.layout.MnsConfirmDialog
import com.mns.designsystem.component.layout.MnsDialog
import com.mns.designsystem.component.layout.MnsSheetHandle
import com.mns.designsystem.component.layout.MnsSheetHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.token.MnsStatus
import org.junit.Test

/**
 * Testes dos componentes que abrem em janela própria (sheet e dialog).
 *
 * Diferente do resto do catálogo, estes componentes vivem em uma janela
 * separada da árvore principal; o framework de teste os inclui na busca, mas
 * qualquer asserção de geometria seria sobre a janela do overlay — por isso as
 * verificações aqui são de conteúdo e comportamento.
 */
@OptIn(ExperimentalMaterial3Api::class)
class MnsOverlayComponentTest : MnsComposeTest() {

    @Test
    fun `dialog exibe conteudo e fecha ao pedido`() {
        var fechou = 0
        setThemedContent {
            MnsDialog(onDismissRequest = { fechou++ }) {
                MnsText("Conteúdo do dialog")
                MnsButton("Fechar", onClick = { fechou++ })
            }
        }
        composeRule.onNodeWithText("Conteúdo do dialog").assertShown()
        composeRule.onNodeWithText("Fechar").performClick()
        settle()
        assertThat(fechou).isEqualTo(1)
    }

    @Test
    fun `dialog de confirmacao exibe titulo mensagem e as duas acoes`() {
        var confirmou = 0
        var cancelou = 0
        setThemedContent {
            MnsConfirmDialog(
                title = "Cancelar ingresso?",
                message = "Esta ação não pode ser desfeita.",
                confirmText = "Cancelar ingresso",
                dismissText = "Voltar",
                status = MnsStatus.DANGER,
                icon = Icons.Filled.Star,
                onConfirm = { confirmou++ },
                onDismissRequest = { cancelou++ },
            )
        }
        composeRule.onNodeWithText("Cancelar ingresso?").assertShown()
        composeRule.onNodeWithText("Esta ação não pode ser desfeita.").assertShown()
        composeRule.onNodeWithText("Voltar").performClick()
        settle()
        composeRule.onNodeWithText("Cancelar ingresso").performClick()
        settle()
        assertThat(cancelou).isEqualTo(1)
        assertThat(confirmou).isEqualTo(1)
    }

    @Test
    fun `dialog de confirmacao sem botao de cancelar renderiza so a confirmacao`() {
        setThemedContent {
            MnsConfirmDialog(
                title = "Tudo certo",
                message = "Seu ingresso foi emitido.",
                confirmText = "Entendi",
                dismissText = null,
                status = MnsStatus.SUCCESS,
                onConfirm = {},
                onDismissRequest = {},
            )
        }
        composeRule.onNodeWithText("Entendi").assertShown()
        composeRule.onNodeWithText("Cancelar").assertDoesNotExist()
    }

    @Test
    fun `dialog de confirmacao em carregamento desabilita o cancelamento`() {
        var cancelou = 0
        setThemedContent {
            MnsConfirmDialog(
                title = "Processando",
                message = "Aguarde a confirmação do pagamento.",
                confirmText = "Pagar",
                dismissText = "Voltar",
                loading = true,
                onConfirm = {},
                onDismissRequest = { cancelou++ },
            )
        }
        composeRule.onNodeWithText("Voltar").performClick()
        settle()
        assertThat(cancelou).isEqualTo(0)
    }

    @Test
    fun `cabecalho de sheet exibe titulo subtitulo e fechar`() {
        var fechou = 0
        setThemedContent {
            MnsSheetHeader(
                title = "Filtros",
                subtitle = "Refine os eventos",
                onClose = { fechou++ },
            )
        }
        composeRule.onNodeWithText("Filtros").assertShown()
        composeRule.onNodeWithText("Refine os eventos").assertShown()
        composeRule.onNodeWithContentDescription("Fechar").performClick()
        settle()
        assertThat(fechou).isEqualTo(1)
    }

    @Test
    fun `alca do sheet renderiza isoladamente`() {
        setThemedContent {
            MnsSheetHandle()
            MnsText("com-alca")
        }
        composeRule.onNodeWithText("com-alca").assertShown()
    }

    @Test
    fun `bottom sheet modal exibe cabecalho e conteudo`() {
        setThemedContent {
            var aberto by remember { mutableStateOf(true) }
            if (aberto) {
                MnsBottomSheet(
                    onDismissRequest = { aberto = false },
                    title = "Filtros",
                    subtitle = "Refine os eventos exibidos",
                ) {
                    MnsText("Corpo do sheet")
                }
            }
        }
        advance(600)
        composeRule.onNodeWithText("Filtros").assertShown()
        composeRule.onNodeWithText("Corpo do sheet").assertShown()
    }

    @Test
    fun `bottom sheet sem cabecalho nem alca renderiza apenas o conteudo`() {
        setThemedContent {
            MnsBottomSheet(
                onDismissRequest = {},
                title = null,
                showHandle = false,
                showCloseButton = false,
            ) {
                MnsText("Somente corpo")
            }
        }
        advance(600)
        composeRule.onNodeWithText("Somente corpo").assertShown()
    }
}
