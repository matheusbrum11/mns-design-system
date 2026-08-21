# Tokens de cor

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

`MnsColors` é o **vocabulário completo de cor** que os componentes conhecem. Nenhum componente do MNS pergunta "qual é o roxo?"; ele pergunta "qual é a cor de ação primária?".

Os campos são todos obrigatórios de propósito — um design system que aceita token faltando termina com `Color.Unspecified` invisível em produção. Para não escrever 60 cores à mão, use as fábricas `MnsColors.light(...)` / `MnsColors.dark(...)`, que derivam por interpolação tudo que você não informar:

```kotlin
val cores = MnsColors.light(
    primary = Color(0xFF6255F4),
    accent = Color(0xFFA197FF),
    background = Color(0xFFF8F8F8),
)
```

Para ler uma cor pelo nome (útil em ferramentas e no Design Contract), use `colors.byRole("primaryContainer")`. A lista completa está em `MnsColors.roleNames`.

## `MnsColors`

**Camada 2 — Semantic tokens de cor.**

É o único vocabulário de cor que os componentes conhecem. Um componente nunca
pergunta *"qual é o roxo?"*, ele pergunta *"qual é a cor de ação primária?"*.
Trocar `MnsColors` troca a marca inteira sem recompilar nenhum componente.

Todos os campos são obrigatórios de propósito: um design system que aceita
token faltando acaba caindo em `Color.Unspecified` silencioso em produção.
Se você não quer definir tudo à mão, parta de um preset e use `copy()`:

```kotlin
val minhasCores = MnsIndigoTicket.light.colors.copy(
    primary = Color(0xFF0066FF),
    onPrimary = Color.White,
)
```

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `isLight` | `Boolean` | — | indica se este esquema é claro. Componentes usam para decidir elevação vs. borda, e o app para pintar a status bar. |
| `// ── Ação primária ────────────────────────────────────────────────────────
    /** Cor da ação principal da tela` | `botão de confirmação` | — | — |
| `item selecionado. */
    val primary` | `Color` | — | — |
| `/** Conteúdo (texto/ícone) desenhado sobre [primary]. Deve ter contraste ≥ 4.5` | `1. */     val onPrimary: Color` | — | — |
| `/** Versão suave de [primary] para fundos de destaque (chips, cards ativos). */
    val primaryContainer` | `Color` | — | — |
| `/** Conteúdo desenhado sobre [primaryContainer]. */
    val onPrimaryContainer` | `Color` | — | — |
| `/** [primary] em estado pressionado. */
    val primaryPressed` | `Color` | — | — |
| `/** [primary] em estado desabilitado. */
    val primaryDisabled` | `Color` | — | — |
| `// ── Ação secundária ──────────────────────────────────────────────────────
    /** Ação de apoio` | `botões secundários` | — | — |
| `tabs não selecionadas com ênfase. */
    val secondary` | `Color` | — | — |
| `/** Conteúdo sobre [secondary]. */
    val onSecondary` | `Color` | — | — |
| `/** Fundo suave da família secundária. */
    val secondaryContainer` | `Color` | — | — |
| `/** Conteúdo sobre [secondaryContainer]. */
    val onSecondaryContainer` | `Color` | — | — |
| `// ── Acento / destaque ────────────────────────────────────────────────────
    /** Cor de realce pontual` | `badge de promoção` | — | — |
| `gráfico. Use com parcimônia. */
    val accent` | `Color` | — | — |
| `/** Conteúdo sobre [accent]. */
    val onAccent` | `Color` | — | — |
| `/** Fundo suave de acento — o "card em destaque" da lista. */
    val accentContainer` | `Color` | — | — |
| `/** Conteúdo sobre [accentContainer]. */
    val onAccentContainer` | `Color` | — | — |
| `// ── Superfícies ──────────────────────────────────────────────────────────
    /** Fundo da janela/tela inteira. */
    val background` | `Color` | — | — |
| `/** Conteúdo padrão sobre [background]. */
    val onBackground` | `Color` | — | — |
| `sheets e dialogs. */
    val surface` | `Color` | — | — |
| `/** Conteúdo padrão sobre [surface]. */
    val onSurface` | `Color` | — | — |
| `/** Superfície de segundo nível` | `campos de input` | — | — |
| `linhas de lista alternadas. */
    val surfaceVariant` | `Color` | — | — |
| `/** Conteúdo sobre [surfaceVariant] — normalmente texto secundário. */
    val onSurfaceVariant` | `Color` | — | — |
| `/** Superfície elevada acima de [surface] (menu flutuante, tooltip, bottom sheet). */
    val surfaceElevated` | `Color` | — | — |
