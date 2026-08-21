package com.mns.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.mns.designsystem.theme.MnsTheme
import com.mns.designsystem.theme.MnsThemeSpec
import com.mns.designsystem.theme.preset.MnsIndigoTicket
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Base dos testes de integração de composição.
 *
 * Duas decisões que valem explicação:
 *
 * 1. **`autoAdvance = false`.** Vários componentes do MNS têm animação infinita
 *    (shimmer, progresso indeterminado). Com o relógio em auto-avanço, qualquer
 *    `waitForIdle` travaria para sempre, porque a composição nunca fica ociosa.
 *    Desligando, o teste controla o tempo e as asserções são determinísticas.
 *
 * 2. **Robolectric em vez de emulador.** A suíte precisa rodar na esteira de CI
 *    a cada pull request. Emulador em CI é lento e instável; o que perdemos
 *    (renderização real de pixels) é coberto pelos testes instrumentados de
 *    `androidTest`, que rodam sob demanda.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
public abstract class MnsComposeTest {

    /**
     * Regra ancorada em uma `ComponentActivity` real (e não em
     * `createComposeRule()`): sob Robolectric, a janela avulsa do compose rule é
     * wrap-content, e qualquer componente com `fillMaxWidth` cai fora dela —
     * fazendo `assertIsDisplayed()` falhar por motivo de ambiente, não de código.
     */
    @get:Rule
    public val composeRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createAndroidComposeRule()

    @Before
    public fun freezeClock() {
        composeRule.mainClock.autoAdvance = false
    }

    /**
     * Monta [content] dentro de um [MnsTheme].
     *
     * @param spec tokens usados. Default: preset claro padrão.
     */
    protected fun setThemedContent(
        spec: MnsThemeSpec = MnsIndigoTicket.light,
        content: @Composable () -> Unit,
    ) {
        composeRule.setContent {
            MnsTheme(spec = spec, content = content)
        }
    }

    /** Avança o relógio de composição em [millis], para animações determinadas. */
    protected fun advance(millis: Long = 300L) {
        composeRule.mainClock.advanceTimeBy(millis)
    }

    /**
     * Afirma que o nó existe na árvore de semântica.
     *
     * Usamos isto no lugar de `assertIsDisplayed()` para nós de **texto**: o
     * Robolectric não faz layout de fonte real, então uma string pode medir 0px
     * de largura e `assertIsDisplayed()` falharia por causa do ambiente, não do
     * componente. Geometria de verdade é verificada nos testes instrumentados
     * de `androidTest`, que rodam com renderização real.
     */
    protected fun SemanticsNodeInteraction.assertShown(): SemanticsNodeInteraction =
        assertExists()

    /**
     * Processa a recomposição pendente depois de uma interação.
     *
     * Com o relógio congelado (ver acima), uma mudança de estado disparada por
     * `performClick`/`performTextInput` só aparece na árvore após um frame.
     * Chame [settle] entre a ação e a asserção — é o equivalente determinístico
     * do `waitForIdle` que usaríamos com auto-avanço ligado.
     */
    protected fun settle(frames: Int = 2) {
        repeat(frames) { composeRule.mainClock.advanceTimeByFrame() }
        composeRule.waitForIdle()
    }
}
