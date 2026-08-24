# mnsShimmer · MnsShimmerBox

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `mnsShimmer`

Aplica o efeito shimmer de carregamento ao elemento.

O gradiente varre a caixa na diagonal, na duração de `motion.durationShimmer`.
Com `reduceMotion` ligado o movimento é suprimido e resta apenas a cor base —
shimmer em loop é um gatilho conhecido para quem tem sensibilidade a movimento.

```kotlin
Box(Modifier.size(120.dp, 16.dp).mnsShimmer(shape = MnsTheme.shapes.shimmer))
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `visible` | `Boolean` | `true` | desliga o efeito quando `false`, sem remover o modifier — evita recriar a árvore ao terminar o carregamento. |
| `shape` | `Shape?` | `null` | recorte do bloco. |

## `MnsShimmerBox`

Bloco retangular de shimmer — a peça de montar de qualquer esqueleto.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `width` | `Dp?` | `null` | largura; `null` ocupa toda a largura disponível. |
| `height` | `Dp` | `16.dp` | altura do bloco. |
| `shape` | `Shape` | `MnsTheme.shapes.shimmer` | recorte. |
| `visible` | `Boolean` | `true` | Liga/desliga o efeito sem remover o componente da árvore. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/loading/MnsShimmer.kt`
