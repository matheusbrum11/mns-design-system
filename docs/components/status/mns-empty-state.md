# MnsEmptyState

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsEmptyState`

Estado vazio de uma lista ou tela.

Um estado vazio bom responde três perguntas: *o que aconteceu*, *por que* e
*o que eu faço agora*. Por isso `title` e `description` são separados e há um
slot dedicado para a `action` — um "Nenhum resultado" solto não ajuda ninguém.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `title` | `String` | — | frase curta do que está vazio. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `description` | `String?` | `null` | explicação e/ou próximo passo. |
| `icon` | `ImageVector?` | `null` | ilustração/ícone acima do texto. |
| `action` | `(@Composable () -> Unit)?` | `null` | slot da ação de saída (ex.: "Limpar filtros"). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/status/MnsEmptyState.kt`
