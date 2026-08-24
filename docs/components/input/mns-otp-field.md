# MnsOtpField

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsOtpField`

Campo de código (OTP / PIN) com uma caixa por dígito.

Mantém **um único** campo de texto invisível por baixo, em vez de N campos
com foco encadeado. Isso preserva colar código inteiro, autofill de SMS e
backspace atravessando caixas — três coisas que a implementação com N campos
quebra.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `String` | — | código digitado até agora. |
| `onValueChange` | `(String) -> Unit` | — | chamado com o novo código, já limitado a `length`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `length` | `Int` | `6` | número de dígitos. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `isError` | `Boolean` | `false` | pinta as caixas com a cor de erro. |
| `boxSize` | `Dp` | `48.dp` | — |
| `onCompleted` | `(String) -> Unit` | `{}` | chamado uma vez quando o código atinge `length`. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsSpecializedFields.kt`
