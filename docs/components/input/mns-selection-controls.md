# MnsCheckbox · MnsRadioButton · MnsSwitch

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCheckbox`

Caixa de seleção com rótulo opcional.

O alvo de toque cobre o rótulo inteiro, não apenas o quadrado — 20dp de
quadrado é alvo pequeno demais e a regra de 48dp vale para a linha toda.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `state` | `MnsToggleState` | — | estado atual — ver `MnsToggleState`. |
| `onStateChange` | `(MnsToggleState) -> Unit` | — | chamado com o próximo estado ao tocar. `INDETERMINATE` sempre avança para `CHECKED`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `label` | `String?` | `null` | rótulo à direita da caixa. |
| `description` | `String?` | `null` | linha secundária abaixo do rótulo. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |

## `MnsRadioButton`

Botão de opção. Use dentro de um grupo com `Modifier.selectableGroup` para
que leitores de tela anunciem "1 de 3".

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `selected` | `Boolean` | — | se esta opção é a escolhida. |
| `onSelect` | `() -> Unit` | — | chamado ao tocar. Não é chamado se já estiver `selected`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `label` | `String?` | `null` | Rótulo textual do componente. |
| `description` | `String?` | `null` | — |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |

## `MnsSwitch`

Interruptor liga/desliga.

Diferente de um checkbox, um switch aplica a mudança **imediatamente** — não
use dentro de formulário que só salva no botão "Confirmar".

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `checked` | `Boolean` | — | estado atual. |
| `onCheckedChange` | `(Boolean) -> Unit` | — | chamado com o novo estado. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `label` | `String?` | `null` | rótulo à esquerda; o switch fica alinhado à direita da linha. |
| `description` | `String?` | `null` | — |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |

## `MnsSelectionGroup`

Agrupa controles de seleção com espaçamento consistente.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `title` | `String?` | `null` | — |
| `content` | `@Composable RowScope.() -> Unit` | — | Slot de conteúdo do componente. |

## `MnsToggleState`

Estado de um `MnsCheckbox`. O terceiro estado (`INDETERMINATE`) existe para
o caso "selecionar todos" com seleção parcial — sem ele, o pai mente sobre
o estado dos filhos.

| Valor | Significado |
|---|---|
| `UNCHECKED` | Desmarcado. |
| `CHECKED` | Marcado. |
| `INDETERMINATE` | Parcialmente marcado (alguns filhos selecionados). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsSelectionControls.kt`
