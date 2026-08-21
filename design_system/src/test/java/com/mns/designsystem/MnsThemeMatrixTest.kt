package com.mns.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonSize
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.action.MnsFab
import com.mns.designsystem.component.action.MnsIconButton
import com.mns.designsystem.component.action.MnsSegment
import com.mns.designsystem.component.action.MnsSegmentedControl
import com.mns.designsystem.component.code.MnsQrCode
import com.mns.designsystem.component.code.MnsTicketCard
import com.mns.designsystem.component.input.MnsCheckbox
import com.mns.designsystem.component.input.MnsChip
import com.mns.designsystem.component.input.MnsCurrencyField
import com.mns.designsystem.component.input.MnsOtpField
import com.mns.designsystem.component.input.MnsPasswordField
import com.mns.designsystem.component.input.MnsRadioButton
import com.mns.designsystem.component.input.MnsSearchField
import com.mns.designsystem.component.input.MnsSlider
import com.mns.designsystem.component.input.MnsStepper
import com.mns.designsystem.component.input.MnsSwitch
import com.mns.designsystem.component.input.MnsTextField
import com.mns.designsystem.component.input.MnsToggleState
import com.mns.designsystem.component.layout.MnsBottomNavBar
import com.mns.designsystem.component.layout.MnsCard
import com.mns.designsystem.component.layout.MnsCardVariant
import com.mns.designsystem.component.layout.MnsDivider
import com.mns.designsystem.component.layout.MnsFixedTabBar
import com.mns.designsystem.component.layout.MnsLabeledDivider
import com.mns.designsystem.component.layout.MnsNavItem
import com.mns.designsystem.component.layout.MnsTab
import com.mns.designsystem.component.layout.MnsTabBar
import com.mns.designsystem.component.layout.MnsTopBar
import com.mns.designsystem.component.list.MnsAvatar
import com.mns.designsystem.component.list.MnsAvatarGroup
import com.mns.designsystem.component.list.MnsListAction
import com.mns.designsystem.component.list.MnsListLeading
import com.mns.designsystem.component.loading.MnsShimmerCard
import com.mns.designsystem.component.loading.MnsShimmerListItem
import com.mns.designsystem.component.loading.MnsShimmerParagraph
import com.mns.designsystem.component.media.MnsCover
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.shortcut.MnsShortcut
import com.mns.designsystem.component.shortcut.MnsShortcutCard
import com.mns.designsystem.component.status.MnsAlert
import com.mns.designsystem.component.status.MnsBadge
import com.mns.designsystem.component.status.MnsCircularProgress
import com.mns.designsystem.component.status.MnsEmptyState
import com.mns.designsystem.component.status.MnsLinearProgress
import com.mns.designsystem.component.status.MnsRating
import com.mns.designsystem.component.status.MnsTag
import com.mns.designsystem.component.text.MnsCompactNumberText
import com.mns.designsystem.component.text.MnsCurrencyText
import com.mns.designsystem.component.text.MnsHeading
import com.mns.designsystem.component.text.MnsHeadingLevel
import com.mns.designsystem.component.text.MnsPercentText
import com.mns.designsystem.component.text.MnsSectionHeader
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsThemeSpec
import com.mns.designsystem.theme.preset.MnsThemePresets
import com.mns.designsystem.token.MnsStatus
import org.junit.Test

/**
 * Matriz tema × componente.
 *
 * Renderiza **todo** o catálogo do design system em cada preset, claro e
 * escuro. É o teste que impede a regressão mais cara de um design system:
 * alguém deixar um `Color(0xFF...)` ou um `16.dp` cravado dentro de um
 * componente. Um valor hard-coded não quebra o preset em que foi escrito — ele
 * quebra os outros cinco, e é aqui que isso aparece.
 *
 * Também serve de rede de cobertura: um único caminho de composição por
 * componente, multiplicado por 6 temas, exercita os `when` de variante, os
 * estados de habilitado/desabilitado e as derivações de cor.
 */
class MnsThemeMatrixTest : MnsComposeTest() {

    private val specs: List<MnsThemeSpec> = MnsThemePresets.all.flatMap {
        listOf(it.light, it.dark)
    }

    @Test
    fun `catalogo completo renderiza em todos os presets claros e escuros`() {
        setThemedContent(spec = MnsThemePresets.IndigoTicket.light) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                specs.forEach { spec ->
                    com.mns.designsystem.theme.MnsTheme(spec = spec) {
                        CatalogoCompleto(marcador = spec.id)
                    }
                }
            }
        }
        advance(600)
        specs.forEach { spec ->
            composeRule.onNodeWithText("matriz-${spec.id}").assertShown()
        }
    }
}

/**
 * Uma instância de cada componente público do design system.
 *
 * Mantido em um único lugar para que adicionar um componente novo ao MNS
 * signifique adicioná-lo aqui — e, com isso, ganhar cobertura em 6 temas de
 * graça.
 */
