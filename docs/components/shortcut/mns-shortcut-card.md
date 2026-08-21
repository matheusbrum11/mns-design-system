# MnsShortcutCard

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsShortcutCard`

Card de atalho — o quadrado "Art / Business / Travel …" do design de eventos.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `shortcut` | `MnsShortcut` | — | dados do atalho. |
| `onClick` | `() -> Unit` | — | chamado ao tocar. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `selected` | `Boolean` | `false` | estado de seleção; usa `primaryContainer` quando ativo. |
| `aspectRatio` | `Float` | `1f` | proporção do card. 1f = quadrado. |
| `shape` | `Shape` | `MnsTheme.shapes.card` | Forma do componente. Use um papel de `MnsTheme.shapes`. |

## `MnsShortcut`

Um atalho do `MnsShortcutGrid`.

| Propriedade | Tipo | Padrão | Descrição |
|---|---|---|---|
| `id` | `String` | — | chave estável — usada como `key` da grade, evitando recomposição desnecessária ao reordenar. |
| `label` | `String` | — | rótulo exibido. |
| `icon` | `ImageVector` | — | ícone do atalho. |
| `badgeCount` | `Int?` | `null` | contador exibido no canto; `null` esconde o badge. |
| `enabled` | `Boolean` | `true` | desabilita o atalho. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/shortcut/MnsShortcutCard.kt`
