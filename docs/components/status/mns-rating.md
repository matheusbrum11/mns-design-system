# MnsRating

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsRating`

Linha de avaliação por estrelas.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `rating` | `Float` | — | nota atual, de 0 até `max`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `max` | `Int` | `5` | número de estrelas. |
| `showValue` | `Boolean` | `true` | exibe a nota numérica ao lado das estrelas. |
| `onRatingChange` | `((Int) -> Unit)?` | `null` | quando não-nulo, torna as estrelas tocáveis. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/status/MnsEmptyState.kt`
