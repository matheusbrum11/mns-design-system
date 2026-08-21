# MnsTag

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsTag`

Rótulo estático de estado — "Confirmado", "Pendente", "Esgotado".

Diferente de `MnsChip`, uma tag **não é interativa**: ela descreve, não filtra.
Se o usuário pode tocar para mudar algo, o componente certo é o chip.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `text` | `String` | — | texto da tag. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `status` | `MnsStatus` | `MnsStatus.NEUTRAL` | intenção semântica; define o par de cores. |
| `icon` | `ImageVector?` | `null` | ícone opcional antes do texto. |
| `solid` | `Boolean` | `false` | usa a cor sólida em vez do container suave. Reserve para o caso em que a tag precisa competir com uma imagem de fundo. |
| `shape` | `Shape` | `MnsTheme.shapes.chip` | Forma do componente. Use um papel de `MnsTheme.shapes`. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/status/MnsBadge.kt`
