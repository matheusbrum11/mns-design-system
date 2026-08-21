# MnsShimmerParagraph · MnsShimmerListItem · MnsShimmerCard

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsShimmerParagraph`

Esqueleto de parágrafo: N linhas, com a última mais curta para imitar texto
real. Detalhe pequeno, mas é o que faz o placeholder parecer conteúdo em vez
de tabela.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `lines` | `Int` | `3` | quantidade de linhas. |
| `lineHeight` | `Dp` | `14.dp` | — |
| `lastLineFraction` | `Float` | `0.6f` | largura da última linha, como fração da largura total. |
| `visible` | `Boolean` | `true` | Liga/desliga o efeito sem remover o componente da árvore. |

## `MnsShimmerListItem`

Esqueleto de um item de lista: avatar circular + duas linhas de texto.
Corresponde 1:1 ao layout de `MnsListAction`, então a transição de esqueleto
para conteúdo não desloca nada.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `showAvatar` | `Boolean` | `true` | inclui o círculo do avatar. |
| `visible` | `Boolean` | `true` | Liga/desliga o efeito sem remover o componente da árvore. |

## `MnsShimmerCard`

Esqueleto de card com capa: imagem no topo, título e duas linhas.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `coverHeight` | `Dp` | `MnsTheme.sizing.coverHeight` | altura da área de imagem. |
| `visible` | `Boolean` | `true` | Liga/desliga o efeito sem remover o componente da árvore. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/loading/MnsShimmer.kt`
