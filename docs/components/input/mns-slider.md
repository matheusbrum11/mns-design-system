# MnsSlider

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsSlider`

Slider de valor contínuo ou discreto.

Implementado com `Canvas` + gestos em vez de envolver o `Slider` do Material
porque o thumb e a trilha do Material carregam paddings e elevações próprias
que não são tokenizáveis — o resultado ficava fora da grade em temas densos.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `Float` | — | valor atual, dentro de `valueRange`. |
| `onValueChange` | `(Float) -> Unit` | — | chamado continuamente durante o arraste. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `valueRange` | `ClosedFloatingPointRange<Float>` | `0f..1f` | faixa permitida. |
| `steps` | `Int` | `0` | número de paradas intermediárias. `0` = contínuo. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `label` | `String?` | `null` | rótulo acima da trilha. |
| `formatValue` | `((Float) -> String)?` | `null` | formatação do valor exibido à direita do rótulo. |
| `trackHeight` | `Dp` | `6.dp` | espessura da trilha. |
| `thumbRadius` | `Dp` | `11.dp` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsSlider.kt`
