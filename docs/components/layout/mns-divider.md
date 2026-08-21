# MnsDivider

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsDivider`

Separador horizontal.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `thickness` | `Dp` | `MnsTheme.borders.hairline` | espessura; default `borders.hairline`. |
| `color` | `Color` | `MnsTheme.colors.outlineVariant` | cor; default `colors.outlineVariant`. |
| `inset` | `Dp` | `MnsTheme.spacing.none` | recuo lateral. Em listas com avatar, alinhe o recuo ao início do texto (não à borda da tela) para que a linha "amarre" a coluna de conteúdo. |

## `MnsVerticalDivider`

Separador vertical — para dividir colunas dentro de uma `Row`.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `thickness` | `Dp` | `MnsTheme.borders.hairline` | — |
| `color` | `Color` | `MnsTheme.colors.outlineVariant` | Cor do conteúdo. `Color.Unspecified` herda do contexto. |
| `inset` | `Dp` | `MnsTheme.spacing.none` | — |

## `MnsLabeledDivider`

Separador com rótulo centralizado — o clássico "ou".

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `text` | `String` | — | rótulo exibido no meio da linha. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `color` | `Color` | `MnsTheme.colors.outlineVariant` | Cor do conteúdo. `Color.Unspecified` herda do contexto. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsDivider.kt`
