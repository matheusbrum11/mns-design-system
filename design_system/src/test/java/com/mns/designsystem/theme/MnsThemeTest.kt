package com.mns.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.preset.MnsIndigoTicket
import com.mns.designsystem.theme.preset.MnsMonoEvents
import com.mns.designsystem.theme.preset.MnsPastelGlass
import com.mns.designsystem.theme.preset.MnsThemePresets
import com.mns.designsystem.token.contrastRatio
import org.junit.Test

/** Testes da camada de tema e dos presets. */
class MnsThemeTest : MnsComposeTest() {

    @Test
    fun `catalogo de presets expoe os tres temas de referencia`() {
        assertThat(MnsThemePresets.all).hasSize(3)
        assertThat(MnsThemePresets.all.map { it.id })
            .containsExactly("indigo-ticket", "mono-events", "pastel-glass")
        assertThat(MnsThemePresets.byId("mono-events")).isEqualTo(MnsMonoEvents)
        assertThat(MnsThemePresets.byId("inexistente")).isNull()
    }

    @Test
    fun `todos os presets suportam modo escuro`() {
        MnsThemePresets.all.forEach { preset ->
            assertThat(preset.supportsDarkMode).isTrue()
            assertThat(preset.specFor(darkMode = true).isDark).isTrue()
            assertThat(preset.specFor(darkMode = false).isDark).isFalse()
        }
    }

    @Test
    fun `provider simples sem dark devolve sempre a variante clara`() {
        val provider = MnsSimpleThemeProvider(
            id = "unico",
            displayName = "Único",
            light = MnsIndigoTicket.light,
        )
        assertThat(provider.supportsDarkMode).isFalse()
        assertThat(provider.specFor(darkMode = true)).isEqualTo(MnsIndigoTicket.light)
    }

    @Test
    fun `presets respeitam contraste minimo de texto sobre superficie`() {
        MnsThemePresets.all.forEach { preset ->
            listOf(preset.light, preset.dark).forEach { spec ->
                val razao = contrastRatio(spec.colors.textPrimary, spec.colors.surface)
                assertThat(razao).isAtLeast(4.5f)
            }
        }
    }

    @Test
    fun `presets respeitam contraste minimo de conteudo sobre a primaria`() {
        MnsThemePresets.all.forEach { preset ->
            listOf(preset.light, preset.dark).forEach { spec ->
                val razao = contrastRatio(spec.colors.onPrimary, spec.colors.primary)
                assertThat(razao).isAtLeast(4.5f)
            }
        }
    }

    @Test
    fun `preset mono usa botoes em pilula e o indigo usa raio proprio`() {
        assertThat(MnsMonoEvents.light.shapes.buttonRadius).isNull()
        assertThat(MnsPastelGlass.light.shapes.buttonRadius).isNull()
        assertThat(MnsIndigoTicket.light.shapes.buttonRadius).isEqualTo(16.dp)
        assertThat(MnsIndigoTicket.light.colors.primary).isEqualTo(Color(0xFF6255F4))
    }

    @Test
    fun `MnsTheme publica os tokens do spec para a subarvore`() {
        var capturado: MnsThemeSpec? = null
        setThemedContent(spec = MnsPastelGlass.light) {
            capturado = MnsTheme.spec
            Column {
                MnsText("token-check", style = MnsTheme.typography.titleLarge)
            }
        }
        composeRule.onNodeWithText("token-check").assertShown()
        assertThat(capturado?.id).isEqualTo("pastel-glass")
    }

    @Test
    fun `acessores do MnsTheme devolvem o mesmo spec fornecido`() {
        val esperado = MnsMonoEvents.dark
        setThemedContent(spec = esperado) {
            assertThat(MnsTheme.colors).isEqualTo(esperado.colors)
            assertThat(MnsTheme.typography).isEqualTo(esperado.typography)
            assertThat(MnsTheme.shapes).isEqualTo(esperado.shapes)
            assertThat(MnsTheme.spacing).isEqualTo(esperado.spacing)
            assertThat(MnsTheme.sizing).isEqualTo(esperado.sizing)
            assertThat(MnsTheme.elevation).isEqualTo(esperado.elevation)
            assertThat(MnsTheme.borders).isEqualTo(esperado.borders)
            assertThat(MnsTheme.opacity).isEqualTo(esperado.opacity)
            assertThat(MnsTheme.motion).isEqualTo(esperado.motion)
            MnsText("ok")
        }
        composeRule.onNodeWithText("ok").assertShown()
    }

    @Test
    fun `MnsTheme com provider resolve a variante pelo parametro darkTheme`() {
        composeRule.setContent {
            MnsTheme(provider = MnsIndigoTicket, darkTheme = true) {
                MnsText(if (MnsTheme.spec.isDark) "escuro" else "claro")
            }
        }
        composeRule.onNodeWithText("escuro").assertShown()
    }
}
