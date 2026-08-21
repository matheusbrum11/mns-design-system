# MnsPercentText

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsPercentText`

Exibe um percentual formatado.

```kotlin
MnsPercentText(value = 0.184, format = MnsPercentFormat.Signed) // "+18,4%"
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `Double` | — | fração (`0.42`) ou percentual (`42.0`), conforme `MnsPercentFormat.inputIsFraction`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `format` | `MnsPercentFormat` | `MnsPercentFormat.Default` | — |
| `style` | `TextStyle` | `LocalMnsTextStyle.current` | Papel tipográfico. Use `MnsTheme.typography.*`. |
| `color` | `Color` | `Color.Unspecified` | Cor do conteúdo. `Color.Unspecified` herda do contexto. |
| `colorizeSign` | `Boolean` | `false` | pinta positivo em `success` e negativo em `danger` — o comportamento esperado em indicadores de variação. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/text/MnsFormattedText.kt`
