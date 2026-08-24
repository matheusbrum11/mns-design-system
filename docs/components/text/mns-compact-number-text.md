# MnsCompactNumberText

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsCompactNumberText`

Número grande em forma compacta (`1,2 mil`), com o valor completo exposto na
semântica para leitores de tela.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `Long` | — | número a exibir. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `locale` | `Locale` | `Locale("pt", "BR")` | — |
| `threshold` | `Long` | `1_000L` | abaixo deste valor exibe o número por extenso. |
| `style` | `TextStyle` | `LocalMnsTextStyle.current` | Papel tipográfico. Use `MnsTheme.typography.*`. |
| `color` | `Color` | `Color.Unspecified` | Cor do conteúdo. `Color.Unspecified` herda do contexto. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/text/MnsFormattedText.kt`
