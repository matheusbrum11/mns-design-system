# MnsCover

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCover`

Área de imagem de capa, com placeholder, véu de legibilidade e slot de
conteúdo sobreposto.

O véu (`scrim`) não é decoração: texto branco sobre foto arbitrária falha
contraste em metade das imagens. O gradiente resolve isso sem escurecer a
imagem inteira.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `painter` | `Painter?` | — | imagem já carregada. `null` mostra o placeholder de shimmer. |
| `contentDescription` | `String?` | — | descrição da imagem; `null` a marca como decorativa. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `imageUrl` | `String?` | `null` | capa remota. Tem precedência sobre `painter` e usa o mesmo shimmer como placeholder de carregamento. |
| `height` | `Dp` | `MnsTheme.sizing.coverHeight` | altura da capa. |
| `shape` | `Shape` | `MnsTheme.shapes.image` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `scrim` | `Boolean` | `false` | aplica gradiente escuro da base ao topo. |
| `placeholderColor` | `Color` | `MnsTheme.colors.surfaceVariant` | — |
| `overlay` | `(@Composable BoxScope.() -> Unit)?` | `null` | conteúdo desenhado sobre a imagem (título, tags, botões). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/media/MnsCover.kt`
