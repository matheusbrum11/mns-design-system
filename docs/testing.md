# Testes e qualidade

[← Documentação](README.md) · [Arquitetura](architecture.md) · [CI/CD](ci-cd.md) · [Contribuição](../CONTRIBUTING.md)

## A régua

> **Cobertura mínima de 90% de linhas** no módulo `:design_system`, verificada
> por Kover. Abaixo disso o build falha e o merge não acontece.

O valor vive em `gradle.properties` (`mns.coverage.minimum=90`) e é lido pelo
`build.gradle.kts` do módulo — não existe número mágico espalhado.

Cobertura atual: **96,4% de linhas** (4038/4189).

---

## Rodando

```bash
# Suíte completa do design system (integração + unidade)
./gradlew :design_system:testDebugUnitTest

# Um arquivo específico
./gradlew :design_system:testDebugUnitTest --tests "*MnsButtonTest*"

# Suíte do app de demonstração
./gradlew :app_demo:testDebugUnitTest

# Relatório de cobertura (HTML)
./gradlew :design_system:koverHtmlReportDefault
open design_system/build/reports/kover/htmlDefault/index.html

# Verificação da régua de 90% — é o que a CI roda
./gradlew :design_system:koverVerify

# Tudo que a esteira exige antes de um merge
./gradlew qualityCheck
```

### Testes instrumentados (dispositivo/emulador)

```bash
./gradlew :design_system:connectedDebugAndroidTest
```

Complementam a suíte de integração com **renderização real de pixels** — o que
o Robolectric não faz.

---

## Como a suíte é organizada

| Arquivo | O que cobre |
|---|---|
| `MnsComposeTest` | Base: regra de composição ancorada em `ComponentActivity`, relógio congelado, helpers `settle()` e `assertShown()` |
| `token/MnsTokenTest` | Derivações, escalas, lookups e contraste WCAG — JVM pura |
| `format/MnsFormatterTest` | Moeda, percentual, número compacto, máscaras e mapeamento de cursor |
| `contract/MnsDesignContractTest` | Parse, validação, derivação, ciclo de exportação/reimportação e os contratos de exemplo do repositório |
| `theme/MnsThemeTest` | Presets, `MnsThemeProvider`, `CompositionLocal`s e **contraste mínimo de todos os presets** |
| `component/Mns*ComponentTest` | Comportamento de cada família de componentes |
| `MnsThemeMatrixTest` | O catálogo inteiro renderizado em 6 temas |

---

## Decisões da suíte que valem explicação

### Robolectric, não emulador

A suíte precisa rodar em **todo pull request**. Emulador em CI é lento e
instável. O que se perde — renderização real — é coberto pelos testes de
`androidTest`, que rodam sob demanda.

### Relógio congelado (`autoAdvance = false`)

Vários componentes têm animação infinita (shimmer, progresso indeterminado).
Com o relógio em auto-avanço, qualquer `waitForIdle` travaria para sempre: a
composição nunca fica ociosa. Com o relógio congelado, o teste controla o tempo
e as asserções ficam determinísticas.

Depois de uma interação, chame `settle()` para processar a recomposição
pendente; para animações determinadas, `advance(millis)`.

### `assertShown()` em vez de `assertIsDisplayed()` para texto

O Robolectric não faz layout de fonte real: uma string pode medir 0px de
largura. `assertIsDisplayed()` sobre um nó de texto testaria o *stub* de fonte,
não o componente. `assertShown()` afirma existência na árvore de semântica —
geometria de verdade é verificada em `androidTest`.

### Regra ancorada em `ComponentActivity`

`createComposeRule()` sob Robolectric usa uma janela *wrap-content*: qualquer
componente com `fillMaxWidth` cai fora dela. `createAndroidComposeRule<ComponentActivity>()`
dá uma janela de tamanho real.

---

## Matriz tema × componente

`MnsThemeMatrixTest` renderiza **todo** o catálogo em cada um dos 3 presets,
claro e escuro — 6 composições completas.

É o teste que impede a regressão mais cara de um design system: alguém deixar
um `Color(0xFF...)` ou um `16.dp` cravado dentro de um componente. Um valor
hard-coded não quebra o preset em que foi escrito; ele quebra os outros cinco,
e é aqui que isso aparece.

**Ao adicionar um componente novo, adicione-o também ao `CatalogoCompleto`** do
arquivo. É uma linha, e rende cobertura em 6 temas de graça.

---

## Acessibilidade como teste, não como checklist

A suíte verifica automaticamente:

- **Contraste WCAG** — todos os presets, `textPrimary/surface` e
  `onPrimary/primary` ≥ 4.5:1 (`MnsThemeTest`).
- **Alvo de toque** — `sizing.touchTarget ≥ 48dp` (`MnsTokenTest`).
- **Semântica de estado** — `assertIsOn/Off`, `assertIsSelected`,
  `assertIsNotEnabled` nos componentes interativos.
- **`contentDescription`** presente em ícones, badges, avatares e QR Codes.
- **`progressBarRangeInfo`** nos indicadores, determinado e indeterminado.

---

## Cobertura: o que fica de fora

O filtro do Kover exclui:

- `@Preview` e `ComposableSingletons*` — código declarativo sem lógica;
- `BuildConfig` e fábricas geradas.

Testes de unidade da variante `release` estão **desligados**: executariam o
mesmo código-fonte do `debug`, sem as dependências `debugImplementation` que a
suíte de composição exige. Seria uma suíte duplicada que falha por
configuração, não por regressão.

---

## Escrevendo um teste novo

```kotlin
class MeuComponenteTest : MnsComposeTest() {

    @Test
    fun `dispara a acao quando habilitado`() {
        var cliques = 0
        setThemedContent {
            MeuComponente(onClick = { cliques++ })
        }
        composeRule.onNodeWithText("Rótulo").performClick()
        settle()
        assertThat(cliques).isEqualTo(1)
    }
}
```

Checklist do que um componente novo precisa provar:

- [ ] Renderiza com os parâmetros mínimos
- [ ] Dispara o callback quando habilitado
- [ ] **Não** dispara quando `enabled = false`
- [ ] Todas as variantes de enum renderizam
- [ ] Estados de erro/carregamento aparecem
- [ ] `contentDescription` está na semântica
- [ ] Foi adicionado ao `CatalogoCompleto` da matriz
- [ ] Foi adicionado ao catálogo do `:app_demo`
