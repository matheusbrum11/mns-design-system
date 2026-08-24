# Guia de contribuição

Obrigado por querer contribuir com o **MNS Design System**. Este documento
cobre o setup, a arquitetura que você precisa conhecer antes de mexer, e as
regras que a esteira cobra automaticamente.

Leitura de apoio: [Arquitetura](docs/architecture.md) ·
[Tokens](docs/tokens/README.md) · [Testes](docs/testing.md) ·
[CI/CD](docs/ci-cd.md)

---

## 1. Setup

### Requisitos

| Item | Versão |
|---|---|
| JDK | 17 |
| Android SDK | `compileSdk` 35, `build-tools` 35 |
| Gradle | 8.11.1 (via `./gradlew`, não instale à parte) |
| Python | 3.9+ (para o gerador de documentação) |

### Clonando e verificando

```bash
git clone https://github.com/matheusbrum11/mns-design-system.git
cd mns-design-system

# aponte para o seu SDK
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties

# verificação completa (lint + testes + cobertura)
./gradlew qualityCheck
```

Se `qualityCheck` passar, seu ambiente está pronto.

### Rodando o catálogo

```bash
./gradlew :app_demo:installDebug
```

O `:app_demo` é onde você valida visualmente o que fez. Toda contribuição de
componente **precisa** aparecer lá.

---

## 2. Arquitetura em cinco minutos

```
:design_system   ← a biblioteca publicada
:app_demo        ← catálogo vivo e playground de tokens
:benchmark       ← macrobenchmark contra o app_demo
docs/            ← parte gerada do KDoc, parte escrita à mão
tools/           ← gerador de documentação
```

Dentro de `:design_system`:

```
token/  →  theme/  →  component/
```

A dependência vai **sempre para dentro** e nunca volta. `token/` não conhece
`component/`; `component/` não conhece o `:app_demo`.

O detalhamento está em [docs/architecture.md](docs/architecture.md).

---

## 3. As regras não-negociáveis

### 3.1 Nenhum literal dentro de componente

```kotlin
// ✗ nunca
Modifier.padding(16.dp).background(Color(0xFF6255F4))

// ✓ sempre
Modifier
    .padding(MnsTheme.spacing.base)
    .background(MnsTheme.colors.primary)
```

Vale para cor, raio, espaçamento, dimensão, duração, opacidade e espessura.
Se o token que você precisa não existe, **adicione o token** — não o literal.

Cobrado por `MnsThemeMatrixTest`: o catálogo inteiro é renderizado em 6 temas, e
um valor cravado quebra em cinco deles.

### 3.2 API pública é contrato

`explicitApi()` está ligado. Todo símbolo público precisa de visibilidade e
tipo de retorno explícitos, **e de KDoc** — as tabelas da documentação saem
dele.

```kotlin
/**
 * Faz X. Use quando Y.
 *
 * @param foo o que este parâmetro controla.
 * @param bar idem, em uma linha.
 */
@Composable
public fun MnsAlgo(foo: String, bar: Int = 0) { … }
```

### 3.3 Acessibilidade é parte do componente

- `contentDescription` **obrigatório** em componentes só-ícone.
- Semântica correta: `Role.Button`, `Role.Checkbox`, `Role.Tab`,
  `selectableGroup()`, `heading()`, `liveRegion`, `progressBarRangeInfo`.
- Alvo de toque nunca abaixo de `sizing.touchTarget` (48dp).
- Contraste ≥ 4.5:1 entre conteúdo e fundo.

Acessibilidade que depende de o consumidor lembrar não acontece.

### 3.4 Cobertura mínima de 90%

`./gradlew :design_system:koverVerify` roda em todo PR. Abaixo de 90% de linhas,
o build falha.

Não é burocracia: é o que permite trocar tokens com confiança. Um design system
sem cobertura vira um design system que ninguém mexe.

### 3.5 Documentação é gerada, não escrita

As tabelas de `docs/components/**` e `docs/tokens/**` saem do KDoc. Depois de
mexer em qualquer assinatura ou KDoc:

```bash
python3 tools/generate_component_docs.py
```

A CI roda `--check` e falha listando os arquivos defasados.

---

## 4. Adicionando um componente

Um componente novo é **cinco arquivos**, não um.

### Passo 1 — o componente

`design_system/src/main/java/com/mns/designsystem/component/<categoria>/MnsAlgo.kt`

```kotlin
/**
 * Uma frase do que é.
 *
 * Um parágrafo de quando usar — e, se houver, quando **não** usar.
 *
 * ```kotlin
 * MnsAlgo(texto = "exemplo", onClick = ::acao)
 * ```
 *
 * @param texto o que controla.
 * @param variant nível de ênfase — ver [MnsAlgoVariant].
 */
@Composable
public fun MnsAlgo(
    texto: String,
    modifier: Modifier = Modifier,
    variant: MnsAlgoVariant = MnsAlgoVariant.PADRAO,
    enabled: Boolean = true,
) { … }
```

Convenções: prefixo `Mns`; `modifier: Modifier = Modifier` como primeiro
parâmetro opcional; enums para variantes (nunca `Boolean` do tipo
`isPrimary`); slots `@Composable` para conteúdo arbitrário.

