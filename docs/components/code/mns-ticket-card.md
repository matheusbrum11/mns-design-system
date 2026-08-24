# MnsTicketCard

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsTicketCard`

Cartão de ingresso com QR Code.

Composto proposital do design system: reúne cabeçalho, pares chave/valor,
picote tracejado e QR num único componente, porque o layout de ingresso é
repetido em toda tela do fluxo (compra, carteira, check-in) e divergir entre
elas confunde na hora de validar na portaria.

```kotlin
MnsTicketCard(
    title = "Newport Beach Jazz Festival",
    subtitle = "Sydney, Australia · 19 Oct 2024",
    details = listOf("Assento" to "D17, D18", "Portão" to "G2"),
    qrContent = "MNS-TICKET-8842",
    footnote = "Apresente este código na entrada",
)
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `title` | `String` | — | nome do evento. |
| `qrContent` | `String` | — | conteúdo a codificar no QR. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `subtitle` | `String?` | `null` | local e data. |
| `details` | `List<Pair<String, String>>` | `emptyList()` | pares rótulo/valor exibidos em grade de duas colunas. |
| `footnote` | `String?` | `null` | instrução abaixo do código. |
| `errorCorrection` | `MnsQrErrorCorrection` | `MnsQrErrorCorrection.QUARTILE` | nível de correção do QR. Use `MnsQrErrorCorrection.QUARTILE` ou mais alto se o ingresso puder ser impresso. |
| `qrSize` | `Dp` | `MnsTheme.sizing.qrSize` | lado do QR. |
| `shape` | `Shape` | `MnsTheme.shapes.extraLarge` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `containerColor` | `Color` | `MnsTheme.colors.surface` | Cor de fundo do componente. |

## `MnsTicketPerforation`

Linha tracejada que imita o picote de um ingresso físico.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `color` | `Color` | `MnsTheme.colors.outline` | Cor do conteúdo. `Color.Unspecified` herda do contexto. |
| `dashLength` | `Dp` | `6.dp` | — |
| `gapLength` | `Dp` | `6.dp` | — |
| `thickness` | `Dp` | `1.dp` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/code/MnsTicketCard.kt`
