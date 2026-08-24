package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * **Camada 2 — Semantic tokens de cor.**
 *
 * É o único vocabulário de cor que os componentes conhecem. Um componente nunca
 * pergunta *"qual é o roxo?"*, ele pergunta *"qual é a cor de ação primária?"*.
 * Trocar [MnsColors] troca a marca inteira sem recompilar nenhum componente.
 *
 * Todos os campos são obrigatórios de propósito: um design system que aceita
 * token faltando acaba caindo em `Color.Unspecified` silencioso em produção.
 * Se você não quer definir tudo à mão, parta de um preset e use `copy()`:
 *
 * ```kotlin
 * val minhasCores = MnsIndigoTicket.light.colors.copy(
 *     primary = Color(0xFF0066FF),
 *     onPrimary = Color.White,
 * )
 * ```
 *
 * @property isLight indica se este esquema é claro. Componentes usam para
 *   decidir elevação vs. borda, e o app para pintar a status bar.
 */
@Immutable
public data class MnsColors(
    val isLight: Boolean,

    // ── Ação primária ────────────────────────────────────────────────────────
    /** Cor da ação principal da tela: botão de confirmação, FAB, item selecionado. */
    val primary: Color,
    /** Conteúdo (texto/ícone) desenhado sobre [primary]. Deve ter contraste ≥ 4.5:1. */
    val onPrimary: Color,
    /** Versão suave de [primary] para fundos de destaque (chips, cards ativos). */
    val primaryContainer: Color,
    /** Conteúdo desenhado sobre [primaryContainer]. */
    val onPrimaryContainer: Color,
    /** [primary] em estado pressionado. */
    val primaryPressed: Color,
    /** [primary] em estado desabilitado. */
    val primaryDisabled: Color,

    // ── Ação secundária ──────────────────────────────────────────────────────
    /** Ação de apoio: botões secundários, tabs não selecionadas com ênfase. */
    val secondary: Color,
    /** Conteúdo sobre [secondary]. */
    val onSecondary: Color,
    /** Fundo suave da família secundária. */
    val secondaryContainer: Color,
    /** Conteúdo sobre [secondaryContainer]. */
    val onSecondaryContainer: Color,

    // ── Acento / destaque ────────────────────────────────────────────────────
    /** Cor de realce pontual: badge de promoção, selo, gráfico. Use com parcimônia. */
    val accent: Color,
    /** Conteúdo sobre [accent]. */
    val onAccent: Color,
    /** Fundo suave de acento — o "card em destaque" da lista. */
    val accentContainer: Color,
    /** Conteúdo sobre [accentContainer]. */
    val onAccentContainer: Color,

    // ── Superfícies ──────────────────────────────────────────────────────────
    /** Fundo da janela/tela inteira. */
    val background: Color,
    /** Conteúdo padrão sobre [background]. */
    val onBackground: Color,
    /** Fundo de cards, sheets e dialogs. */
    val surface: Color,
    /** Conteúdo padrão sobre [surface]. */
    val onSurface: Color,
    /** Superfície de segundo nível: campos de input, linhas de lista alternadas. */
    val surfaceVariant: Color,
    /** Conteúdo sobre [surfaceVariant] — normalmente texto secundário. */
    val onSurfaceVariant: Color,
    /** Superfície elevada acima de [surface] (menu flutuante, tooltip, bottom sheet). */
    val surfaceElevated: Color,
    /** Superfície invertida — snackbar, tooltip escuro sobre tema claro. */
    val surfaceInverse: Color,
    /** Conteúdo sobre [surfaceInverse]. */
    val onSurfaceInverse: Color,

    // ── Traço e separação ────────────────────────────────────────────────────
    /** Borda visível: contorno de input, card outlined. */
    val outline: Color,
    /** Borda sutil: divisores, separadores de lista. */
    val outlineVariant: Color,
    /** Anel de foco (acessibilidade / navegação por teclado ou D-pad). */
    val focusRing: Color,

    // ── Feedback / status ────────────────────────────────────────────────────
    /** Sucesso: confirmação, saldo positivo, check-in feito. */
    val success: Color,
    /** Conteúdo sobre [success]. */
    val onSuccess: Color,
    /** Fundo suave de sucesso. */
    val successContainer: Color,
    /** Conteúdo sobre [successContainer]. */
    val onSuccessContainer: Color,
    /** Atenção: algo requer revisão mas não bloqueia. */
    val warning: Color,
    /** Conteúdo sobre [warning]. */
    val onWarning: Color,
    /** Fundo suave de atenção. */
    val warningContainer: Color,
    /** Conteúdo sobre [warningContainer]. */
    val onWarningContainer: Color,
    /** Erro/destrutivo: falha de validação, exclusão, pagamento recusado. */
    val danger: Color,
    /** Conteúdo sobre [danger]. */
    val onDanger: Color,
    /** Fundo suave de erro. */
    val dangerContainer: Color,
    /** Conteúdo sobre [dangerContainer]. */
    val onDangerContainer: Color,
    /** Informativo: dica, aviso neutro, estado "em análise". */
    val info: Color,
    /** Conteúdo sobre [info]. */
    val onInfo: Color,
    /** Fundo suave informativo. */
    val infoContainer: Color,
    /** Conteúdo sobre [infoContainer]. */
    val onInfoContainer: Color,

    // ── Texto ────────────────────────────────────────────────────────────────
    /** Texto de maior ênfase: títulos, valores. */
    val textPrimary: Color,
    /** Texto de apoio: subtítulos, descrições. */
    val textSecondary: Color,
    /** Texto de menor ênfase: metadados, timestamps, placeholders. */
    val textTertiary: Color,
    /** Texto de elemento desabilitado. */
    val textDisabled: Color,
    /** Texto sobre fundos escuros/coloridos. */
    val textInverse: Color,
    /** Texto de link / ação inline. */
    val textLink: Color,

    // ── Efeitos ──────────────────────────────────────────────────────────────
    /** Véu atrás de dialogs e bottom sheets modais. */
    val scrim: Color,
    /** Camada aplicada sobre imagens para garantir legibilidade do texto. */
    val overlay: Color,
    /** Cor base do shimmer de carregamento. */
    val shimmerBase: Color,
    /** Cor do brilho que atravessa o shimmer. */
    val shimmerHighlight: Color,
    /** Cor da sombra projetada pelos componentes elevados. */
    val shadow: Color,
) {
    /**
     * Resolve o par (container, onContainer) para um [MnsStatus]. Evita `when`
     * repetido dentro de badges, alerts e tags.
     */
    public fun containerFor(status: MnsStatus): Pair<Color, Color> = when (status) {
        MnsStatus.NEUTRAL -> surfaceVariant to onSurfaceVariant
        MnsStatus.INFO -> infoContainer to onInfoContainer
        MnsStatus.SUCCESS -> successContainer to onSuccessContainer
        MnsStatus.WARNING -> warningContainer to onWarningContainer
        MnsStatus.DANGER -> dangerContainer to onDangerContainer
        MnsStatus.ACCENT -> accentContainer to onAccentContainer
    }

    /** Resolve o par (sólido, onSólido) para um [MnsStatus]. */
    public fun solidFor(status: MnsStatus): Pair<Color, Color> = when (status) {
        MnsStatus.NEUTRAL -> onSurface to surface
        MnsStatus.INFO -> info to onInfo
        MnsStatus.SUCCESS -> success to onSuccess
        MnsStatus.WARNING -> warning to onWarning
        MnsStatus.DANGER -> danger to onDanger
        MnsStatus.ACCENT -> accent to onAccent
    }

    /**
     * Resolve uma cor pelo nome do papel semântico.
     *
     * É o que permite ferramentas genéricas — o playground do `app_demo`, o
     * agente de Design Contract, um editor remoto de tema — manipularem tokens
     * sem conhecer a estrutura do `data class` em tempo de compilação.
     *
     * @param role nome do papel, sem distinção de caixa (`"primary"`, `"onPrimary"`).
     * @throws IllegalArgumentException se o papel não existir.
     */
    public fun byRole(role: String): Color = requireNotNull(byRoleOrNull(role)) {
        "Papel de cor desconhecido: '$role'. Veja MnsColors.roleNames."
    }

    /** Versão tolerante de [byRole]: devolve `null` em vez de lançar. */
    public fun byRoleOrNull(role: String): Color? = when (role.lowercase()) {
        "primary" -> primary
        "onprimary" -> onPrimary
        "primarycontainer" -> primaryContainer
        "onprimarycontainer" -> onPrimaryContainer
        "primarypressed" -> primaryPressed
        "primarydisabled" -> primaryDisabled
        "secondary" -> secondary
        "onsecondary" -> onSecondary
        "secondarycontainer" -> secondaryContainer
        "onsecondarycontainer" -> onSecondaryContainer
        "accent" -> accent
        "onaccent" -> onAccent
        "accentcontainer" -> accentContainer
        "onaccentcontainer" -> onAccentContainer
        "background" -> background
        "onbackground" -> onBackground
        "surface" -> surface
        "onsurface" -> onSurface
        "surfacevariant" -> surfaceVariant
        "onsurfacevariant" -> onSurfaceVariant
        "surfaceelevated" -> surfaceElevated
        "surfaceinverse" -> surfaceInverse
        "onsurfaceinverse" -> onSurfaceInverse
        "outline" -> outline
        "outlinevariant" -> outlineVariant
        "focusring" -> focusRing
        "success" -> success
        "onsuccess" -> onSuccess
        "successcontainer" -> successContainer
        "onsuccesscontainer" -> onSuccessContainer
        "warning" -> warning
        "onwarning" -> onWarning
        "warningcontainer" -> warningContainer
        "onwarningcontainer" -> onWarningContainer
        "danger" -> danger
        "ondanger" -> onDanger
        "dangercontainer" -> dangerContainer
        "ondangercontainer" -> onDangerContainer
        "info" -> info
        "oninfo" -> onInfo
        "infocontainer" -> infoContainer
        "oninfocontainer" -> onInfoContainer
        "textprimary" -> textPrimary
        "textsecondary" -> textSecondary
        "texttertiary" -> textTertiary
        "textdisabled" -> textDisabled
        "textinverse" -> textInverse
        "textlink" -> textLink
        "scrim" -> scrim
        "overlay" -> overlay
        "shimmerbase" -> shimmerBase
        "shimmerhighlight" -> shimmerHighlight
        "shadow" -> shadow
        else -> null
    }

    public companion object {
        /** Todos os papéis de cor aceitos por [byRole], na ordem da documentação. */
        public val roleNames: List<String> = listOf(
            "primary", "onPrimary", "primaryContainer", "onPrimaryContainer",
            "primaryPressed", "primaryDisabled",
            "secondary", "onSecondary", "secondaryContainer", "onSecondaryContainer",
            "accent", "onAccent", "accentContainer", "onAccentContainer",
            "background", "onBackground", "surface", "onSurface",
            "surfaceVariant", "onSurfaceVariant", "surfaceElevated",
            "surfaceInverse", "onSurfaceInverse",
            "outline", "outlineVariant", "focusRing",
            "success", "onSuccess", "successContainer", "onSuccessContainer",
            "warning", "onWarning", "warningContainer", "onWarningContainer",
            "danger", "onDanger", "dangerContainer", "onDangerContainer",
            "info", "onInfo", "infoContainer", "onInfoContainer",
            "textPrimary", "textSecondary", "textTertiary", "textDisabled",
            "textInverse", "textLink",
            "scrim", "overlay", "shimmerBase", "shimmerHighlight", "shadow",
        )
    }
}

/**
 * Intenção semântica de um elemento de feedback. É o enum consumido por
 * `MnsBadge`, `MnsTag`, `MnsAlert`, `MnsProgress` e afins — nunca passe cor
 * crua para esses componentes.
 */
public enum class MnsStatus {
    /** Sem carga semântica: contagem, rótulo informativo genérico. */
    NEUTRAL,

    /** Informação neutra que vale destacar. */
    INFO,

    /** Operação concluída com êxito. */
    SUCCESS,

    /** Requer atenção, mas não impede o fluxo. */
    WARNING,

    /** Erro, falha ou ação destrutiva. */
    DANGER,

    /** Destaque de marca (promoção, "novo", "em alta"). */
    ACCENT,
}
