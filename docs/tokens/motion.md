# Tokens de movimento

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Durações e curvas. Além de padronizar, existe por um motivo de acessibilidade: com `reduceMotion` ligado, **todas** as durações efetivas viram zero e o app para de animar sem que nenhum componente precise saber disso.

```kotlin
val spec = MnsTheme.motion.tween<Float>(MnsTheme.motion.durationNormal)
```

Componentes do MNS **nunca** chamam `tween(...)` diretamente — sempre `MnsTheme.motion.tween(...)`, que é o que aplica `reduceMotion`.

## `MnsMotion`

**Semantic tokens de movimento.**

Durações e curvas. Além de padronizar, existe por um motivo de
acessibilidade: com `reduceMotion` ligado (respeitando a preferência do
sistema) todas as durações viram zero e o app para de animar sem que nenhum
componente precise saber disso.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `/** 0ms — sem animação. */
    val durationInstant` | `Int` | — | — |
| `/** 100ms — feedback de toque. */
    val durationFast` | `Int` | — | — |
| `/** 200ms — **padrão**` | `mudança de estado` | — | — |
| `elevação. */
    val durationNormal` | `Int` | — | — |
| `/** 300ms — entrada/saída de container. */
    val durationSlow` | `Int` | — | — |
| `/** 500ms — transição de tela. */
    val durationSlower` | `Int` | — | — |
| `/** 1200ms — ciclo do shimmer de carregamento. */
    val durationShimmer` | `Int` | — | — |
| `/** Curva padrão — desacelera no fim. */
    val easingStandard` | `Easing` | — | — |
| `/** Curva de entrada — elemento chegando na tela. */
    val easingEnter` | `Easing` | — | — |
| `/** Curva de saída — elemento deixando a tela. */
    val easingExit` | `Easing` | — | — |
| `para ênfase. */
    val easingEmphasized` | `Easing` | — | — |
| `/** Curva linear — usada por progresso e shimmer. */
    val easingLinear` | `Easing` | — | — |
| `todas as durações efetivas viram 0. */
    val reduceMotion` | `Boolean` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsMotion.kt`
