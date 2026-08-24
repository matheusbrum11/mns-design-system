# MnsShortcutGrid

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsShortcutGrid`

Grade de atalhos.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `shortcuts` | `List<MnsShortcut>` | — | atalhos a exibir. |
| `onShortcutClick` | `(MnsShortcut) -> Unit` | — | chamado com o atalho tocado. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `selectedIds` | `Set<String>` | `emptySet()` | ids atualmente selecionados. |
| `columns` | `Int` | `2` | número de colunas. |
| `aspectRatio` | `Float` | `1.15f` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/shortcut/MnsShortcutCard.kt`
