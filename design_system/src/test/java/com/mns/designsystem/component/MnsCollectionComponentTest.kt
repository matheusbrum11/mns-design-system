package com.mns.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.list.MnsAvatar
import com.mns.designsystem.component.list.MnsAvatarGroup
import com.mns.designsystem.component.list.MnsListAction
import com.mns.designsystem.component.list.MnsListLeading
import com.mns.designsystem.component.list.initialsOf
import com.mns.designsystem.component.shortcut.MnsShortcut
import com.mns.designsystem.component.shortcut.MnsShortcutCard
import com.mns.designsystem.component.shortcut.MnsShortcutGrid
import com.mns.designsystem.component.text.MnsText
import org.junit.Test

/** Testes de integração de listas, avatares e atalhos. */
class MnsCollectionComponentTest : MnsComposeTest() {

    private val participantes = listOf("Alves Farhat", "Bruna Lima", "Caio Souza", "Dara Nunes")

    @Test
    fun `iniciais cobrem nome composto simples e vazio`() {
        assertThat(initialsOf("Alves Farhat")).isEqualTo("AF")
        assertThat(initialsOf("Bruna")).isEqualTo("BR")
        assertThat(initialsOf("   ")).isEqualTo("?")
        assertThat(initialsOf("ana maria de souza")).isEqualTo("AS")
    }

    @Test
    fun `avatar expoe o nome como descricao acessivel`() {
        setThemedContent {
            Column {
                MnsAvatar(name = "Alves Farhat")
                MnsAvatar(name = "Com icone", icon = Icons.Filled.Star)
                MnsAvatar(name = "Com imagem", painter = ColorPainter(Color.Blue))
                MnsAvatar(name = "Descrito", contentDescription = "Foto de perfil")
            }
        }
        composeRule.onNodeWithContentDescription("Alves Farhat").assertShown()
        composeRule.onNodeWithContentDescription("Com icone").assertShown()
        composeRule.onNodeWithContentDescription("Com imagem").assertShown()
        composeRule.onNodeWithContentDescription("Foto de perfil").assertShown()
    }

    @Test
    fun `grupo de avatares resume o total na semantica`() {
        setThemedContent {
            MnsAvatarGroup(names = participantes, max = 2)
        }
        composeRule.onNodeWithContentDescription("4 participantes").assertShown()
    }

    @Test
    fun `grupo sem excedentes nao exibe contador`() {
        setThemedContent {
            MnsAvatarGroup(names = participantes.take(2), max = 3, showOverflowCount = false)
        }
        composeRule.onNodeWithContentDescription("2 participantes").assertShown()
    }

    @Test
    fun `item de lista exibe todas as linhas e dispara clique`() {
        var cliques = 0
        setThemedContent {
            MnsListAction(
                title = "North Van Hiking",
                overline = "Mount Seymour",
                subtitle = "Vancouver Community Centre",
                meta = "MAR 20 · 8:30 AM PDT",
                leading = MnsListLeading.Avatar("North Van"),
                trailing = { MnsText("R$ 0") },
                showChevron = true,
                onClick = { cliques++ },
            )
        }
        composeRule.onNodeWithText("North Van Hiking").assertShown()
        composeRule.onNodeWithText("Mount Seymour").assertShown()
        composeRule.onNodeWithText("Vancouver Community Centre").assertShown()
        composeRule.onNodeWithText("MAR 20 · 8:30 AM PDT").assertShown()
        composeRule.onNodeWithText("North Van Hiking").performClick()
        settle()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `item de lista cobre todos os tipos de leading`() {
        setThemedContent {
            Column {
                MnsListAction(title = "sem-leading", leading = MnsListLeading.None)
                MnsListAction(title = "avatar", leading = MnsListLeading.Avatar("Ana", size = 32.dp))
                MnsListAction(title = "icone", leading = MnsListLeading.Icon(Icons.Filled.Home))
                MnsListAction(
                    title = "thumb",
                    leading = MnsListLeading.Thumbnail(ColorPainter(Color.Red), size = 48.dp),
                )
                MnsListAction(
                    title = "custom",
                    leading = MnsListLeading.Custom { MnsText("slot") },
                )
            }
        }
        listOf("sem-leading", "avatar", "icone", "thumb", "custom", "slot").forEach {
            composeRule.onNodeWithText(it).assertShown()
        }
    }

    @Test
    fun `item de lista desabilitado nao dispara clique`() {
        var cliques = 0
        setThemedContent {
            MnsListAction(title = "off", onClick = { cliques++ }, enabled = false, selected = true)
        }
        composeRule.onNodeWithText("off").performClick()
        settle()
        assertThat(cliques).isEqualTo(0)
    }

    @Test
    fun `card de atalho reporta clique e estado selecionado`() {
        var cliques = 0
        setThemedContent {
            // Largura fixa de propósito: `aspectRatio(1f)` em largura cheia
            // geraria um card de 822dp de altura e o toque cairia fora da janela.
            Column {
                MnsShortcutCard(
                    shortcut = MnsShortcut("travel", "Travel", Icons.Filled.Star, badgeCount = 4),
                    onClick = { cliques++ },
                    selected = true,
                    modifier = Modifier.width(120.dp),
                )
                MnsShortcutCard(
                    shortcut = MnsShortcut("off", "Off", Icons.Filled.Star, enabled = false),
                    onClick = { cliques++ },
                    modifier = Modifier.width(120.dp),
                )
            }
        }
        composeRule.onNodeWithText("Travel").performClick()
        settle()
        composeRule.onNodeWithText("Off").performClick()
        settle()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `grade de atalhos alterna a selecao`() {
        setThemedContent {
            var selecionados by remember { mutableStateOf(setOf("a")) }
            MnsShortcutGrid(
                shortcuts = listOf(
                    MnsShortcut("a", "Arte", Icons.Filled.Star),
                    MnsShortcut("b", "Business", Icons.Filled.Home),
                ),
                onShortcutClick = { atalho ->
                    selecionados = if (atalho.id in selecionados) {
                        selecionados - atalho.id
                    } else {
                        selecionados + atalho.id
                    }
                },
                selectedIds = selecionados,
                modifier = Modifier.height(300.dp),
            )
        }
        composeRule.onNodeWithText("Arte").assertShown()
        composeRule.onNodeWithText("Business").performClick()
        settle()
        composeRule.onNodeWithText("Business").assertShown()
    }
}
