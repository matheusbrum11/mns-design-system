# MnsBottomNavBar

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsBottomNavBar`

Barra de navegação inferior.

Suporta de 2 a 5 destinos. Acima disso a área de toque de cada item cai
abaixo do confortável e o padrão certo passa a ser um menu.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `items` | `List<MnsNavItem>` | — | destinos. |
| `selectedId` | `String` | — | id do destino ativo. |
| `onSelect` | `(MnsNavItem) -> Unit` | — | chamado com o item tocado. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `showLabels` | `Boolean` | `true` | exibe o rótulo sob o ícone. |
| `containerColor` | `Color` | `MnsTheme.colors.surface` | Cor de fundo do componente. |
| `elevation` | `Dp` | `MnsTheme.elevation.level2` | Altura da sombra. Use um degrau de `MnsTheme.elevation`. |
| `applyNavigationBarPadding` | `Boolean` | `true` | reserva o espaço da barra de gestos. |

## `MnsNavItem`

Item da `MnsBottomNavBar`.

| Propriedade | Tipo | Padrão | Descrição |
|---|---|---|---|
| `id` | `String` | — | chave estável do destino. |
| `label` | `String` | — | rótulo. Mesmo com `MnsBottomNavBar.showLabels` desligado, é usado como `contentDescription` — nunca fica sem rótulo acessível. |
| `icon` | `ImageVector` | — | ícone do estado inativo. |
| `selectedIcon` | `ImageVector?` | `null` | ícone do estado ativo; `null` reutiliza `icon`. |
| `badgeCount` | `Int?` | `null` | contador exibido sobre o ícone. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsBottomNavBar.kt`
