# MnsQrCode

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsQrCode`

Exibe um QR Code a partir de uma `MnsQrMatrix`.

```kotlin
val matriz = MnsQrEncoder.encode("MNS-TICKET-8842")
MnsQrCode(matrix = matriz, caption = "Apresente na entrada")
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `matrix` | `MnsQrMatrix` | — | módulos do código. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `size` | `Dp` | `MnsTheme.sizing.qrSize` | lado da área de desenho. |
| `foreground` | `Color` | `MnsTheme.colors.onSurface` | cor dos módulos escuros. |
| `background` | `Color` | `MnsTheme.colors.surface` | cor do fundo. **Mantenha alto contraste** — leitores falham abaixo de ~3:1, e QR "estilizado" ilegível é pior que QR feio. |
| `dotStyle` | `MnsQrDotStyle` | `MnsQrDotStyle.SQUARE` | forma dos módulos. |
| `quietZoneModules` | `Int` | `4` | margem branca ao redor, em módulos. O padrão ISO é 4; abaixo disso a taxa de leitura cai. |
| `shape` | `Shape` | `MnsTheme.shapes.qrFrame` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `caption` | `String?` | `null` | legenda abaixo do código. |
| `contentDescription` | `String` | `"Código QR"` | descrição para leitores de tela. Descreva **o que o código representa** ("Ingresso do evento X"), não "QR Code". |
| `logo` | `(@Composable () -> Unit)?` | `null` | — |

## `MnsQrDotStyle`

Forma de cada módulo do QR. Afeta só a estética, nunca a leitura.

| Valor | Significado |
|---|---|
| `SQUARE` | Módulos quadrados — leitura mais robusta em impressão. |
| `ROUNDED` | Cantos levemente arredondados. |
| `DOT` | Círculos — visual mais leve, exige contraste alto. |

## `MnsQrErrorCorrection`

Nível de correção de erro do QR.

Quanto maior a correção, mais o código sobrevive a sujeira, dobra e logo
sobreposto — ao custo de mais módulos (código mais denso) para o mesmo dado.
Para ingresso impresso ou exibido em tela riscada, use `QUARTILE` ou `HIGH`.

| Valor | Significado |
|---|---|
| `LOW` | ~7% de recuperação. Códigos curtos exibidos em tela limpa. |
| `MEDIUM` | ~15%. **Padrão** — bom equilíbrio. |
| `QUARTILE` | ~25%. Use quando houver logo sobreposto. |
| `HIGH` | ~30%. Impressão em papel térmico, pulseira, ambiente hostil. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/code/MnsQrCode.kt`, `design_system/src/main/java/com/mns/designsystem/component/code/MnsQrEncoder.kt`
