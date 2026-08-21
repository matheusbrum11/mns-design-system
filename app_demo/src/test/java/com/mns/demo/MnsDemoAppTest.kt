package com.mns.demo

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth.assertThat
import com.mns.demo.playground.DemoDensity
import com.mns.demo.playground.ThemeController
import com.mns.designsystem.theme.preset.MnsThemePresets
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Testes de integração de ponta a ponta do app de demonstração. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MnsDemoAppTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun freezeClock() {
        composeRule.mainClock.autoAdvance = false
    }

    private fun settle(frames: Int = 3) {
        repeat(frames) { composeRule.mainClock.advanceTimeByFrame() }
        composeRule.waitForIdle()
    }

    @Test
    fun `home lista os componentes da primeira categoria`() {
        composeRule.setContent { MnsDemoApp() }
        settle()
        composeRule.onNodeWithText("MNS Design System").assertExists()
        composeRule.onNodeWithText("MnsButton").assertExists()
    }

    @Test
    fun `busca filtra o catalogo atravessando categorias`() {
        composeRule.setContent { MnsDemoApp() }
        settle()
        composeRule.onNodeWithText("Buscar componente").performTextInput("currency")
        settle()
        composeRule.onNodeWithText("MnsCurrencyField").assertExists()
        composeRule.onNodeWithText("MnsCurrencyText").assertExists()
    }

    @Test
    fun `abrir um componente mostra preview e painel de parametros`() {
        composeRule.setContent { MnsDemoApp() }
        settle()
        composeRule.onNodeWithText("MnsButton").performClick()
        settle()
        composeRule.onNodeWithText("Preview").assertExists()
        composeRule.onNodeWithText("Parâmetros").assertExists()
        composeRule.onNodeWithContentDescription("Voltar").performClick()
        settle()
        composeRule.onNodeWithText("MNS Design System").assertExists()
    }

    @Test
    fun `tela de tokens abre e alterna o preset`() {
        val theme = ThemeController()
        composeRule.setContent { MnsDemoApp(themeController = theme) }
        settle()
        composeRule.onNodeWithContentDescription("Abrir tokens do tema").performClick()
        settle()
        composeRule.onNodeWithText("Pastel Glass").performClick()
        settle()
        assertThat(theme.provider.id).isEqualTo(MnsThemePresets.PastelGlass.id)
    }

    @Test
    fun `alternar modo escuro atualiza o controlador de tema`() {
        val theme = ThemeController()
        composeRule.setContent { MnsDemoApp(themeController = theme) }
        settle()
        composeRule.onNodeWithContentDescription("Usar tema escuro").performClick()
        settle()
        assertThat(theme.darkMode).isTrue()
    }

    @Test
    fun `exportar contrato produz json com a identidade do preset`() {
        val theme = ThemeController()
        val json = theme.exportContractJson()
        assertThat(json).contains("indigo-ticket")
        assertThat(json).contains("#6255F4")
        assertThat(json).contains("playground")
    }

    @Test
    fun `controlador aplica sobrescritas de token`() {
        val theme = ThemeController()
        val original = theme.spec
        theme.density = DemoDensity.COMPACT
        theme.typographyScale = 1.2f
        theme.flatElevation = true
        theme.reduceMotion = true
        theme.pillButtons = true
        theme.baseRadius = 24f
        val ajustado = theme.spec

        assertThat(ajustado.spacing.base.value).isLessThan(original.spacing.base.value)
        assertThat(ajustado.typography.bodyMedium.fontSize.value)
            .isGreaterThan(original.typography.bodyMedium.fontSize.value)
        assertThat(ajustado.elevation.shadowAlpha).isEqualTo(0f)
        assertThat(ajustado.motion.reduceMotion).isTrue()
        assertThat(ajustado.shapes.buttonRadius).isNull()
        assertThat(ajustado.shapes.baseRadius.value).isEqualTo(24f)
    }

    @Test
    fun `reset devolve o controlador ao estado do preset`() {
        val theme = ThemeController()
        theme.density = DemoDensity.COMFORTABLE
        theme.pillButtons = true
        theme.reset()
        assertThat(theme.density).isEqualTo(DemoDensity.DEFAULT)
        assertThat(theme.pillButtons).isFalse()
        assertThat(theme.baseRadius).isEqualTo(ThemeController.DEFAULT_RADIUS_SENTINEL)
    }

    @Test
    fun `saver preserva o estado do tema`() {
        val theme = ThemeController().apply {
            selectProvider(MnsThemePresets.MonoEvents)
            darkMode = true
            density = DemoDensity.COMPACT
            typographyScale = 1.1f
            reduceMotion = true
            flatElevation = true
            pillButtons = true
            baseRadius = 18f
        }
        val salvo = with(ThemeController.Saver) {
            androidx.compose.runtime.saveable.SaverScope { true }.save(theme)
        }
        val restaurado = ThemeController.Saver.restore(salvo!!)!!
        assertThat(restaurado.provider.id).isEqualTo("mono-events")
        assertThat(restaurado.darkMode).isTrue()
        assertThat(restaurado.density).isEqualTo(DemoDensity.COMPACT)
        assertThat(restaurado.baseRadius).isEqualTo(18f)
    }
}
