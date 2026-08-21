package com.mns.designsystem.token

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

/**
 * **Semantic tokens de movimento.**
 *
 * Durações e curvas. Além de padronizar, existe por um motivo de
 * acessibilidade: com [reduceMotion] ligado (respeitando a preferência do
 * sistema) todas as durações viram zero e o app para de animar sem que nenhum
 * componente precise saber disso.
 */
@Immutable
public data class MnsMotion(
    /** 0ms — sem animação. */
    val durationInstant: Int,
    /** 100ms — feedback de toque. */
    val durationFast: Int,
    /** 200ms — **padrão**: mudança de estado, cor, elevação. */
    val durationNormal: Int,
    /** 300ms — entrada/saída de container. */
    val durationSlow: Int,
    /** 500ms — transição de tela. */
    val durationSlower: Int,
    /** 1200ms — ciclo do shimmer de carregamento. */
    val durationShimmer: Int,
    /** Curva padrão — desacelera no fim. */
    val easingStandard: Easing,
    /** Curva de entrada — elemento chegando na tela. */
    val easingEnter: Easing,
    /** Curva de saída — elemento deixando a tela. */
    val easingExit: Easing,
    /** Curva com leve overshoot, para ênfase. */
    val easingEmphasized: Easing,
    /** Curva linear — usada por progresso e shimmer. */
    val easingLinear: Easing,
    /** Quando `true`, todas as durações efetivas viram 0. */
    val reduceMotion: Boolean,
) {
    /** Duração efetiva de [millis], já considerando [reduceMotion]. */
    public fun duration(millis: Int): Int = if (reduceMotion) 0 else millis

    /** `tween` padrão do design system, com [reduceMotion] aplicado. */
    public fun <T> tween(
        durationMillis: Int = durationNormal,
        delayMillis: Int = 0,
        easing: Easing = easingStandard,
    ): AnimationSpec<T> = androidx.compose.animation.core.tween(
        durationMillis = duration(durationMillis),
        delayMillis = duration(delayMillis),
        easing = easing,
    )

    public companion object {
        public val Default: MnsMotion = MnsMotion(
            durationInstant = 0,
            durationFast = 100,
            durationNormal = 200,
            durationSlow = 300,
            durationSlower = 500,
            durationShimmer = 1200,
            easingStandard = FastOutSlowInEasing,
            easingEnter = CubicBezierEasing(0f, 0f, 0f, 1f),
            easingExit = CubicBezierEasing(0.3f, 0f, 1f, 1f),
            easingEmphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f),
            easingLinear = LinearEasing,
            reduceMotion = false,
        )

        /** Preset sem movimento, para testes determinísticos e acessibilidade. */
        public val None: MnsMotion = Default.copy(reduceMotion = true)
    }
}
