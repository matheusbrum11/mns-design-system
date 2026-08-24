package com.mns.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Mede a fluidez da rolagem da lista de componentes e da tela de um componente.
 *
 * [FrameTimingMetric] reporta os percentis de duração de frame; o alvo é
 * P90 abaixo de 16,6ms (60fps). Componentes que recalculam token a cada frame
 * — em vez de ler de um `CompositionLocal` estático — aparecem aqui primeiro.
 */
@RunWith(AndroidJUnit4::class)
class CatalogScrollBenchmark {

    @get:Rule
    val benchmarkRule: MacrobenchmarkRule = MacrobenchmarkRule()

    @Test
    fun rolarListaDeComponentes() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        iterations = ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = { startActivityAndWait() },
    ) {
        val lista = device.wait(Until.findObject(By.scrollable(true)), TIMEOUT_MS)
            ?: return@measureRepeated
        lista.setGestureMargin(device.displayWidth / 5)
        repeat(3) {
            lista.fling(Direction.DOWN)
            device.waitForIdle()
        }
        lista.fling(Direction.UP)
        device.waitForIdle()
    }

    @Test
    fun abrirTelaDeComponente() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        iterations = ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = { startActivityAndWait() },
    ) {
        device.wait(Until.findObject(By.textContains("MnsButton")), TIMEOUT_MS)?.click()
        device.waitForIdle()
        device.pressBack()
        device.waitForIdle()
    }

    private companion object {
        const val TARGET_PACKAGE = "com.mns.demo"
        const val ITERATIONS = 5
        const val TIMEOUT_MS = 5_000L
    }
}
