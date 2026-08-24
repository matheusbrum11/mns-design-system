# MnsScaffold

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsScaffold`

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `topBar` | `(@Composable () -> Unit)?` | `null` | — |
| `bottomBar` | `(@Composable () -> Unit)?` | `null` | — |
| `floatingActionButton` | `(@Composable () -> Unit)?` | `null` | — |
| `fabPosition` | `MnsFabPosition` | `MnsFabPosition.END` | — |
| `banner` | `(@Composable () -> Unit)?` | `null` | — |
| `containerColor` | `Color` | `MnsTheme.colors.background` | Cor de fundo do componente. |
| `contentColor` | `Color` | `MnsTheme.colors.onBackground` | Cor herdada por textos e ícones filhos. |
| `contentWindowInsetsPadding` | `Boolean` | `true` | — |
| `fabMargin` | `Dp` | `MnsTheme.spacing.base` | — |
| `content` | `@Composable (PaddingValues) -> Unit` | — | Slot de conteúdo do componente. |

## `MnsScreenColumn`

Coluna de conteúdo com as margens de tela do design system já aplicadas.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | — |
| `verticalArrangement` | `Arrangement.Vertical` | `Arrangement.spacedBy(MnsTheme.spacing.base)` | — |
| `horizontalAlignment` | `Alignment.Horizontal` | `Alignment.Start` | — |
| `content` | `@Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit` | — | Slot de conteúdo do componente. |

## `MnsFabPosition`

Onde o FAB é ancorado dentro do `MnsScaffold`.

| Valor | Significado |
|---|---|
| `END` | Canto inferior direito — o padrão. |
| `CENTER` | Centralizado acima da barra inferior. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsScaffold.kt`
