# MnsText

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsText`

Texto base do design system.

Toda string visível no app deveria passar por aqui, e não por `Text` do
Material. Três motivos:

1. o estilo default vem de `LocalMnsTextStyle`, então um container pode
   definir a tipografia dos filhos sem repetir parâmetro;
2. a cor cai para `LocalContentColor`, o que faz o texto se adaptar sozinho
   ao trocar de superfície (card claro → banner escuro);
3. centraliza o ponto onde plugaríamos i18n, pseudo-localização ou auditoria
   de strings hard-coded.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `text` | `String` | — | conteúdo a exibir. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `style` | `TextStyle` | `LocalMnsTextStyle.current` | papel tipográfico. Use `MnsTheme.typography.*`. |
| `color` | `Color` | `Color.Unspecified` | cor do texto. `Color.Unspecified` (default) herda do contexto. |
| `maxLines` | `Int` | `Int.MAX_VALUE` | número máximo de linhas antes de aplicar `overflow`. |
| `minLines` | `Int` | `1` | número mínimo de linhas reservadas — evita "pulo" de layout quando o texto chega de forma assíncrona. |
| `overflow` | `TextOverflow` | `TextOverflow.Clip` | o que fazer quando o texto não cabe. |
| `textAlign` | `TextAlign?` | `null` | alinhamento horizontal; `null` usa o do `style`. |
| `decoration` | `TextDecoration?` | `null` | sublinhado/tachado; útil para preço "de/por". |
| `softWrap` | `Boolean` | `true` | se `false`, o texto nunca quebra linha. |
| `onTextLayout` | `((TextLayoutResult) -> Unit)?` | `null` | callback com as métricas medidas — usado por componentes que precisam saber se houve truncamento (ex.: botão "ver mais"). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/text/MnsText.kt`
