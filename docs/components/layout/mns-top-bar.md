# MnsTopBar

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsTopBar`

Barra superior.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `title` | `String?` | `null` | título da tela. Trunca em uma linha. |
| `subtitle` | `String?` | `null` | segunda linha, menor — útil para contexto (nome do evento). |
| `onNavigateBack` | `(() -> Unit)?` | `null` | quando não-nulo, exibe o botão de voltar. |
| `navigationIcon` | `(@Composable () -> Unit)?` | `null` | slot que substitui completamente o botão de voltar. |
| `actions` | `(@Composable RowScope.() -> Unit)?` | `null` | ações à direita. Máximo recomendado: 2 ícones + overflow. |
| `alignment` | `MnsTopBarAlignment` | `MnsTopBarAlignment.START` | posição do título — ver `MnsTopBarAlignment`. |
| `containerColor` | `Color` | `MnsTheme.colors.background` | cor de fundo. `Color.Transparent` para barra sobreposta a uma imagem de capa. |
| `contentColor` | `Color` | `MnsTheme.colors.onBackground` | Cor herdada por textos e ícones filhos. |
| `elevation` | `Dp` | `MnsTheme.elevation.level0` | Altura da sombra. Use um degrau de `MnsTheme.elevation`. |
| `applyStatusBarPadding` | `Boolean` | `true` | reserva o espaço da status bar. Desligue quando a barra já estiver dentro de um container que aplicou o inset. |

## `MnsTopBarAlignment`

Alinhamento do título na `MnsTopBar`.

| Valor | Significado |
|---|---|
| `START` | Título à esquerda, após a navegação. Padrão Android. |
| `CENTER` | Título centralizado. Padrão dos designs de referência de ticketing. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsTopBar.kt`
