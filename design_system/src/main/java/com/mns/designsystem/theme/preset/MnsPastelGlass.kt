package com.mns.designsystem.theme.preset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mns.designsystem.theme.MnsThemeProvider
import com.mns.designsystem.theme.MnsThemeSpec
import com.mns.designsystem.token.MnsBorders
import com.mns.designsystem.token.MnsColors
import com.mns.designsystem.token.MnsElevation
import com.mns.designsystem.token.MnsMotion
import com.mns.designsystem.token.MnsOpacity
import com.mns.designsystem.token.MnsShapes
import com.mns.designsystem.token.MnsSizing
import com.mns.designsystem.token.MnsSpacing
import com.mns.designsystem.token.MnsTypography
import com.mns.designsystem.token.dark
import com.mns.designsystem.token.light

/**
 * **Preset 3 de 3 — `pastel-glass`.**
 *
 * Extraído do design de viagens: fundo lilás claríssimo, cards muito
 * arredondados (20dp) que parecem vidro sobre a página, paleta pastel de
 * suporte (turquesa e pervinca) e ações em pílula preta.
 *
 * É o preset mais "decorativo" dos três — bom para produtos de lazer, ruim para
 * telas densas de dados. Espaçamento `Comfortable` por padrão.
 *
 * Cores-chave lidas do print de referência:
 * | Token                | Valor     | Onde aparece no design           |
 * |----------------------|-----------|----------------------------------|
 * | `primary`            | `#12111A` | Botão "Search", "See Details"    |
 * | `secondary`          | `#5CC9C9` | Ilustração do trem, chip ativo   |
 * | `accent`             | `#8079E8` | Voucher, banner de desconto      |
 * | `background`         | `#F4F5FC` | Fundo da tela                    |
 * | `secondaryContainer` | `#D5EBF1` | Cards de categoria (Trains/Boats)|
 */
public object MnsPastelGlass : MnsThemeProvider {

    override val id: String = "pastel-glass"
    override val displayName: String = "Pastel Glass"

    /** Tinta das ações — quase preto com viés azulado. */
    public val Ink: Color = Color(0xFF12111A)

    /** Turquesa pastel de suporte. */
    public val Turquoise: Color = Color(0xFF5CC9C9)

    /** Pervinca de destaque (vouchers, promoções). */
    public val Periwinkle: Color = Color(0xFF8079E8)

    /** Pêssego pastel — a quarta categoria do design original. */
    public val Peach: Color = Color(0xFFF3D2C4)

    private val shapes: MnsShapes = MnsShapes.fromBaseRadius(
        base = 20.dp,
        pillButtons = true,
    )

    override val light: MnsThemeSpec = MnsThemeSpec(
        id = id,
        name = displayName,
        isDark = false,
        colors = MnsColors.light(
            primary = Ink,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE4E1FB),
            onPrimaryContainer = Color(0xFF241F52),
            primaryPressed = Color(0xFF33314A),
            secondary = Turquoise,
            onSecondary = Color(0xFF04302F),
            secondaryContainer = Color(0xFFD5EBF1),
            onSecondaryContainer = Color(0xFF0A3B44),
            accent = Periwinkle,
            onAccent = Color.White,
            accentContainer = Color(0xFFE4E1FB),
            onAccentContainer = Color(0xFF2E2879),
            background = Color(0xFFF4F5FC),
            onBackground = Color(0xFF15141F),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF15141F),
            surfaceVariant = Color(0xFFF0EDF6),
            onSurfaceVariant = Color(0xFF5D5A70),
            surfaceElevated = Color(0xFFFDFCFF),
            outline = Color(0xFFE6E3F0),
            outlineVariant = Color(0xFFF1EEF8),
            textSecondary = Color(0xFF5D5A70),
            textTertiary = Color(0xFF908DA3),
            shimmerBase = Color(0xFFEAE7F4),
            shimmerHighlight = Color(0xFFF9F8FE),
            shadow = Color(0xFF2A2450),
        ),
        typography = MnsTypography.default(),
        shapes = shapes,
        spacing = MnsSpacing.Comfortable,
        sizing = MnsSizing.Default,
        elevation = MnsElevation.Light.copy(shadowAlpha = 0.08f, ambientAlpha = 0.05f),
        borders = MnsBorders.Default,
        opacity = MnsOpacity.Default,
        motion = MnsMotion.Default,
    )

    override val dark: MnsThemeSpec = light.copy(
        id = "$id-dark",
        isDark = true,
        colors = MnsColors.dark(
            primary = Color(0xFFEDEBFF),
            onPrimary = Color(0xFF12111A),
            primaryContainer = Color(0xFF2E2879),
            onPrimaryContainer = Color(0xFFE4E1FB),
            secondary = Turquoise,
            onSecondary = Color(0xFF04302F),
            secondaryContainer = Color(0xFF11414A),
            onSecondaryContainer = Color(0xFFD5EBF1),
            accent = Periwinkle,
            accentContainer = Color(0xFF2E2879),
            background = Color(0xFF11101A),
            surface = Color(0xFF1A1926),
            surfaceVariant = Color(0xFF252336),
            outline = Color(0xFF35334A),
        ),
        elevation = MnsElevation.Dark,
    )
}
