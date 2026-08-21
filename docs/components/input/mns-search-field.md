# MnsSearchField

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsSearchField`

Campo de busca — ícone de lupa, forma em pílula e botão de limpar que só
aparece quando há texto.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `String` | — | texto atual. |
| `onValueChange` | `(String) -> Unit` | — | chamado a cada alteração. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `placeholder` | `String` | `"Buscar"` | texto de dica. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `showClearButton` | `Boolean` | `true` | exibe o botão de limpar quando há conteúdo. |
| `onSearch` | `(String) -> Unit` | `{}` | chamado quando o usuário aciona a busca no teclado. |
| `shape` | `Shape` | `MnsTheme.shapes.full` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `interactionSource` | `MutableInteractionSource` | `rememberMnsInteractionSource()` | Fonte de interação. Injete a sua para observar ou compartilhar os estados de toque/foco com outro elemento. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsSpecializedFields.kt`
