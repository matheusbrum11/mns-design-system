# MnsStepper

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsStepper`

Seletor numérico de incremento/decremento — quantidade de ingressos,
passageiros, itens no carrinho.

Prefira o stepper a um campo numérico quando o intervalo é pequeno (até ~10):
dois toques batem digitar em teclado numérico, e não há estado inválido.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `Int` | — | quantidade atual. |
| `onValueChange` | `(Int) -> Unit` | — | chamado com o novo valor, já respeitando `range` e `step`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `range` | `IntRange` | `0..99` | faixa permitida. Os botões desabilitam nos extremos. |
| `step` | `Int` | `1` | incremento por toque. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `label` | `String?` | `null` | rótulo à esquerda do controle. |
| `formatValue` | `(Int) -> String` | `{ it.toString() }` | formatação do número exibido (ex.: "2 pessoas"). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsStepper.kt`