| `tooltip escuro sobre tema claro. */
    val surfaceInverse` | `Color` | — | — |
| `/** Conteúdo sobre [surfaceInverse]. */
    val onSurfaceInverse` | `Color` | — | — |
| `// ── Traço e separação ────────────────────────────────────────────────────
    /** Borda visível` | `contorno de input` | — | — |
| `card outlined. */
    val outline` | `Color` | — | — |
| `/** Borda sutil` | `divisores` | — | — |
| `separadores de lista. */
    val outlineVariant` | `Color` | — | — |
| `/** Anel de foco (acessibilidade / navegação por teclado ou D-pad). */
    val focusRing` | `Color` | — | — |
| `// ── Feedback / status ────────────────────────────────────────────────────
    /** Sucesso` | `confirmação` | — | — |
| `check-in feito. */
    val success` | `Color` | — | — |
| `/** Conteúdo sobre [success]. */
    val onSuccess` | `Color` | — | — |
| `/** Fundo suave de sucesso. */
    val successContainer` | `Color` | — | — |
| `/** Conteúdo sobre [successContainer]. */
    val onSuccessContainer` | `Color` | — | — |
| `/** Atenção` | `algo requer revisão mas não bloqueia. */     val warning: Color` | — | — |
| `/** Conteúdo sobre [warning]. */
    val onWarning` | `Color` | — | — |
| `/** Fundo suave de atenção. */
    val warningContainer` | `Color` | — | — |
| `/** Conteúdo sobre [warningContainer]. */
    val onWarningContainer` | `Color` | — | — |
| `/** Erro/destrutivo` | `falha de validação` | — | — |
| `pagamento recusado. */
    val danger` | `Color` | — | — |
| `/** Conteúdo sobre [danger]. */
    val onDanger` | `Color` | — | — |
| `/** Fundo suave de erro. */
    val dangerContainer` | `Color` | — | — |
| `/** Conteúdo sobre [dangerContainer]. */
    val onDangerContainer` | `Color` | — | — |
| `/** Informativo` | `dica` | — | — |
| `estado "em análise". */
    val info` | `Color` | — | — |
| `/** Conteúdo sobre [info]. */
    val onInfo` | `Color` | — | — |
| `/** Fundo suave informativo. */
    val infoContainer` | `Color` | — | — |
| `/** Conteúdo sobre [infoContainer]. */
    val onInfoContainer` | `Color` | — | — |
| `// ── Texto ────────────────────────────────────────────────────────────────
    /** Texto de maior ênfase` | `títulos` | — | — |
| `valores. */
    val textPrimary` | `Color` | — | — |
| `/** Texto de apoio` | `subtítulos` | — | — |
| `descrições. */
    val textSecondary` | `Color` | — | — |
| `/** Texto de menor ênfase` | `metadados` | — | — |
| `placeholders. */
    val textTertiary` | `Color` | — | — |
| `/** Texto de elemento desabilitado. */
    val textDisabled` | `Color` | — | — |
| `/** Texto sobre fundos escuros/coloridos. */
    val textInverse` | `Color` | — | — |
| `/** Texto de link / ação inline. */
    val textLink` | `Color` | — | — |
| `// ── Efeitos ──────────────────────────────────────────────────────────────
    /** Véu atrás de dialogs e bottom sheets modais. */
    val scrim` | `Color` | — | — |
| `/** Camada aplicada sobre imagens para garantir legibilidade do texto. */
    val overlay` | `Color` | — | — |
| `/** Cor base do shimmer de carregamento. */
    val shimmerBase` | `Color` | — | — |
| `/** Cor do brilho que atravessa o shimmer. */
    val shimmerHighlight` | `Color` | — | — |
| `/** Cor da sombra projetada pelos componentes elevados. */
    val shadow` | `Color` | — | — |

## `MnsStatus`

Intenção semântica de um elemento de feedback. É o enum consumido por
`MnsBadge`, `MnsTag`, `MnsAlert`, `MnsProgress` e afins — nunca passe cor
crua para esses componentes.

| Valor | Significado |
|---|---|
| `NEUTRAL` | Sem carga semântica: contagem, rótulo informativo genérico. |
| `INFO` | Informação neutra que vale destacar. |
| `SUCCESS` | Operação concluída com êxito. |
| `WARNING` | Requer atenção, mas não impede o fluxo. |
| `DANGER` | Erro, falha ou ação destrutiva. |
| `ACCENT` | Destaque de marca (promoção, "novo", "em alta"). |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsColors.kt`, `design_system/src/main/java/com/mns/designsystem/token/MnsColorsFactory.kt`
