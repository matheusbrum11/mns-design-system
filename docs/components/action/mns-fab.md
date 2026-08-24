# MnsFab

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsFab`

Botão de ação flutuante.

Existe para **uma** ação: a mais provável da tela. Duas FABs na mesma tela é
sinal de que a hierarquia não foi decidida.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `icon` | `ImageVector` | — | ícone da ação. |
| `contentDescription` | `String` | — | descrição obrigatória para acessibilidade. |
| `onClick` | `() -> Unit` | — | Ação disparada no toque. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `expanded` | `Boolean` | `false` | quando `true` e `label` não é nulo, o FAB se expande em pílula mostrando o rótulo. Anime este valor com o scroll da lista para o comportamento "encolhe ao rolar". |
| `label` | `String?` | `null` | rótulo exibido no estado expandido. |
| `containerColor` | `Color` | `MnsTheme.colors.primary` | cor de fundo; default `colors.primary`. |
| `contentColor` | `Color` | `MnsTheme.colors.onPrimary` | Cor herdada por textos e ícones filhos. |
| `size` | `Dp` | `MnsTheme.sizing.fabSize` | Dimensão do componente. Use um degrau de `MnsTheme.sizing`. |
| `shape` | `Shape` | `MnsTheme.shapes.full` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `elevation` | `Dp` | `MnsTheme.elevation.level3` | Altura da sombra. Use um degrau de `MnsTheme.elevation`. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/action/MnsFab.kt`
