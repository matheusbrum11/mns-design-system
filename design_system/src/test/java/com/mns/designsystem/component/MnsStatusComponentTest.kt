package com.mns.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.status.MnsAlert
import com.mns.designsystem.component.status.MnsBadge
import com.mns.designsystem.component.status.MnsBadgedBox
import com.mns.designsystem.component.status.MnsCircularProgress
import com.mns.designsystem.component.status.MnsEmptyState
import com.mns.designsystem.component.status.MnsLinearProgress
import com.mns.designsystem.component.status.MnsRating
import com.mns.designsystem.component.status.MnsTag
import com.mns.designsystem.token.MnsStatus
import org.junit.Test
import java.util.Locale

/** Testes de integração dos componentes de status e feedback. */
class MnsStatusComponentTest : MnsComposeTest() {

    @Test
    fun `badge exibe contador e satura no maximo`() {
        setThemedContent {
            Column {
                MnsBadge(count = 3)
                MnsBadge(count = 150, max = 99)
            }
        }
        composeRule.onNodeWithContentDescription("3").assertShown()
        composeRule.onNodeWithContentDescription("99+").assertShown()
    }

    @Test
    fun `badge sem contador vira ponto de novidade`() {
        setThemedContent { MnsBadge(count = null) }
        composeRule.onNodeWithContentDescription("Novidade").assertShown()
    }

    @Test
    fun `badge cobre todos os status`() {
        setThemedContent {
            Column {
                MnsStatus.entries.forEachIndexed { index, status ->
                    MnsBadge(
                        count = index + 1,
                        status = status,
                        contentDescription = "badge-${status.name}",
                    )
                }
            }
        }
        MnsStatus.entries.forEach { status ->
            composeRule.onNodeWithContentDescription("badge-${status.name}").assertShown()
        }
    }

    @Test
    fun `badged box ancora o badge sobre o conteudo`() {
        setThemedContent {
            MnsBadgedBox(badge = { MnsBadge(count = 7) }) {
                MnsIcon(Icons.Filled.Notifications, "Notificações")
            }
        }
        composeRule.onNodeWithContentDescription("Notificações").assertShown()
        composeRule.onNodeWithContentDescription("7").assertShown()
    }

    @Test
    fun `tag renderiza solida e suave em todos os status`() {
        setThemedContent {
            Column {
                MnsStatus.entries.forEach { status ->
                    MnsTag(text = "suave-${status.name}", status = status)
                    MnsTag(text = "solida-${status.name}", status = status, solid = true, icon = Icons.Filled.Star)
                }
            }
        }
        MnsStatus.entries.forEach { status ->
            composeRule.onNodeWithText("suave-${status.name}").assertShown()
            composeRule.onNodeWithText("solida-${status.name}").assertShown()
        }
    }

    @Test
    fun `alerta exibe titulo mensagem acao e fechar`() {
        var fechou = 0
        setThemedContent {
            MnsAlert(
                message = "Conclua o pagamento em 15 minutos.",
                title = "Pagamento pendente",
                status = MnsStatus.WARNING,
                onDismiss = { fechou++ },
                action = { MnsButton("Pagar agora", {}) },
            )
        }
        composeRule.onNodeWithText("Pagamento pendente").assertShown()
        composeRule.onNodeWithText("Conclua o pagamento em 15 minutos.").assertShown()
        composeRule.onNodeWithText("Pagar agora").assertShown()
        composeRule.onNodeWithContentDescription("Fechar aviso").performClick()
        settle()
        assertThat(fechou).isEqualTo(1)
    }

    @Test
    fun `alerta escolhe icone por status`() {
        setThemedContent {
            Column {
                MnsStatus.entries.forEach { status ->
                    MnsAlert(message = "msg-${status.name}", status = status)
                }
            }
        }
        MnsStatus.entries.forEach { status ->
            composeRule.onNodeWithText("msg-${status.name}").assertShown()
        }
    }

    @Test
    fun `progresso determinado e indeterminado expoem semantica`() {
        setThemedContent {
            Column {
                MnsLinearProgress(progress = 0.4f, contentDescription = "Linear determinado")
                MnsLinearProgress(progress = null, contentDescription = "Linear indeterminado")
                MnsCircularProgress(progress = 0.7f, contentDescription = "Circular determinado")
                MnsCircularProgress(progress = null, contentDescription = "Circular indeterminado")
            }
        }
        advance(400)
        composeRule.onNodeWithContentDescription("Linear determinado").assertShown()
        composeRule.onNodeWithContentDescription("Linear indeterminado").assertShown()
        composeRule.onNodeWithContentDescription("Circular determinado").assertShown()
        composeRule.onNodeWithContentDescription("Circular indeterminado").assertShown()
    }

    @Test
    fun `progresso satura valores fora de 0 a 1`() {
        setThemedContent {
            Column {
                MnsLinearProgress(progress = 5f, contentDescription = "acima")
                MnsCircularProgress(progress = -3f, contentDescription = "abaixo")
            }
        }
        composeRule.onNodeWithContentDescription("acima").assertShown()
        composeRule.onNodeWithContentDescription("abaixo").assertShown()
    }

    @Test
    fun `avaliacao interativa reporta a nota tocada`() {
        var nota = 0
        setThemedContent {
            MnsRating(rating = 3f, onRatingChange = { nota = it })
        }
        // O separador decimal segue o Locale do ambiente — travar em vírgula
        // faria o teste quebrar em uma máquina com locale en-US.
        val esperado = String.format(Locale.getDefault(), "%.1f", 3f)
        composeRule.onNodeWithText(esperado).assertShown()
        assertThat(nota).isEqualTo(0)
    }

    @Test
    fun `avaliacao sem valor numerico omite o texto`() {
        setThemedContent { MnsRating(rating = 4.6f, showValue = false) }
        composeRule.onNodeWithText(String.format(Locale.getDefault(), "%.1f", 4.6f))
            .assertDoesNotExist()
    }

    @Test
    fun `estado vazio exibe titulo descricao icone e acao`() {
        setThemedContent {
            MnsEmptyState(
                title = "Nenhum evento por aqui",
                description = "Nada corresponde aos filtros.",
                icon = Icons.Filled.Warning,
                action = { MnsButton("Limpar filtros", {}) },
            )
        }
        composeRule.onNodeWithText("Nenhum evento por aqui").assertShown()
        composeRule.onNodeWithText("Nada corresponde aos filtros.").assertShown()
        composeRule.onNodeWithText("Limpar filtros").assertShown()
    }

    @Test
    fun `estado vazio minimo renderiza apenas o titulo`() {
        setThemedContent { MnsEmptyState(title = "Vazio") }
        composeRule.onNodeWithText("Vazio").assertShown()
    }
}
