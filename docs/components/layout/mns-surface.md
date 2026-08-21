# MnsSurface

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsSurface`

Container primitivo do design system: um retângulo com forma, cor, borda,
elevação e cor de conteúdo consistentes.

Todo componente que "tem fundo" (card, sheet, dialog, chip, banner) é
construído em cima deste. Ele resolve sozinho a parte que costuma ser
esquecida: propagar `LocalContentColor`, para que os textos e ícones
internos peguem automaticamente a cor certa para aquele fundo.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `shape` | `Shape` | `MnsTheme.shapes.card` | recorte aplicado a fundo, borda e sombra simultaneamente. |
| `color` | `Color` | `MnsTheme.colors.surface` | cor do fundo. |
| `contentColor` | `Color` | `MnsTheme.colors.onSurface` | cor herdada por textos e ícones filhos. Por padrão é calculada por contraste sobre `color`. |
| `borderWidth` | `Dp` | `0.dp` | espessura da borda; `0.dp` remove a borda. |
| `borderColor` | `Color` | `MnsTheme.colors.outline` | cor da borda. |
| `elevation` | `Dp` | `MnsTheme.elevation.level0` | altura da sombra. Use `MnsTheme.elevation.*`. |
| `onClick` | `(() -> Unit)?` | `null` | quando não-nulo, torna a superfície clicável com ripple e semântica de botão. |
| `enabled` | `Boolean` | `true` | desabilita o clique e reduz a opacidade. |
| `role` | `Role` | `Role.Button` | — |
| `interactionSource` | `MutableInteractionSource` | `rememberMnsInteractionSource()` | Fonte de interação. Injete a sua para observar ou compartilhar os estados de toque/foco com outro elemento. |
| `content` | `@Composable () -> Unit` | — | Slot de conteúdo do componente. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsSurface.kt`
