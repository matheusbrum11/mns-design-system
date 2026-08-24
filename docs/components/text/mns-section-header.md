# MnsSectionHeader

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsSectionHeader`

Cabeçalho de seção com ação à direita — o padrão "Today Events … See All".

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `title` | `String` | — | título da seção. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `subtitle` | `String?` | `null` | linha de apoio opcional. |
| `level` | `MnsHeadingLevel` | `MnsHeadingLevel.H2` | — |
| `action` | `@Composable (RowScope.() -> Unit)?` | `null` | slot da ação à direita. Costuma ser um `MnsButton` `TEXT`. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/text/MnsHeading.kt`
