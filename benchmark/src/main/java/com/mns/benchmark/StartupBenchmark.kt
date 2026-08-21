package com.mns.benchmark

import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Mede o tempo de abertura do `app_demo`.
 *
 * O número que importa aqui é o **custo de inicialização do design system**:
 * quantos milissegundos o consumidor da biblioteca paga só por montar o
 * `MnsTheme` e a primeira tela. Regressão aqui costuma vir de token calculado
 * em tempo de composição em vez de derivado uma vez.
 *
 * Requer um dispositivo ou emulador conectado:
 * ```bash
 * ./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest
 * ```
 */
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {

    @get:Rule
    val benchmarkRule: MacrobenchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupFrio() = medir(StartupMode.COLD)

    @Test
    fun startupMorno() = medir(StartupMode.WARM)

    private fun medir(mode: StartupMode) = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        iterations = ITERATIONS,
        startupMode = mode,
    ) {
        pressHome()
        startActivityAndWait()
    }

    private companion object {
        const val TARGET_PACKAGE = "com.mns.demo"
        const val ITERATIONS = 10
    }
}
