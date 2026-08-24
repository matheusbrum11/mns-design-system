# MnsCard

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCard`

Container de conteúdo agrupado.

```kotlin
MnsCard(variant = MnsCardVariant.ACCENT, onClick = { abrir(evento) }) {
    MnsHeading("North Van Hiking", level = MnsHeadingLevel.H4)
    MnsText("Vancouver Community Centre")
}
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `variant` | `MnsCardVariant` | `MnsCardVariant.FILLED` | aparência — ver `MnsCardVariant`. |
| `onClick` | `(() -> Unit)?` | `null` | quando não-nulo, o card vira alvo de toque com feedback de escala (mais adequado que ripple em superfícies grandes). |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `shape` | `Shape` | `MnsTheme.shapes.card` | forma; default `MnsTheme.shapes.card`. |
| `contentPadding` | `Dp` | `MnsTheme.spacing.cardPadding` | padding interno. Passe `PaddingValues(0.dp)` quando o card começa com uma imagem sangrada. |
| `elevation` | `Dp?` | `null` | sobrescreve a elevação da variante. |
| `verticalArrangement` | `Arrangement.Vertical` | `Arrangement.spacedBy(MnsTheme.spacing.sm)` | espaçamento entre os filhos. |
| `content` | `@Composable ColumnScope.() -> Unit` | — | Slot de conteúdo do componente. |

## `MnsCardVariant`

Aparência de um `MnsCard`. Cada variante corresponde a uma decisão de
hierarquia visual — não são apenas "estilos bonitos":

- `ELEVATED` separa por sombra: use quando o card flutua sobre conteúdo.
- `FILLED` separa por cor: use em listas longas, onde N sombras viram ruído.
- `OUTLINED` separa por traço: use em temas flat ou densidade alta.
- `ACCENT` destaca **um** card da lista (o evento em alta, o plano recomendado).
- `GHOST` não separa nada: agrupa conteúdo sem custo visual.

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsCard.kt`
