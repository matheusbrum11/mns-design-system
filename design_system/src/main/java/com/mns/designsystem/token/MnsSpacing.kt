package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * **Semantic tokens de espaçamento.**
 *
 * Grade de 4dp. Todo padding, margem e `Arrangement.spacedBy` do design system
 * sai daqui — nunca de um `16.dp` literal dentro do componente. É isso que
 * permite densificar o app inteiro (ver [compact]) em uma linha.
 */
@Immutable
public data class MnsSpacing(
    /** 0dp — ausência explícita de espaço. */
    val none: Dp,
    /** 2dp — separação óptica entre ícone e texto muito pequenos. */
    val xxs: Dp,
    /** 4dp — gap interno de badges e tags. */
    val xs: Dp,
    /** 8dp — gap entre elementos irmãos próximos. */
    val sm: Dp,
    /** 12dp — padding interno de componentes compactos. */
    val md: Dp,
    /** 16dp — **unidade base**: padding de card, margem lateral de tela. */
    val base: Dp,
    /** 20dp — respiro entre grupos dentro de um card. */
    val lg: Dp,
    /** 24dp — separação entre seções de conteúdo. */
    val xl: Dp,
    /** 32dp — separação entre blocos maiores da tela. */
    val xxl: Dp,
    /** 40dp — respiro antes de uma ação final (botão de checkout). */
    val xxxl: Dp,
    /** 56dp — espaço heroico, topo de tela de onboarding. */
    val huge: Dp,

    // ── Papéis ───────────────────────────────────────────────────────────────
    /** Margem horizontal padrão do conteúdo de tela. */
    val screenHorizontal: Dp,
    /** Margem vertical padrão do conteúdo de tela. */
    val screenVertical: Dp,
    /** Padding interno padrão de `MnsCard`. */
    val cardPadding: Dp,
    /** Espaço entre itens de uma lista. */
    val listItemGap: Dp,
    /** Padding interno de um item de lista. */
    val listItemPadding: Dp,
    /** Padding interno horizontal de botões. */
    val buttonHorizontal: Dp,
    /** Espaço entre ícone e rótulo dentro de um botão. */
    val buttonIconGap: Dp,
    /** Padding interno de `MnsBottomSheet`. */
    val sheetPadding: Dp,
) {
    public companion object {
        /** Grade de 4dp padrão do MNS. */
        public val Default: MnsSpacing = MnsSpacing(
            none = 0.dp, xxs = 2.dp, xs = 4.dp, sm = 8.dp, md = 12.dp, base = 16.dp,
            lg = 20.dp, xl = 24.dp, xxl = 32.dp, xxxl = 40.dp, huge = 56.dp,
            screenHorizontal = 16.dp,
            screenVertical = 16.dp,
            cardPadding = 16.dp,
            listItemGap = 8.dp,
            listItemPadding = 12.dp,
            buttonHorizontal = 20.dp,
            buttonIconGap = 8.dp,
            sheetPadding = 20.dp,
        )

        /** Variante densa (~75%) para tablets, dashboards e listas longas. */
        public val Compact: MnsSpacing = Default.scaledBy(0.75f)

        /** Variante folgada (~125%) para telas de destaque e onboarding. */
        public val Comfortable: MnsSpacing = Default.scaledBy(1.25f)
    }

    /** Multiplica toda a grade por [factor], arredondando para o dp mais próximo. */
    public fun scaledBy(factor: Float): MnsSpacing {
        fun Dp.s(): Dp = (value * factor).dp
        return MnsSpacing(
            none, xxs.s(), xs.s(), sm.s(), md.s(), base.s(), lg.s(), xl.s(),
            xxl.s(), xxxl.s(), huge.s(),
            screenHorizontal.s(), screenVertical.s(), cardPadding.s(),
            listItemGap.s(), listItemPadding.s(), buttonHorizontal.s(),
            buttonIconGap.s(), sheetPadding.s(),
        )
    }
}
