package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * **Semantic tokens de dimensão.**
 *
 * Alturas mínimas, tamanhos de ícone e de avatar. Separado de [MnsSpacing]
 * porque tamanho de alvo de toque é regra de acessibilidade, não de estética:
 * [touchTarget] nunca deve cair abaixo de 48dp.
 */
@Immutable
public data class MnsSizing(
    // ── Ícones ───────────────────────────────────────────────────────────────
    /** 12dp — ícone decorativo dentro de badge. */
    val iconXs: Dp,
    /** 16dp — ícone inline em texto e em chips. */
    val iconSm: Dp,
    /** 20dp — **padrão**: ícone de botão e de item de lista. */
    val iconMd: Dp,
    /** 24dp — ícone de barra de navegação e de app bar. */
    val iconLg: Dp,
    /** 32dp — ícone de atalho / categoria. */
    val iconXl: Dp,
    /** 48dp — ícone de estado vazio. */
    val iconXxl: Dp,

    // ── Avatares ─────────────────────────────────────────────────────────────
    /** 20dp — avatar empilhado em grupo denso. */
    val avatarXs: Dp,
    /** 28dp — avatar de item de lista compacto. */
    val avatarSm: Dp,
    /** 40dp — **padrão** de item de lista. */
    val avatarMd: Dp,
    /** 56dp — avatar de cabeçalho. */
    val avatarLg: Dp,
    /** 80dp — avatar de perfil. */
    val avatarXl: Dp,

    // ── Alturas de componente ────────────────────────────────────────────────
    /** Altura do botão pequeno. */
    val buttonHeightSm: Dp,
    /** Altura do botão padrão. */
    val buttonHeightMd: Dp,
    /** Altura do botão grande (CTA de checkout). */
    val buttonHeightLg: Dp,
    /** Altura mínima de um campo de input. */
    val inputHeight: Dp,
    /** Altura de um chip. */
    val chipHeight: Dp,
    /** Altura mínima de um item de lista de uma linha. */
    val listItemHeight: Dp,
    /** Altura da top app bar. */
    val topBarHeight: Dp,
    /** Altura da bottom navigation bar. */
    val bottomBarHeight: Dp,
    /** Diâmetro do FAB. */
    val fabSize: Dp,
    /** Largura da alça (handle) do bottom sheet. */
    val sheetHandleWidth: Dp,
    /** Altura da alça do bottom sheet. */
    val sheetHandleHeight: Dp,
    /** Lado do card de atalho (`MnsShortcutCard`). */
    val shortcutCardSize: Dp,
    /** Lado padrão do QR Code renderizado. */
    val qrSize: Dp,
    /** Altura padrão da imagem de capa em cards. */
    val coverHeight: Dp,

    // ── Acessibilidade ───────────────────────────────────────────────────────
    /**
     * Alvo de toque padrão — 48dp (WCAG 2.5.5 AAA / Material).
     *
     * É o default de todo componente acionável do MNS. Containers densos são a
     * única exceção conhecida: dentro de um chip de 32dp ou de um campo de
     * 52dp, um alvo de 48dp forçaria o container a crescer. Nesses casos o piso
     * é **24dp**, o mínimo do WCAG 2.5.8 (AA) — abaixo disso é bug.
     */
    val touchTarget: Dp,
) {
    public companion object {
        /** Dimensões padrão do MNS, calibradas para telefones de 360–430dp. */
        public val Default: MnsSizing = MnsSizing(
            iconXs = 12.dp, iconSm = 16.dp, iconMd = 20.dp,
            iconLg = 24.dp, iconXl = 32.dp, iconXxl = 48.dp,
            avatarXs = 20.dp, avatarSm = 28.dp, avatarMd = 40.dp,
            avatarLg = 56.dp, avatarXl = 80.dp,
            buttonHeightSm = 36.dp,
            buttonHeightMd = 48.dp,
            buttonHeightLg = 56.dp,
            inputHeight = 52.dp,
            chipHeight = 32.dp,
            listItemHeight = 56.dp,
            topBarHeight = 56.dp,
            bottomBarHeight = 64.dp,
            fabSize = 56.dp,
            sheetHandleWidth = 36.dp,
            sheetHandleHeight = 4.dp,
            shortcutCardSize = 96.dp,
            qrSize = 220.dp,
            coverHeight = 180.dp,
            touchTarget = 48.dp,
        )
    }
}
