# Tokens de dimensão

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Alturas mínimas, tamanhos de ícone e de avatar. Fica separado do espaçamento porque **alvo de toque é regra de acessibilidade, não de estética**: `touchTarget` nunca deve cair abaixo de 48dp (WCAG 2.5.5). Um teste de integração cobra esse piso.

## `MnsSizing`

**Semantic tokens de dimensão.**

Alturas mínimas, tamanhos de ícone e de avatar. Separado de `MnsSpacing`
porque tamanho de alvo de toque é regra de acessibilidade, não de estética:
`touchTarget` nunca deve cair abaixo de 48dp.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `// ── Ícones ───────────────────────────────────────────────────────────────
    /** 12dp — ícone decorativo dentro de badge. */
    val iconXs` | `Dp` | — | — |
| `/** 16dp — ícone inline em texto e em chips. */
    val iconSm` | `Dp` | — | — |
| `/** 20dp — **padrão**` | `ícone de botão e de item de lista. */     val iconMd: Dp` | — | — |
| `/** 24dp — ícone de barra de navegação e de app bar. */
    val iconLg` | `Dp` | — | — |
| `/** 32dp — ícone de atalho / categoria. */
    val iconXl` | `Dp` | — | — |
| `/** 48dp — ícone de estado vazio. */
    val iconXxl` | `Dp` | — | — |
| `// ── Avatares ─────────────────────────────────────────────────────────────
    /** 20dp — avatar empilhado em grupo denso. */
    val avatarXs` | `Dp` | — | — |
| `/** 28dp — avatar de item de lista compacto. */
    val avatarSm` | `Dp` | — | — |
| `/** 40dp — **padrão** de item de lista. */
    val avatarMd` | `Dp` | — | — |
| `/** 56dp — avatar de cabeçalho. */
    val avatarLg` | `Dp` | — | — |
| `/** 80dp — avatar de perfil. */
    val avatarXl` | `Dp` | — | — |
| `// ── Alturas de componente ────────────────────────────────────────────────
    /** Altura do botão pequeno. */
    val buttonHeightSm` | `Dp` | — | — |
| `/** Altura do botão padrão. */
    val buttonHeightMd` | `Dp` | — | — |
| `/** Altura do botão grande (CTA de checkout). */
    val buttonHeightLg` | `Dp` | — | — |
| `/** Altura mínima de um campo de input. */
    val inputHeight` | `Dp` | — | — |
| `/** Altura de um chip. */
    val chipHeight` | `Dp` | — | — |
| `/** Altura mínima de um item de lista de uma linha. */
    val listItemHeight` | `Dp` | — | — |
| `/** Altura da top app bar. */
    val topBarHeight` | `Dp` | — | — |
| `/** Altura da bottom navigation bar. */
    val bottomBarHeight` | `Dp` | — | — |
| `/** Diâmetro do FAB. */
    val fabSize` | `Dp` | — | — |
| `/** Largura da alça (handle) do bottom sheet. */
    val sheetHandleWidth` | `Dp` | — | — |
| `/** Altura da alça do bottom sheet. */
    val sheetHandleHeight` | `Dp` | — | — |
| `/** Lado do card de atalho (`MnsShortcutCard`). */
    val shortcutCardSize` | `Dp` | — | — |
| `/** Lado padrão do QR Code renderizado. */
    val qrSize` | `Dp` | — | — |
| `/** Altura padrão da imagem de capa em cards. */
    val coverHeight` | `Dp` | — | — |
| `// ── Acessibilidade ───────────────────────────────────────────────────────
    /** Alvo de toque mínimo. **Não reduza abaixo de 48dp** (WCAG 2.5.5 / Material). */
    val touchTarget` | `Dp` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsSizing.kt`
