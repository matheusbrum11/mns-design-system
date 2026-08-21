# Tokens de tipografia

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

A escala é **fechada em 18 papéis**. Se um design pede um tamanho que não existe aqui, a resposta certa quase sempre é usar o papel mais próximo — não criar o 19º. Escala aberta é a principal causa de deriva visual em design system longevo.

Trocar a fonte do produto inteiro é uma linha:

```kotlin
val tipografia = MnsTypography.default(
    fontFamily = FontFamily(Font(R.font.inter)),
)
```

E densificar (tablet, dashboard) é outra: `MnsTypography.default().scaledBy(0.9f)`.

## `MnsTypography`

**Semantic tokens de tipografia.**

A escala é fechada em 16 papéis. Se um design pede um tamanho que não existe
aqui, a resposta certa quase sempre é usar o papel mais próximo — não criar
o 17º. Escalas abertas são a principal causa de deriva visual em design
systems longevos.

Trocar a fonte do produto inteiro é uma linha:
```kotlin
val tipografia = MnsTypography.default(fontFamily = FontFamily(Font(R.font.inter)))
```

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `/** Números e títulos heroicos de tela de abertura. Use no máximo 1 por tela. */
    val displayLarge` | `TextStyle` | — | — |
| `/** Título de destaque de uma seção-herói. */
    val displayMedium` | `TextStyle` | — | — |
| `/** Título de destaque compacto. */
    val displaySmall` | `TextStyle` | — | — |
| `/** Título principal de tela (ex.: "Today Events"). */
    val headlineLarge` | `TextStyle` | — | — |
| `/** Título de bloco dentro da tela. */
    val headlineMedium` | `TextStyle` | — | — |
| `/** Título de sub-bloco. */
    val headlineSmall` | `TextStyle` | — | — |
| `/** Título de card e de item de lista com ênfase. */
    val titleLarge` | `TextStyle` | — | — |
| `/** Título de item de lista padrão. */
    val titleMedium` | `TextStyle` | — | — |
| `/** Título compacto — cabeçalho de agrupamento. */
    val titleSmall` | `TextStyle` | — | — |
| `/** Corpo de texto longo (descrição de evento, termos). */
    val bodyLarge` | `TextStyle` | — | — |
| `/** Corpo de texto padrão do app. */
    val bodyMedium` | `TextStyle` | — | — |
| `/** Corpo compacto — legenda de card. */
    val bodySmall` | `TextStyle` | — | — |
| `/** Rótulo de botão e de chip. */
    val labelLarge` | `TextStyle` | — | — |
| `/** Rótulo de campo de formulário e de tab. */
    val labelMedium` | `TextStyle` | — | — |
| `contador. */
    val labelSmall` | `TextStyle` | — | — |
| `/** Metadado auxiliar` | `data` | — | — |
| `"há 3 min". */
    val caption` | `TextStyle` | — | — |
| `para seções de catálogo. */
    val overline` | `TextStyle` | — | — |
| `valores alinhados. */
    val mono` | `TextStyle` | — | — |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsTypography.kt`
