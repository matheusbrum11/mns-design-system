# MnsHeading

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsHeading`

Título com semântica de cabeçalho.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `text` | `String` | — | texto do título. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `level` | `MnsHeadingLevel` | `MnsHeadingLevel.H2` | nível hierárquico — ver `MnsHeadingLevel`. |
| `overline` | `String?` | `null` | rótulo curto em caixa alta acima do título (categoria, seção). |
| `subtitle` | `String?` | `null` | linha de apoio abaixo do título. |
| `maxLines` | `Int` | `Int.MAX_VALUE` | truncamento do título. |
| `subtitleMaxLines` | `Int` | `3` | truncamento do subtítulo. Existe para que um cabeçalho espremido por um irmão largo trunque em vez de crescer indefinidamente em altura. |

## `MnsHeadingLevel`

Nível hierárquico de um `MnsHeading`. Mapeia 1:1 nos papéis tipográficos e,
mais importante, é o que alimenta a semântica de acessibilidade — leitores de
tela navegam por títulos, e usar o nível errado quebra essa navegação.

| Valor | Significado |
|---|---|
| `DISPLAY` | Título-herói de tela. Um por tela, no máximo. |
| `H1` | Título principal da tela. |
| `H2` | Título de seção. |
| `H3` | Título de subseção. |
| `H4` | Título de bloco menor (dentro de card). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/text/MnsHeading.kt`
