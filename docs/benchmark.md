# Benchmark

[← Documentação](README.md) · [Testes](testing.md)

O módulo `:benchmark` mede o que a suíte de integração não mede: **quanto custa,
em milissegundos e em frames, usar o design system**.

## Requisito

Macrobenchmark precisa de **dispositivo físico ou emulador com API 28+**. Não
roda em JVM, e por isso não faz parte da esteira de PR — é executado sob
demanda, antes de um release ou ao investigar uma regressão.

Dispositivo físico dá números bem mais confiáveis que emulador.

---

## Rodando

```bash
# Suíte completa
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest

# Só o startup
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.mns.benchmark.StartupBenchmark
```

Os resultados saem no console e em JSON dentro de
`benchmark/build/outputs/connected_android_test_additional_output/`.

---

## O que é medido

### `StartupBenchmark`

`StartupTimingMetric` em modo frio e morno.

O número que importa é o **custo de inicialização do design system**: quantos
milissegundos o consumidor paga só por montar o `MnsTheme` e a primeira tela.
Regressão aqui costuma vir de token calculado em tempo de composição em vez de
derivado uma vez.

### `CatalogScrollBenchmark`

`FrameTimingMetric` ao rolar a lista de componentes e ao abrir a tela de um
componente.

Alvo: **P90 abaixo de 16,6ms** (60fps). Componentes que recalculam token a cada
frame — em vez de ler de um `CompositionLocal` estático — aparecem aqui
primeiro.

---

## Configuração da variante

O `:app_demo` declara uma build type `benchmarkRelease`: parte do `release`,
mas sem ofuscação e com *profiling* habilitado, para que os traces sejam
legíveis. O manifesto do app declara `<profileable android:shell="true" />`.

O `:benchmark` usa `targetProjectPath = ":app_demo"` e
`self-instrumenting = true`: o APK de teste é separado do APK medido, que é o
que evita o instrumentador contaminar a medição.

---

## Interpretando

| Sintoma | Suspeita mais provável |
|---|---|
| Startup frio subiu | Trabalho de inicialização no `MnsThemeSpec` — derive uma vez, não por composição |
| P90 de frame acima de 16,6ms na rolagem | Alocação por item de lista, ou `Modifier.composed` em caminho quente |
| P99 com pico isolado | GC durante a rolagem — procure objeto criado dentro de `@Composable` sem `remember` |
| Abrir a tela de componente ficou lento | QR Code sendo codificado a cada recomposição em vez de por `remember` |

---

## Baseline Profile

O plugin `androidx.baselineprofile` já está declarado no catálogo de versões e
pode ser ativado quando fizer sentido: um baseline profile costuma cortar 20–30%
do startup frio, pré-compilando os caminhos de composição mais quentes.

Enquanto não for gerado, o `:app_demo` já inclui `androidx.profileinstaller`,
que é o que permite ao dispositivo instalar o perfil quando ele existir.
