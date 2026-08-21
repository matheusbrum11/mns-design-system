# MnsListAction

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsListAction`

Item de lista acionável, altamente configurável.

Cobre os padrões que aparecem nos três designs de referência: linha simples
de configuração, linha com avatar e metadados, e card de evento com
miniatura, participantes e ação à direita.

```kotlin
MnsListAction(
    title = "North Van Hiking",
    overline = "Mount Seymour",
    subtitle = "Vancouver Community Centre",
    meta = "MAR 20 · 8:30 AM PDT",
    leading = MnsListLeading.Avatar("North Van"),
    trailing = { MnsAvatarGroup(participantes) },
    onClick = ::abrirEvento,
)
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `title` | `String` | — | texto principal. Obrigatório. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `overline` | `String?` | `null` | rótulo pequeno acima do título (categoria, local). |
| `subtitle` | `String?` | `null` | linha de apoio abaixo do título. |
| `meta` | `String?` | `null` | terceira linha, para data/hora/estado. |
| `leading` | `MnsListLeading` | `MnsListLeading.None` | elemento inicial — ver `MnsListLeading`. |
| `trailing` | `(@Composable RowScope.() -> Unit)?` | `null` | slot final (badge, switch, grupo de avatares, preço). |
| `onClick` | `(() -> Unit)?` | `null` | torna a linha acionável. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `showChevron` | `Boolean` | `false` | exibe a seta ">" indicando navegação. Só faz sentido com `onClick` definido e sem `trailing` concorrente. |
| `selected` | `Boolean` | `false` | pinta a linha com a cor de container selecionado. |
| `containerColor` | `Color?` | `null` | sobrescreve a cor de fundo da linha. |
| `shape` | `Shape` | `MnsTheme.shapes.card` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `contentPadding` | `Dp` | `MnsTheme.spacing.listItemPadding` | padding interno. |

## `MnsListLeading`

O que aparece no início de um `MnsListAction`.

É um tipo selado em vez de três parâmetros nuláveis para tornar impossível o
estado "avatar **e** ícone ao mesmo tempo", que renderizava layout quebrado.

| Valor | Significado |
|---|---|
| `None` | Sem elemento inicial — o texto começa na margem. |
| `Avatar` | Avatar com iniciais/foto. |
| `Icon` | Ícone dentro de um container arredondado. |
| `Thumbnail` | Miniatura de imagem (capa de evento, thumbnail). |
| `Custom` | Slot totalmente livre. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/list/MnsListAction.kt`
