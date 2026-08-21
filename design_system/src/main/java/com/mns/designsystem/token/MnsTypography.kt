package com.mns.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * **Semantic tokens de tipografia.**
 *
 * A escala é fechada em 16 papéis. Se um design pede um tamanho que não existe
 * aqui, a resposta certa quase sempre é usar o papel mais próximo — não criar
 * o 17º. Escalas abertas são a principal causa de deriva visual em design
 * systems longevos.
 *
 * Trocar a fonte do produto inteiro é uma linha:
 * ```kotlin
 * val tipografia = MnsTypography.default(fontFamily = FontFamily(Font(R.font.inter)))
 * ```
 */
@Immutable
public data class MnsTypography(
    /** Números e títulos heroicos de tela de abertura. Use no máximo 1 por tela. */
    val displayLarge: TextStyle,
    /** Título de destaque de uma seção-herói. */
    val displayMedium: TextStyle,
    /** Título de destaque compacto. */
    val displaySmall: TextStyle,
    /** Título principal de tela (ex.: "Today Events"). */
    val headlineLarge: TextStyle,
    /** Título de bloco dentro da tela. */
    val headlineMedium: TextStyle,
    /** Título de sub-bloco. */
    val headlineSmall: TextStyle,
    /** Título de card e de item de lista com ênfase. */
    val titleLarge: TextStyle,
    /** Título de item de lista padrão. */
    val titleMedium: TextStyle,
    /** Título compacto — cabeçalho de agrupamento. */
    val titleSmall: TextStyle,
    /** Corpo de texto longo (descrição de evento, termos). */
    val bodyLarge: TextStyle,
    /** Corpo de texto padrão do app. */
    val bodyMedium: TextStyle,
    /** Corpo compacto — legenda de card. */
    val bodySmall: TextStyle,
    /** Rótulo de botão e de chip. */
    val labelLarge: TextStyle,
    /** Rótulo de campo de formulário e de tab. */
    val labelMedium: TextStyle,
    /** Rótulo mínimo — badge, contador. */
    val labelSmall: TextStyle,
    /** Metadado auxiliar: data, hora, "há 3 min". */
    val caption: TextStyle,
    /** Texto em caixa alta com tracking largo, para seções de catálogo. */
    val overline: TextStyle,
    /** Fonte monoespaçada — códigos de ingresso, hashes, valores alinhados. */
    val mono: TextStyle,
) {
    /** Aplica uma [FontFamily] a todos os papéis de uma vez. */
    public fun withFontFamily(fontFamily: FontFamily): MnsTypography = MnsTypography(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
        caption = caption.copy(fontFamily = fontFamily),
        overline = overline.copy(fontFamily = fontFamily),
        // `mono` fica de fora de propósito: código/ingresso precisa de largura fixa.
        mono = mono,
    )

    /**
     * Multiplica todos os tamanhos por [factor], preservando a proporção da
     * escala. Útil para densidades compactas (tablet dashboard) sem duplicar
     * a definição inteira.
     */
    public fun scaledBy(factor: Float): MnsTypography {
        fun TextStyle.scale(): TextStyle = copy(
            fontSize = fontSize.scaleSp(factor),
            lineHeight = lineHeight.scaleSp(factor),
        )
        return MnsTypography(
            displayLarge.scale(), displayMedium.scale(), displaySmall.scale(),
            headlineLarge.scale(), headlineMedium.scale(), headlineSmall.scale(),
            titleLarge.scale(), titleMedium.scale(), titleSmall.scale(),
            bodyLarge.scale(), bodyMedium.scale(), bodySmall.scale(),
            labelLarge.scale(), labelMedium.scale(), labelSmall.scale(),
            caption.scale(), overline.scale(), mono.scale(),
        )
    }

    public companion object {
        private val lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        )

        private fun style(
            size: Int,
            lineHeight: Int,
            weight: FontWeight,
            tracking: TextUnit = 0.sp,
            family: FontFamily = FontFamily.Default,
        ): TextStyle = TextStyle(
            fontFamily = family,
            fontSize = size.sp,
            lineHeight = lineHeight.sp,
            fontWeight = weight,
            letterSpacing = tracking,
            lineHeightStyle = lineHeightStyle,
        )

        /**
         * Escala padrão do MNS: base de 16sp, razão ~1.25, pesos entre 400 e 700.
         * Foi calibrada em telas de 360dp — o menor alvo suportado (minSdk 24).
         */
        public fun default(fontFamily: FontFamily = FontFamily.Default): MnsTypography =
            MnsTypography(
                displayLarge = style(40, 46, FontWeight.Bold, (-0.02).em, fontFamily),
                displayMedium = style(34, 40, FontWeight.Bold, (-0.02).em, fontFamily),
                displaySmall = style(28, 34, FontWeight.Bold, (-0.01).em, fontFamily),
                headlineLarge = style(24, 30, FontWeight.Bold, (-0.01).em, fontFamily),
                headlineMedium = style(20, 26, FontWeight.SemiBold, 0.sp, fontFamily),
                headlineSmall = style(18, 24, FontWeight.SemiBold, 0.sp, fontFamily),
                titleLarge = style(16, 22, FontWeight.SemiBold, 0.sp, fontFamily),
                titleMedium = style(15, 20, FontWeight.Medium, 0.sp, fontFamily),
                titleSmall = style(13, 18, FontWeight.Medium, 0.sp, fontFamily),
                bodyLarge = style(16, 24, FontWeight.Normal, 0.sp, fontFamily),
                bodyMedium = style(14, 20, FontWeight.Normal, 0.sp, fontFamily),
                bodySmall = style(12, 18, FontWeight.Normal, 0.sp, fontFamily),
                labelLarge = style(15, 20, FontWeight.SemiBold, 0.sp, fontFamily),
                labelMedium = style(13, 16, FontWeight.Medium, 0.sp, fontFamily),
                labelSmall = style(11, 14, FontWeight.Medium, 0.01.em, fontFamily),
                caption = style(11, 14, FontWeight.Normal, 0.sp, fontFamily),
                overline = style(10, 14, FontWeight.SemiBold, 0.08.em, fontFamily),
                mono = style(14, 20, FontWeight.Medium, 0.sp, FontFamily.Monospace),
            )
    }
}

private fun TextUnit.scaleSp(factor: Float): TextUnit =
    if (isSp) (value * factor).sp else this
