# Tokens

[← Documentação](../README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

Um **token** é uma decisão de design com nome. `#6255F4` é um valor; `primary`
é um token. A diferença importa porque o componente conhece o nome, nunca o
valor — e é isso que permite trocar a marca inteira sem tocar em componente.

## As três camadas

```
┌──────────────────────────────────────────────────────────────┐
│ 1. REFERENCE      MnsPalette / MnsColorRamp                  │
│    valores crus   #6255F4, 16.dp, 14.sp                      │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│ 2. SEMANTIC       MnsColors, MnsShapes, MnsSpacing, …        │
│    intenção       colors.primary, shapes.card, spacing.base  │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│ 3. COMPONENTE     MnsButton, MnsCard, MnsListAction, …       │
│    consumo        MnsTheme.colors.primary                    │
└──────────────────────────────────────────────────────────────┘
```

A camada 1 existe para que a 2 possa ser derivada. A camada 2 é o **contrato**:
é o que a biblioteca promete e o que você sobrescreve. A camada 3 nunca
inventa valor.

> **Regra do projeto:** nenhum componente do `:design_system` contém literal de
> cor, raio, espaçamento, duração ou opacidade. O
> [teste de matriz](../testing.md#matriz-tema--componente) renderiza o catálogo
> inteiro em 6 temas justamente para expor quem quebrar essa regra.

## As famílias

| Família | Tipo | O que controla | Página |
|---|---|---|---|
| Cor | `MnsColors` | 53 papéis semânticos: ações, superfícies, texto, status, efeitos | [colors.md](colors.md) |
| Paleta | `MnsPalette`, `MnsColorRamp` | rampas cruas e utilitários de contraste WCAG | [palette.md](palette.md) |
| Tipografia | `MnsTypography` | 18 papéis, de `displayLarge` a `mono` | [typography.md](typography.md) |
| Forma | `MnsShapes` | escala de raios + papéis de componente | [shapes.md](shapes.md) |
| Espaçamento | `MnsSpacing` | grade de 4dp + papéis (`cardPadding`, `screenHorizontal`) | [spacing.md](spacing.md) |
| Dimensão | `MnsSizing` | ícones, avatares, alturas e alvo de toque | [sizing.md](sizing.md) |
| Elevação | `MnsElevation` | 6 níveis + alphas de sombra por tema | [elevation.md](elevation.md) |
| Traço | `MnsBorders` | 6 espessuras, de `hairline` a `focus` | [borders.md](borders.md) |
| Opacidade | `MnsOpacity` | alphas de estado (`disabled`, `pressed`, `scrim`) | [opacity.md](opacity.md) |
| Movimento | `MnsMotion` | durações, curvas e `reduceMotion` | [motion.md](motion.md) |

Tudo junto forma um [`MnsThemeSpec`](../theming.md) — o pacote que o `MnsTheme`
publica para a árvore.

## Lendo um token

```kotlin
@Composable
fun MeuCard() {
    Column(
        modifier = Modifier
            .background(MnsTheme.colors.surface, MnsTheme.shapes.card)
            .padding(MnsTheme.spacing.cardPadding),
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
    ) {
        MnsText("Título", style = MnsTheme.typography.titleLarge)
    }
}
```

`MnsTheme.*` lê de `CompositionLocal`s estáticos: a leitura é barata e a troca
de tema recompõe apenas quem realmente usa aquele grupo de tokens.

## Sobrescrevendo um token

Você quase nunca constrói um grupo do zero. Parta de um preset e use `copy()`:

```kotlin
val meuSpec = MnsIndigoTicket.light.copy(
    colors = MnsIndigoTicket.light.colors.copy(primary = Color(0xFFFF6B00)),
    shapes = MnsShapes.fromBaseRadius(base = 8.dp, pillButtons = true),
    spacing = MnsSpacing.Compact,
)

MnsTheme(spec = meuSpec) { AppRoot() }
```

Para o caminho completo — incluindo suporte a modo escuro e registro do tema —
veja [Tematização](../theming.md).
