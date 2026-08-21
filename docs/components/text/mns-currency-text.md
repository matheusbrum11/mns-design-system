# MnsCurrencyText

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCurrencyText`

Exibe um valor monetário já formatado, recebendo **centavos**.

Usar `Long` de centavos em vez de `Double` não é preciosismo: é o que impede
o erro de arredondamento aparecer no total do carrinho.

```kotlin
MnsCurrencyText(cents = 12550)  // "R$ 125,50"
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `cents` | `Long` | — | valor em centavos. Negativo é aceito e formatado conforme `MnsCurrencyFormat.negativeStyle`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `format` | `MnsCurrencyFormat` | `MnsCurrencyFormat.BRL` | configuração de moeda/locale. |
| `style` | `TextStyle` | `LocalMnsTextStyle.current` | papel tipográfico; default herda do container. |
| `color` | `Color` | `Color.Unspecified` | cor do texto. Se `Color.Unspecified` e `colorizeSign` for `true`, valores negativos ficam em `colors.danger` e positivos em `colors.success`. |
| `colorizeSign` | `Boolean` | `false` | pinta o valor conforme o sinal. Desligado por padrão — ligue apenas em contextos de saldo/variação, nunca em preço. |
| `emphasizeSymbol` | `Boolean` | `false` | renderiza o símbolo da moeda menor que o número, num peso mais leve. É o tratamento usado nos cards de preço dos designs. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/text/MnsFormattedText.kt`
