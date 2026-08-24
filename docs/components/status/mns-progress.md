# MnsCircularProgress · MnsLinearProgress

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCircularProgress`

Indicador de progresso circular.

Passe `progress` para o modo determinado (0f–1f) ou deixe `null` para o modo
indeterminado. Prefira o determinado sempre que houver como estimar: barra
indeterminada em operação longa é a principal causa de abandono percebido.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `progress` | `Float?` | `null` | fração concluída (0f–1f), ou `null` para indeterminado. |
| `size` | `Dp` | `MnsTheme.sizing.iconLg` | diâmetro do indicador. |
| `strokeWidth` | `Dp` | `3.dp` | espessura do traço. |
| `color` | `Color` | `MnsTheme.colors.primary` | cor do arco de progresso. |
| `trackColor` | `Color` | `MnsTheme.colors.outlineVariant` | cor da trilha de fundo. `Color.Transparent` remove a trilha. |
| `contentDescription` | `String?` | `null` | descrição para leitores de tela. |

## `MnsLinearProgress`

Barra de progresso linear.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `progress` | `Float?` | `null` | fração concluída (0f–1f), ou `null` para indeterminado. |
| `height` | `Dp` | `6.dp` | espessura da barra. |
| `color` | `Color` | `MnsTheme.colors.primary` | cor do preenchimento. |
| `trackColor` | `Color` | `MnsTheme.colors.surfaceVariant` | cor da trilha. |
| `contentDescription` | `String?` | `null` | Descrição para leitores de tela. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/status/MnsProgress.kt`
