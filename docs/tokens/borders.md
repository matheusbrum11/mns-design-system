# Tokens de traço

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Espessuras de borda e de divisor. Manter isso fora do espaçamento evita o erro clássico de um redesenho de grade engrossar todas as linhas do app de uma vez.

## `MnsBorders`

**Semantic tokens de traço.**

Espessuras de borda e de divisor. Separar isso do espaçamento evita o erro
clássico de um redesenho de grade engrossar todas as linhas do app.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `/** 0dp — sem traço. */
    val none` | `Dp` | — | — |
| `/** 0.5dp — fio de cabelo` | `divisores de lista em telas densas. */     val hairline: Dp` | — | — |
| `/** 1dp — **padrão**` | `contorno de card e de input. */     val thin: Dp` | — | — |
| `/** 1.5dp — input em foco. */
    val medium` | `Dp` | — | — |
| `contorno de erro. */
    val thick` | `Dp` | — | — |
| `/** 3dp — anel de foco de acessibilidade. */
    val focus` | `Dp` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsBorders.kt`
