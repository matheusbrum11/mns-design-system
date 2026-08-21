# Tokens de forma

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Duas camadas em um único objeto: a **escala** (`none` → `full`), que é o vocabulário cru de raios, e os **papéis de componente** (`button`, `card`, `input`…), que é o que os componentes realmente consomem.

Essa separação é o que viabiliza um redesenho do tipo *"cards ficam mais arredondados, botões continuam iguais"* mexendo em um campo só.

Na prática você quase nunca constrói `MnsShapes` à mão — informa o raio dos cards e deixa a escala ser derivada:

```kotlin
MnsShapes.fromBaseRadius(base = 12.dp, buttonRadius = 16.dp)
MnsShapes.fromBaseRadius(base = 20.dp, pillButtons = true)
```

## `MnsShapes`

**Semantic tokens de forma.**

Divide-se em duas partes:
 - a **escala** (`none` → `full`), que é o vocabulário cru de raios;
 - os **papéis de componente** (`button`, `card`, …), que é o que os
   componentes consomem.

Essa separação é o que permite um redesenho do tipo *"cards ficam mais
arredondados, botões continuam iguais"* mexendo em um campo só.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `// ── Escala ───────────────────────────────────────────────────────────────
    /** Canto reto (0dp). */
    val none` | `CornerBasedShape` | — | — |
| `/** Arredondamento mínimo — 4dp. Tags e badges densos. */
    val extraSmall` | `CornerBasedShape` | — | — |
| `chips. */
    val small` | `CornerBasedShape` | — | — |
| `/** 12dp. Padrão de botões e campos. */
    val medium` | `CornerBasedShape` | — | — |
| `/** 16dp. Cards e containers. */
    val large` | `CornerBasedShape` | — | — |
| `cards-herói. */
    val extraLarge` | `CornerBasedShape` | — | — |
| `/** 32dp. Superfícies muito arredondadas (estilo "squircle"). */
    val huge` | `CornerBasedShape` | — | — |
| `/** Pílula/círculo — 50%. */
    val full` | `CornerBasedShape` | — | — |
| `// ── Papéis de componente ─────────────────────────────────────────────────
    /** Forma de `MnsButton` e `MnsIconButton`. */
    val button` | `CornerBasedShape` | — | — |
| `/** Forma de `MnsTextField` e derivados. */
    val input` | `CornerBasedShape` | — | — |
| `/** Forma de `MnsCard`. */
    val card` | `CornerBasedShape` | — | — |
| `/** Forma de `MnsChip` e `MnsTag`. */
    val chip` | `CornerBasedShape` | — | — |
| `/** Forma do topo de `MnsBottomSheet`. */
    val bottomSheet` | `CornerBasedShape` | — | — |
| `/** Forma de `MnsDialog`. */
    val dialog` | `CornerBasedShape` | — | — |
| `/** Forma padrão de imagens e thumbnails. */
    val image` | `CornerBasedShape` | — | — |
| `/** Forma de `MnsAvatar`. */
    val avatar` | `CornerBasedShape` | — | — |
| `/** Forma de `MnsBadge`. */
    val badge` | `CornerBasedShape` | — | — |
| `/** Forma da moldura de `MnsQrCode`. */
    val qrFrame` | `CornerBasedShape` | — | — |
| `/** Forma dos blocos de `MnsShimmer`. */
    val shimmer` | `CornerBasedShape` | — | — |
| `// ── Metadados ────────────────────────────────────────────────────────────
    /**
     * Raio que originou a escala. Não afeta a renderização` | `existe para que o      * Design Contract consiga exportar a escala de volta para JSON sem precisar      * inspecionar `Shape`` | — | — |
| `que depende de densidade e não é reversível.
     */
    val baseRadius` | `Dp` | `16.dp` | — |
| `pelo mesmo motivo de [baseRadius]. `null` = pílula. */
    val buttonRadius` | `Dp?` | `12.dp` | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsShapes.kt`