### Passo 2 — os testes

`design_system/src/test/.../component/Mns<Categoria>ComponentTest.kt`

Prove: renderização mínima, callback disparado, callback **não** disparado
quando desabilitado, todas as variantes, estados de erro/carregamento, e a
semântica de acessibilidade.

### Passo 3 — a matriz

Adicione uma instância ao `CatalogoCompleto` de `MnsThemeMatrixTest`. Uma linha,
e o componente ganha cobertura em 6 temas.

### Passo 4 — o catálogo do app

`app_demo/src/main/java/com/mns/demo/catalog/entries/<Categoria>Entries.kt`

```kotlin
DemoComponent(
    id = "mns-algo",
    name = "MnsAlgo",
    category = DemoCategory.ACTION,
    summary = "Uma linha dizendo quando usar.",
    docPath = "docs/components/action/mns-algo.md",
    icon = Icons.Filled.Star,
    knobs = listOf(
        DemoKnob.TextKnob("texto", "texto", "O que controla.", "Exemplo"),
        DemoKnob.OptionKnob("variant", "variant", MnsAlgoVariant.entries.map { it.name }),
        DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a interação.", true),
    ),
) { knobs ->
    MnsAlgo(
        texto = knobs.text("texto", "Exemplo"),
        variant = knobs.enum("variant", MnsAlgoVariant.entries.toTypedArray()),
        enabled = knobs.bool("enabled", true),
    )
}
```

> **Regra do catálogo:** se o componente aceita o parâmetro, existe um knob para
> ele. É o que transforma a tela de demonstração em documentação executável em
> vez de um screenshot glorificado.

`DemoCatalogTest` verifica que o `docPath` aponta para um arquivo que existe.

### Passo 5 — a documentação

Registre a página em `PAGES` dentro de `tools/generate_component_docs.py` e
rode o gerador.

### Checklist

- [ ] Componente com KDoc completo (`@param` em tudo que não é óbvio)
- [ ] Zero literais — tudo de `MnsTheme.*`
- [ ] Semântica de acessibilidade
- [ ] Testes de integração cobrindo estados e callbacks
- [ ] Adicionado ao `CatalogoCompleto` da matriz
- [ ] Adicionado ao catálogo do `:app_demo` com knobs
- [ ] Página registrada em `PAGES` e gerador executado
- [ ] `./gradlew qualityCheck` verde

---

## 5. Adicionando um token

Tokens são mais caros que componentes: eles entram no contrato público e todo
tema existente precisa passar a fornecê-los.

1. Adicione o campo ao `data class` correspondente em `token/`, **com KDoc**.
2. Dê um default na fábrica (`MnsColors.light/dark`) ou no preset — nunca
   deixe o consumidor descobrir que faltou.
3. Atualize os três presets em `theme/preset/`.
4. Se for cor, adicione o papel a `MnsColors.byRoleOrNull` e a
   `MnsColors.roleNames`.
5. Se fizer sentido no Design Contract, adicione ao `ContractColors` e ao codec.
6. Teste a derivação em `MnsTokenTest`.
7. Rode o gerador de documentação.

---

## 6. Adicionando um preset de tema

1. Crie `theme/preset/MnsSeuTema.kt` implementando `MnsThemeProvider`,
   documentando **de onde vieram as cores**.
2. Registre em `MnsThemePresets.all`.
3. O preset entra automaticamente na matriz e nos testes de contraste — se
   falhar em 4.5:1, ajuste as cores, não o teste.
4. Adicione o contrato equivalente em `docs/contracts/`.

---

## 7. Pull requests

### Antes de abrir

```bash
./gradlew qualityCheck
python3 tools/generate_component_docs.py
```

### Mensagens de commit

[Conventional Commits](https://www.conventionalcommits.org/pt-br/):

```
feat(component): adiciona MnsTimeline
fix(input): MnsCurrencyField perdia o último dígito ao colar
docs(tokens): esclarece a derivação de containers
test(layout): cobre MnsScaffold sem barra inferior
chore(ci): fixa a versão do runner
```

### Descrição do PR

- O que muda e **por quê**.
- Print (claro e escuro) para mudança visual.
- Menção explícita a qualquer quebra de API pública.

### Regras da `main`

A `main` é protegida por [ruleset](docs/protecao-da-main.md): nenhum push direto,
CI verde obrigatória e, no ruleset completo, aprovação de um CODEOWNER. Vale para
todo mundo — a lista de quem contorna está vazia de propósito.

### O que a CI vai cobrar

| Verificação | Comando |
|---|---|
| Android Lint sem *warnings* | `:design_system:lintRelease` |
| Documentação em dia | `tools/generate_component_docs.py --check` |
| Testes | `:design_system:testDebugUnitTest` |
| Cobertura ≥ 90% | `:design_system:koverVerify` |
| Build | `:design_system:assembleRelease` |

---

## 8. Reportando bugs

Inclua: versão da biblioteca, preset em uso (ou o Design Contract), trecho
mínimo que reproduz, comportamento esperado versus obtido, e print quando for
visual.

---

## 9. Código de conduta

Seja direto sobre o código e gentil com as pessoas. Revisão é sobre a mudança,
nunca sobre quem a escreveu.
