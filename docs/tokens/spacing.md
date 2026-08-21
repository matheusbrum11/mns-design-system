# Tokens de espaçamento

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Grade de 4dp. Todo `padding`, margem e `Arrangement.spacedBy` do design system sai daqui — nunca de um `16.dp` literal dentro do componente. É isso que permite densificar o app inteiro em uma linha:

```kotlin
MnsSpacing.Compact       // ~75% — tablet, dashboard, listas longas
MnsSpacing.Default       // referência
MnsSpacing.Comfortable   // ~125% — onboarding, telas de destaque
MnsSpacing.Default.scaledBy(0.9f)
```

## `MnsSpacing`

**Semantic tokens de espaçamento.**

Grade de 4dp. Todo padding, margem e `Arrangement.spacedBy` do design system
sai daqui — nunca de um `16.dp` literal dentro do componente. É isso que
permite densificar o app inteiro (ver `compact`) em uma linha.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `/** 0dp — ausência explícita de espaço. */
    val none` | `Dp` | — | — |
| `/** 2dp — separação óptica entre ícone e texto muito pequenos. */
    val xxs` | `Dp` | — | — |
| `/** 4dp — gap interno de badges e tags. */
    val xs` | `Dp` | — | — |
| `/** 8dp — gap entre elementos irmãos próximos. */
    val sm` | `Dp` | — | — |
| `/** 12dp — padding interno de componentes compactos. */
    val md` | `Dp` | — | — |
| `/** 16dp — **unidade base**` | `padding de card` | — | — |
| `margem lateral de tela. */
    val base` | `Dp` | — | — |
| `/** 20dp — respiro entre grupos dentro de um card. */
    val lg` | `Dp` | — | — |
| `/** 24dp — separação entre seções de conteúdo. */
    val xl` | `Dp` | — | — |
| `/** 32dp — separação entre blocos maiores da tela. */
    val xxl` | `Dp` | — | — |
| `/** 40dp — respiro antes de uma ação final (botão de checkout). */
    val xxxl` | `Dp` | — | — |
| `topo de tela de onboarding. */
    val huge` | `Dp` | — | — |
| `// ── Papéis ───────────────────────────────────────────────────────────────
    /** Margem horizontal padrão do conteúdo de tela. */
    val screenHorizontal` | `Dp` | — | — |
| `/** Margem vertical padrão do conteúdo de tela. */
    val screenVertical` | `Dp` | — | — |
| `/** Padding interno padrão de `MnsCard`. */
    val cardPadding` | `Dp` | — | — |
| `/** Espaço entre itens de uma lista. */
    val listItemGap` | `Dp` | — | — |
| `/** Padding interno de um item de lista. */
    val listItemPadding` | `Dp` | — | — |
| `/** Padding interno horizontal de botões. */
    val buttonHorizontal` | `Dp` | — | — |
| `/** Espaço entre ícone e rótulo dentro de um botão. */
    val buttonIconGap` | `Dp` | — | — |
| `/** Padding interno de `MnsBottomSheet`. */
    val sheetPadding` | `Dp` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsSpacing.kt`
