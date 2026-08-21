# MnsIconButton

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsIconButton`

Botão que contém apenas um ícone.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `icon` | `ImageVector` | — | ícone exibido. |
| `contentDescription` | `String?` | — | **obrigatório**: sem rótulo textual, é a única informação que o leitor de tela tem. Passe `null` apenas se o botão for puramente decorativo — o que quase nunca é o caso. |
| `onClick` | `() -> Unit` | — | Ação disparada no toque. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `variant` | `MnsButtonVariant` | `MnsButtonVariant.TEXT` | ênfase — ver `MnsButtonVariant`. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `size` | `Dp` | `MnsTheme.sizing.touchTarget` | lado do alvo de toque. Nunca abaixo de `sizing.touchTarget`. |
| `iconSize` | `Dp` | `MnsTheme.sizing.iconMd` | — |
| `shape` | `Shape` | `MnsTheme.shapes.full` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `interactionSource` | `MutableInteractionSource` | `rememberMnsInteractionSource()` | Fonte de interação. Injete a sua para observar ou compartilhar os estados de toque/foco com outro elemento. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/action/MnsButton.kt`
