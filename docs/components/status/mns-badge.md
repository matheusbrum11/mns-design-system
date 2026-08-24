# MnsBadge

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsBadge`

Contador ou ponto de notificação, ancorado a outro elemento.

```kotlin
MnsBadgedBox(badge = { MnsBadge(count = 3) }) {
    MnsIcon(Icons.Filled.Notifications, "Notificações")
}
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `count` | `Int?` | `null` | valor exibido. `null` renderiza apenas o ponto — use quando existe novidade mas o número exato não importa. |
| `max` | `Int` | `99` | acima deste valor exibe `"$max+"`. Impede o badge de esticar e quebrar o alinhamento do ícone. |
| `status` | `MnsStatus` | `MnsStatus.DANGER` | intenção semântica; define a cor. |
| `contentDescription` | `String?` | `null` | texto anunciado por leitores de tela. O default é o próprio rótulo (`"12"`, `"99+"`), porque um badge pode contar qualquer coisa — mensagens, itens no carrinho, filtros ativos. Diga o que ele conta. |

## `MnsBadgedBox`

Ancora um `MnsBadge` no canto superior direito de `content`.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `badge` | `@Composable BoxScope.() -> Unit` | — | slot do badge. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `content` | `@Composable BoxScope.() -> Unit` | — | elemento decorado (ícone, avatar, aba). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/status/MnsBadge.kt`
