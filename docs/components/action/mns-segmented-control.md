# MnsSegmentedControl

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsSegmentedControl`

Controle segmentado — o par "One Way / Round Trip" dos designs de referência.

Use para alternar entre **modos de visualização mutuamente exclusivos** com
2 a 4 opções. Acima de 4, use `MnsTabBar`; para filtros múltiplos, use chips.

O indicador desliza entre as posições em vez de aparecer/sumir: o movimento
comunica que as opções pertencem ao mesmo eixo de escolha.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `segments` | `List<MnsSegment>` | — | opções, na ordem de exibição. Precisa ter ao menos uma. |
| `selectedIndex` | `Int` | — | índice da opção ativa. |
| `onSelect` | `(Int) -> Unit` | — | chamado com o índice tocado. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `height` | `Dp` | `MnsTheme.sizing.buttonHeightMd` | altura do controle. |
| `shape` | `Shape` | `MnsTheme.shapes.full` | Forma do componente. Use um papel de `MnsTheme.shapes`. |

## `MnsSegment`

Uma opção do `MnsSegmentedControl`.

| Propriedade | Tipo | Padrão | Descrição |
|---|---|---|---|
| `label` | `String` | — | texto exibido. |
| `icon` | `ImageVector?` | `null` | ícone opcional à esquerda do texto. |
| `enabled` | `Boolean` | `true` | quando `false`, a opção não pode ser escolhida. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/action/MnsSegmentedControl.kt`
