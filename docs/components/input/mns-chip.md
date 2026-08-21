# MnsChip

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsChip`

Chip de filtro/seleção.

É o componente do "travel ✕" e dos filtros de categoria dos designs de
referência. Diferente de um botão, um chip representa **um valor**, não uma
ação — por isso tem estado selecionado e semântica de seleção.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `label` | `String` | — | texto do chip. |
| `selected` | `Boolean` | — | estado de seleção. Controla cor de fundo e de conteúdo. |
| `onClick` | `() -> Unit` | — | chamado ao tocar no corpo do chip. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `leadingIcon` | `ImageVector?` | `null` | ícone antes do rótulo (categoria, avatar). |
| `onDismiss` | `(() -> Unit)?` | `null` | quando não-nulo, exibe um "✕" ao final que remove o chip. O toque no ✕ **não** dispara `onClick`. |
| `enabled` | `Boolean` | `true` | desabilita interação e reduz opacidade. |
| `height` | `Dp` | `MnsTheme.sizing.chipHeight` | altura do chip. |
| `shape` | `Shape` | `MnsTheme.shapes.chip` | Forma do componente. Use um papel de `MnsTheme.shapes`. |

## `MnsChipRow`

Linha rolável de chips de filtro.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `options` | `List<String>` | — | rótulos das opções. |
| `selectedIndices` | `Set<Int>` | — | índices selecionados. |
| `onToggle` | `(Int) -> Unit` | — | chamado com o índice tocado; cabe ao chamador decidir se o filtro é de seleção única ou múltipla. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsChip.kt`