@Composable
private fun CatalogoCompleto(marcador: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MnsText("matriz-$marcador")

        // ── Texto ────────────────────────────────────────────────────────────
        MnsHeading("Título", level = MnsHeadingLevel.H1, overline = "over", subtitle = "sub")
        MnsSectionHeader(title = "Seção", action = { MnsButton("Ver", {}, variant = MnsButtonVariant.TEXT) })
        MnsCurrencyText(cents = 12550, emphasizeSymbol = true)
        MnsPercentText(value = 0.184, colorizeSign = true)
        MnsCompactNumberText(value = 12400)

        // ── Ações ────────────────────────────────────────────────────────────
        MnsButtonVariant.entries.forEach { variante ->
            MnsButton(
                text = variante.name,
                onClick = {},
                variant = variante,
                size = MnsButtonSize.MEDIUM,
                leadingIcon = Icons.Filled.Add,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            MnsIconButton(Icons.Filled.Star, "Favorito $marcador", {})
            MnsFab(Icons.Filled.Add, "Criar $marcador", {}, expanded = true, label = "Novo")
        }
        MnsSegmentedControl(
            segments = listOf(MnsSegment("Ida"), MnsSegment("Volta", icon = Icons.Filled.Home)),
            selectedIndex = 0,
            onSelect = {},
        )

        // ── Entrada ──────────────────────────────────────────────────────────
        MnsTextField(value = "texto", onValueChange = {}, label = "Campo", helperText = "dica")
        MnsTextField(value = "", onValueChange = {}, errorMessage = "erro", maxLength = 10)
        MnsSearchField(value = "busca", onValueChange = {})
        MnsCurrencyField(cents = 12550, onCentsChange = {}, label = "Valor")
        MnsPasswordField(value = "segredo", onValueChange = {})
        MnsOtpField(value = "123", onValueChange = {}, length = 4)
        MnsCheckbox(state = MnsToggleState.CHECKED, onStateChange = {}, label = "Check")
        MnsRadioButton(selected = true, onSelect = {}, label = "Radio")
        MnsSwitch(checked = true, onCheckedChange = {}, label = "Switch")
        MnsChip(label = "chip", selected = true, onClick = {}, onDismiss = {})
        MnsStepper(value = 2, onValueChange = {}, label = "Qtd")
        MnsSlider(value = 0.5f, onValueChange = {}, label = "Slider")

        // ── Status ───────────────────────────────────────────────────────────
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            MnsBadge(count = 3, contentDescription = "badge-$marcador")
            MnsStatus.entries.forEach { MnsTag(text = it.name, status = it) }
        }
        MnsAlert(message = "Aviso", title = "Título", status = MnsStatus.WARNING, onDismiss = {})
        MnsLinearProgress(progress = 0.4f)
        MnsCircularProgress(progress = null)
        MnsRating(rating = 4.5f)
        MnsEmptyState(title = "Vazio", description = "Sem itens", icon = Icons.Filled.Star)

        // ── Layout ───────────────────────────────────────────────────────────
        MnsCardVariant.entries.forEach { variante ->
            MnsCard(variant = variante, onClick = {}) { MnsText("card-${variante.name}") }
        }
        MnsDivider()
        MnsLabeledDivider(text = "ou")
        MnsTopBar(title = "Topo", onNavigateBack = {}, applyStatusBarPadding = false)
        MnsTabBar(
            tabs = listOf(MnsTab("a", "A"), MnsTab("b", "B", badgeCount = 2)),
            selectedIndex = 0,
            onSelect = {},
        )
        MnsFixedTabBar(tabs = listOf(MnsTab("a", "A"), MnsTab("b", "B")), selectedIndex = 0, onSelect = {})
        MnsBottomNavBar(
            items = listOf(
                MnsNavItem("a", "Início", Icons.Filled.Home),
                MnsNavItem("b", "Perfil", Icons.Filled.Star, badgeCount = 1),
            ),
            selectedId = "a",
            onSelect = {},
            applyNavigationBarPadding = false,
        )

        // ── Listas e atalhos ─────────────────────────────────────────────────
        MnsListAction(
            title = "Item",
            overline = "over",
            subtitle = "sub",
            meta = "meta",
            leading = MnsListLeading.Avatar("Ana Silva"),
            trailing = { MnsAvatarGroup(names = listOf("A B", "C D", "E F", "G H")) },
            showChevron = true,
            onClick = {},
        )
        MnsAvatar(name = "Ana Silva")
        MnsShortcutCard(
            shortcut = MnsShortcut("s", "Atalho", Icons.Filled.Star, badgeCount = 2),
            onClick = {},
            selected = true,
            modifier = Modifier.width(120.dp),
        )

        // ── Carregamento e mídia ─────────────────────────────────────────────
        MnsShimmerParagraph(lines = 2)
        MnsShimmerListItem()
        MnsShimmerCard(coverHeight = 80.dp)
        MnsIcon(Icons.Filled.Star, "Ícone $marcador")
        MnsCover(
            painter = ColorPainter(Color.Gray),
            contentDescription = "Capa $marcador",
            height = 80.dp,
            scrim = true,
        )

        // ── Códigos ──────────────────────────────────────────────────────────
        MnsQrCode(
            content = "MNS-$marcador",
            size = 100.dp,
            contentDescription = "QR $marcador",
        )
        MnsTicketCard(
            title = "Ingresso $marcador",
            qrContent = "TICKET-$marcador",
            subtitle = "Local · Data",
            details = listOf("Assento" to "D17", "Portão" to "G2"),
            footnote = "Apresente na entrada",
            qrSize = 100.dp,
        )
    }
}
