# Arquitetura

[← Documentação](README.md) · [Tokens](tokens/README.md) · [Contribuição](../CONTRIBUTING.md)

## Os módulos

```
mns-design-system/
├── design_system/   ← a biblioteca publicada no Maven
├── app_demo/        ← catálogo vivo e playground de tokens
├── benchmark/       ← macrobenchmark contra o app_demo
├── docs/            ← documentação (parte gerada, parte escrita)
└── tools/           ← gerador de documentação
```

| Módulo | Tipo | Publicado? | Depende de |
|---|---|---|---|
| `:design_system` | `com.android.library` | **sim** | Compose, kotlinx-serialization, ZXing |
| `:app_demo` | `com.android.application` | não | `:design_system` |
| `:benchmark` | `com.android.test` | não | `:app_demo` (via `targetProjectPath`) |

A seta de dependência aponta **sempre para dentro**: a biblioteca não conhece o
app, o app não conhece o benchmark. Não existe caminho pelo qual uma decisão de
produto vaze para dentro do design system.

---

## Camadas dentro de `:design_system`

```
com.mns.designsystem
├── token/        ← reference + semantic tokens (sem Compose UI, só dados)
├── theme/        ← MnsThemeSpec, MnsThemeProvider, MnsTheme, presets
├── foundation/   ← Modifiers e indication compartilhados
├── format/       ← formatação de moeda, percentual, máscaras
├── contract/     ← Design Contract (JSON ⇄ tokens)
└── component/    ← os componentes, agrupados por função
    ├── action/  input/  text/  status/
    ├── layout/  list/   shortcut/
    └── loading/ code/   media/
```

**A dependência é de cima para baixo e nunca volta.** `component/` lê `theme/`,
que lê `token/`. Nenhum token conhece um componente; nenhum componente conhece
outro componente de uma categoria "acima" dele.

### Por que `token/` não depende de Compose UI

`token/` importa apenas `androidx.compose.runtime` (para `@Immutable`),
`ui.graphics.Color`, `ui.unit.Dp` e `ui.text`. Nada de layout, nada de
`Modifier`. Isso mantém a camada testável em JVM pura e deixa aberta a porta
para gerar tokens fora do Android (Web, iOS) a partir do mesmo Design Contract.

---

## As regras que sustentam o desacoplamento

### 1. Nenhum literal dentro de componente

Nenhum `Color(0xFF...)`, `16.dp`, `14.sp`, `300` (ms) ou `0.38f` dentro de
`component/`. Tudo vem de `MnsTheme.*`.

Cobrado por: [teste de matriz tema × componente](testing.md#matriz-tema--componente)
e pelo *code review*.

### 2. `explicitApi()` ligado

Todo símbolo público precisa de visibilidade e tipo de retorno declarados.
É o que impede a API pública de crescer por acidente e mantém o contrato
estável entre versões.

### 3. Tipos selados em vez de parâmetros nuláveis combináveis

`MnsListAction` recebe `leading: MnsListLeading`, um tipo selado, em vez de
`avatar: String?`, `icon: ImageVector?` e `thumbnail: Painter?`. Com três
nuláveis, "avatar **e** ícone ao mesmo tempo" é um estado representável — e
alguém vai representá-lo.

### 4. Semântica de acessibilidade é parte do componente

`contentDescription` obrigatório em `MnsIconButton`; `heading()` em
`MnsHeading`; `liveRegion` em `MnsAlert`; `progressBarRangeInfo` nos
indicadores; `selectableGroup()` nas barras de aba. Acessibilidade que depende
de o consumidor lembrar não acontece.

### 5. Nenhuma dependência pesada

A biblioteca **não** depende de `material-icons-extended` (≈2 mil vetores).
Os poucos ícones que os componentes internos exigem e que não estão no
`material-icons-core` moram em `MnsIcons`, desenhados à mão. O `:app_demo` usa
o pacote completo à vontade — ele não é publicado.

A única dependência de terceiros é `com.google.zxing:core`, isolada em
`MnsQrEncoder`. `MnsQrCode` fala apenas `MnsQrMatrix`, então trocar de
codificador não toca em nenhum componente.

---

## Fluxo de tema em tempo de execução

```
MnsThemeProvider          MnsThemeSpec              CompositionLocals
(preset ou seu)     →     (tokens resolvidos)  →    LocalMnsColors
                                                    LocalMnsTypography
                                                    LocalMnsShapes
                                                    …
                                                          ↓
                                                    MnsTheme.colors.primary
                                                          ↓
                                                       MnsButton
```

Os `CompositionLocal`s são **estáticos** (`staticCompositionLocalOf`): tokens
mudam raramente — na troca de tema inteiro — e o custo de rastrear leitura
individual em cada componente não se pagaria.

O escape hatch é o parâmetro `spec` de `MnsTheme`: passe um `MnsThemeSpec`
pronto e o provider é ignorado. É por ele que o playground do `:app_demo` edita
tokens ao vivo.

---

## Fluxo do Design Contract

```
   print de design
         │
         ▼
  agente mns-design-contract       (.claude/agents/)
         │
         ▼
  design-contract.json             docs/contracts/
         │
         ▼
  MnsDesignContractCodec.toProvider(json)
         │
         ▼
  MnsThemeProvider → MnsTheme → app inteiro
```

E o caminho de volta: a tela **Tokens** do `:app_demo` exporta o spec editado
como JSON via `MnsDesignContractCodec.fromThemeSpec(...)`, fechando o ciclo
design → código → design. Ver [Design Contract](design-contract.md).

---

## Estratégia de teste

| Camada | Onde | Como roda |
|---|---|---|
| Tokens, formatadores, contrato | `src/test` | JVM pura |
| Componentes (integração) | `src/test` | Robolectric + Compose UI Test |
| Componentes (renderização real) | `src/androidTest` | dispositivo/emulador |
| Performance | `:benchmark` | dispositivo/emulador |

A régua de **90% de cobertura de integração** é verificada por Kover e trava o
merge. Detalhes em [Testes e qualidade](testing.md).
