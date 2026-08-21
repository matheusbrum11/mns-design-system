# MnsCurrencyField

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCurrencyField`

Campo monetário com formatação em tempo real.

O estado externo é **sempre em centavos** (`Long`). O usuário digita apenas
dígitos e o campo cuida da máscara; nenhum parse de string formatada
acontece do lado do chamador.

```kotlin
var valor by remember { mutableLongStateOf(0L) }
MnsCurrencyField(cents = valor, onCentsChange = { valor = it }, label = "Valor")
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `cents` | `Long` | — | valor atual em centavos. |
| `onCentsChange` | `(Long) -> Unit` | — | recebe o novo valor em centavos. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `label` | `String?` | `null` | Rótulo textual do componente. |
| `helperText` | `String?` | `null` | — |
| `errorMessage` | `String?` | `null` | — |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `format` | `MnsCurrencyFormat` | `MnsCurrencyFormat.BRL` | moeda e locale — ver `MnsCurrencyFormat`. |
| `maxCents` | `Long` | `99_999_999_99L` | teto do valor. Digitações acima disso são ignoradas. |
| `leadingIcon` | `ImageVector?` | `null` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsSpecializedFields.kt`
