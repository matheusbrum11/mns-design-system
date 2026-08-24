# Tokens de elevação

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Além do valor em dp, o token carrega `shadowAlpha` e `ambientAlpha`, porque a mesma elevação precisa de sombras muito diferentes em tema claro e escuro — no escuro a sombra praticamente some e quem separa as camadas é a cor da superfície. Componentes que respeitam isso não "flutuam errado" no dark mode.

Presets: `MnsElevation.Light`, `MnsElevation.Dark` e `MnsElevation.Flat` (nenhuma sombra; hierarquia só por borda e cor).

## `MnsElevation`

**Semantic tokens de elevação.**

Além do valor em dp, carrega `shadowAlpha` e `ambientAlpha` porque a mesma
elevação precisa de sombras muito diferentes em tema claro e escuro — em
tema escuro a sombra praticamente some e o que separa as camadas é a cor da
superfície. Componentes que respeitam isso não "flutuam errado" no dark mode.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `/** 0dp — no plano da superfície. */
    val level0` | `Dp` | — | — |
| `/** 1dp — separação mínima (barra fixa sobre conteúdo rolando). */
    val level1` | `Dp` | — | — |
| `/** 3dp — cards. */
    val level2` | `Dp` | — | — |
| `menu suspenso. */
    val level3` | `Dp` | — | — |
| `/** 8dp — bottom sheet. */
    val level4` | `Dp` | — | — |
| `/** 12dp — dialog modal. */
    val level5` | `Dp` | — | — |
| `/** Opacidade da sombra projetada (`spotShadowColor`). */
    val shadowAlpha` | `Float` | — | — |
| `/** Opacidade da sombra ambiente. */
    val ambientAlpha` | `Float` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsElevation.kt`
