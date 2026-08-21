# Tokens de opacidade

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Centraliza os alphas de estado. Sem isso, cada componente inventa o seu `0.38f` e o app termina com cinco cinzas de "desabilitado" ligeiramente diferentes.

## `MnsOpacity`

**Semantic tokens de opacidade.**

Centraliza os alphas de estado. Sem isso cada componente inventa o seu
`0.38f` e o app fica com cinco cinzas de "desabilitado" ligeiramente
diferentes.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `/** Elemento desabilitado — conteúdo e container. */
    val disabled` | `Float` | — | — |
| `/** Sobreposição no estado pressionado. */
    val pressed` | `Float` | — | — |
| `/** Sobreposição no estado hover (teclado/mouse/TV). */
    val hovered` | `Float` | — | — |
| `/** Sobreposição no estado focado. */
    val focused` | `Float` | — | — |
| `/** Sobreposição no estado arrastado. */
    val dragged` | `Float` | — | — |
| `/** Véu do scrim atrás de modais. */
    val scrim` | `Float` | — | — |
| `/** Camada sobre imagens para legibilidade de texto. */
    val overlay` | `Float` | — | — |
| `/** Ênfase reduzida de texto/ícone secundário. */
    val subtle` | `Float` | — | — |
| `/** Divisores e traços muito discretos. */
    val faint` | `Float` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsOpacity.kt`
