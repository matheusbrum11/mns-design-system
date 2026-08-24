package com.mns.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsFab
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.layout.MnsBottomNavBar
import com.mns.designsystem.component.layout.MnsCard
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsDivider
import com.mns.designsystem.component.layout.MnsFabPosition
import com.mns.designsystem.component.layout.MnsFixedTabBar
import com.mns.designsystem.component.layout.MnsLabeledDivider
import com.mns.designsystem.component.layout.MnsNavItem
import com.mns.designsystem.component.layout.MnsScaffold
import com.mns.designsystem.component.layout.MnsScreenColumn
import com.mns.designsystem.component.layout.MnsSurface
import com.mns.designsystem.component.layout.MnsTab
import com.mns.designsystem.component.layout.MnsTabBar
import com.mns.designsystem.component.layout.MnsTopBar
import com.mns.designsystem.component.layout.MnsTopBarAlignment
import com.mns.designsystem.component.layout.MnsVerticalDivider
import com.mns.designsystem.component.text.MnsText
import org.junit.Test

/** Testes de integração dos componentes de layout. */
class MnsLayoutComponentTest : MnsComposeTest() {

    @Test
    fun `superficie clicavel dispara onClick e respeita enabled`() {
        var cliques = 0
        setThemedContent {
            Column {
                MnsSurface(onClick = { cliques++ }) { MnsText("Ativa") }
                MnsSurface(onClick = { cliques++ }, enabled = false) { MnsText("Inativa") }
                MnsSurface { MnsText("Estatica") }
            }
        }
        composeRule.onNodeWithText("Ativa").performClick()
        settle()
        composeRule.onNodeWithText("Inativa").performClick()
        settle()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `card renderiza todas as variantes`() {
        setThemedContent {
            Column {
                MnsCardVariant.entries.forEach { variante ->
                    MnsCard(variant = variante) { MnsText("card-${variante.name}") }
                }
            }
        }
        MnsCardVariant.entries.forEach { variante ->
            composeRule.onNodeWithText("card-${variante.name}").assertShown()
        }
    }

    @Test
    fun `card clicavel dispara e card desabilitado nao`() {
        var cliques = 0
        setThemedContent {
            Column {
                MnsCard(onClick = { cliques++ }) { MnsText("Clicavel") }
                MnsCard(onClick = { cliques++ }, enabled = false) { MnsText("Desabilitado") }
            }
        }
        composeRule.onNodeWithText("Clicavel").performClick()
        settle()
        composeRule.onNodeWithText("Desabilitado").performClick()
        settle()
        assertThat(cliques).isEqualTo(1)
    }

    @Test
    fun `divisores renderizam nas tres formas`() {
        setThemedContent {
            Column {
                MnsDivider()
                MnsLabeledDivider(text = "ou")
                Box(modifier = Modifier.padding(1.dp())) { MnsVerticalDivider() }
            }
        }
        composeRule.onNodeWithText("ou").assertShown()
    }

    @Test
    fun `top bar exibe titulo subtitulo voltar e acoes`() {
        var voltou = 0
        setThemedContent {
            MnsTopBar(
                title = "Checkout",
                subtitle = "Passo 2 de 3",
                onNavigateBack = { voltou++ },
                actions = {
                    MnsIconButton(Icons.Filled.Share, "Compartilhar", {}, variant = MnsButtonVariant.TEXT)
                },
            )
        }
        composeRule.onNodeWithText("Checkout").assertShown()
        composeRule.onNodeWithText("Passo 2 de 3").assertShown()
        composeRule.onNodeWithContentDescription("Compartilhar").assertShown()
        composeRule.onNodeWithContentDescription("Voltar").performClick()
        settle()
        assertThat(voltou).isEqualTo(1)
    }

    @Test
    fun `top bar centralizada reserva espaco simetrico sem acoes`() {
        setThemedContent {
            MnsTopBar(title = "Centralizado", alignment = MnsTopBarAlignment.CENTER)
        }
        composeRule.onNodeWithText("Centralizado").assertShown()
    }

    @Test
    fun `top bar aceita icone de navegacao customizado`() {
        setThemedContent {
            MnsTopBar(
                title = "Custom",
                navigationIcon = {
                    MnsIconButton(Icons.Filled.Home, "Ir para o início", {})
                },
            )
        }
        composeRule.onNodeWithContentDescription("Ir para o início").assertShown()
    }

    @Test
    fun `bottom nav seleciona destino e exibe badge`() {
        setThemedContent {
            var selecionado by remember { mutableStateOf("home") }
            MnsBottomNavBar(
                items = listOf(
                    MnsNavItem("home", "Início", Icons.Filled.Home, Icons.Filled.Home),
                    MnsNavItem("perfil", "Perfil", Icons.Filled.Person, badgeCount = 3),
                ),
                selectedId = selecionado,
                onSelect = { selecionado = it.id },
            )
        }
        composeRule.onNodeWithText("Início").assertIsSelected()
        composeRule.onNodeWithText("Perfil").performClick()
        settle()
        composeRule.onNodeWithText("Perfil").assertIsSelected()
    }

    @Test
    fun `bottom nav sem rotulos usa o label como descricao`() {
        setThemedContent {
            MnsBottomNavBar(
                items = listOf(MnsNavItem("home", "Início", Icons.Filled.Home)),
                selectedId = "home",
                onSelect = {},
                showLabels = false,
            )
        }
        composeRule.onNodeWithContentDescription("Início").assertShown()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `bottom nav exige ao menos um item`() {
        setThemedContent {
            MnsBottomNavBar(items = emptyList(), selectedId = "x", onSelect = {})
        }
        composeRule.onNodeWithText("x").assertShown()
    }

    @Test
    fun `tab bar rolavel seleciona a aba tocada`() {
        setThemedContent {
            var index by remember { mutableIntStateOf(0) }
            MnsTabBar(
                tabs = listOf(MnsTab("a", "Todos"), MnsTab("b", "Hoje", badgeCount = 5)),
                selectedIndex = index,
                onSelect = { index = it },
            )
        }
        advance(400)
        composeRule.onNodeWithText("Hoje").performClick()
        settle()
        composeRule.onNodeWithText("Hoje").assertIsSelected()
    }

    @Test
    fun `tab bar fixa seleciona a aba tocada`() {
        setThemedContent {
            var index by remember { mutableIntStateOf(0) }
            MnsFixedTabBar(
                tabs = listOf(MnsTab("a", "A"), MnsTab("b", "B")),
                selectedIndex = index,
                onSelect = { index = it },
            )
        }
        composeRule.onNodeWithText("B").performClick()
        settle()
        composeRule.onNodeWithText("B").assertIsSelected()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `tab bar exige ao menos uma aba`() {
        setThemedContent { MnsTabBar(tabs = emptyList(), selectedIndex = 0, onSelect = {}) }
        composeRule.onNodeWithText("x").assertShown()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `tab bar fixa exige ao menos uma aba`() {
        setThemedContent { MnsFixedTabBar(tabs = emptyList(), selectedIndex = 0, onSelect = {}) }
        composeRule.onNodeWithText("x").assertShown()
    }

    @Test
    fun `scaffold entrega padding das barras ao conteudo`() {
        setThemedContent {
            MnsScaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MnsTopBar(title = "Topo", applyStatusBarPadding = false) },
                bottomBar = {
                    MnsBottomNavBar(
                        items = listOf(MnsNavItem("a", "A", Icons.Filled.Home)),
                        selectedId = "a",
                        onSelect = {},
                        applyNavigationBarPadding = false,
                    )
                },
                floatingActionButton = { MnsFab(Icons.Filled.Add, "Criar", {}) },
                banner = { MnsText("Banner fixo") },
            ) { padding ->
                MnsScreenColumn(contentPadding = padding) {
                    MnsText("Conteudo")
                    MnsText("top=${padding.calculateTopPadding()}")
                }
            }
        }
        composeRule.onNodeWithText("Topo").assertShown()
        composeRule.onNodeWithText("Banner fixo").assertShown()
        composeRule.onNodeWithText("Conteudo").assertShown()
        composeRule.onNodeWithContentDescription("Criar").assertShown()
    }

    @Test
    fun `scaffold com fab centralizado e sem insets renderiza`() {
        setThemedContent {
            MnsScaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = { MnsFab(Icons.Filled.Add, "Centro", {}) },
                fabPosition = MnsFabPosition.CENTER,
                contentWindowInsetsPadding = false,
            ) { _ -> MnsText("Sem barras") }
        }
        composeRule.onNodeWithText("Sem barras").assertShown()
        composeRule.onNodeWithContentDescription("Centro").assertShown()
    }
}

private fun Int.dp() = androidx.compose.ui.unit.Dp(this.toFloat())
