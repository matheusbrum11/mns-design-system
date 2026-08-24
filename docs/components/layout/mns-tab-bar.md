# MnsTabBar · MnsFixedTabBar

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsTabBar`

Barra de abas rolável.

A barra rola sozinha para trazer a aba ativa ao campo de visão — sem isso,
navegar por teclado ou por deep link deixa a aba selecionada fora da tela.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `tabs` | `List<MnsTab>` | — | abas, na ordem de exibição. |
| `selectedIndex` | `Int` | — | índice ativo. |
| `onSelect` | `(Int) -> Unit` | — | chamado com o índice tocado. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `containerColor` | `Color` | `Color.Transparent` | Cor de fundo do componente. |
| `indicatorColor` | `Color` | `MnsTheme.colors.primary` | cor do sublinhado da aba ativa. |
| `edgePadding` | `Dp` | `MnsTheme.spacing.screenHorizontal` | recuo nas extremidades da lista. |

## `MnsFixedTabBar`

Variante "segmentada" da barra de abas: as abas dividem a largura igualmente
e a ativa recebe fundo em vez de sublinhado. Boa para 2–3 abas fixas.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `tabs` | `List<MnsTab>` | — | — |
| `selectedIndex` | `Int` | — | — |
| `onSelect` | `(Int) -> Unit` | — | — |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |

## `MnsTab`

Uma aba da `MnsTabBar`.

| Propriedade | Tipo | Padrão | Descrição |
|---|---|---|---|
| `id` | `String` | — | chave estável. |
| `label` | `String` | — | rótulo exibido. |
| `badgeCount` | `Int?` | `null` | contador opcional exibido **ao lado** do rótulo. |
| `badgeStatus` | `MnsStatus` | `MnsStatus.NEUTRAL` | intenção do contador. O default é `MnsStatus.NEUTRAL` de propósito: contar itens não é alerta, e badge vermelho em aba de catálogo treina o usuário a ignorar vermelho onde ele importa. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsTabBar.kt`
