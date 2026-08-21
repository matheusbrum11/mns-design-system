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
 * **Preset 1 de 3 — `indigo-ticket`.**
 *
 * Extraído do design de *ticketing* (seleção de assento + checkout): fundo
 * quase branco, uma única cor de marca índigo saturada carregando todas as
 * ações, cantos moderados (12dp) e listas separadas por fio de cabelo.
 *
 * É o preset **padrão** da biblioteca por ser o mais neutro dos três: uma cor
 * de marca, contraste alto e nenhuma decoração que dispute com o conteúdo.
 *
 * Cores-chave lidas do print de referência:
 * | Token              | Valor     | Onde aparece no design            |
 * |--------------------|-----------|-----------------------------------|
 * | `primary`          | `#6255F4` | Botões "Confirm" e "Pay Now"      |
 * | `accent`           | `#A197FF` | Assento selecionado               |
 * | `primaryContainer` | `#EEEDFE` | Arco do palco, chips de preço     |
 * | `background`       | `#F8F8F8` | Fundo da tela                     |
 * | `surfaceVariant`   | `#E6E8EC` | Assento disponível                |
 */
public object MnsIndigoTicket : MnsThemeProvider {

    override val id: String = "indigo-ticket"
    override val displayName: String = "Indigo Ticket"

    /** Índigo da marca — a única cor saturada do tema. */
    public val Indigo: Color = Color(0xFF6255F4)

    /** Lavanda de destaque — assentos selecionados, badges "novo". */
    public val Lavender: Color = Color(0xFFA197FF)

    private val shapes: MnsShapes = MnsShapes.fromBaseRadius(
        base = 12.dp,
        buttonRadius = 16.dp,
    )

    override val light: MnsThemeSpec = MnsThemeSpec(
        id = id,
        name = displayName,
        isDark = false,
        colors = MnsColors.light(
            primary = Indigo,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFEEEDFE),
            onPrimaryContainer = Color(0xFF241C86),
            primaryPressed = Color(0xFF4B3FD6),
            secondary = Color(0xFF2F2A55),
            secondaryContainer = Color(0xFFE7E6F2),
            accent = Lavender,
            onAccent = Color.White,
            accentContainer = Color(0xFFFAF3FF),
            onAccentContainer = Color(0xFF3B2A7A),
            background = Color(0xFFF8F8F8),
            onBackground = Color(0xFF111114),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF111114),
            surfaceVariant = Color(0xFFE6E8EC),
            onSurfaceVariant = Color(0xFF5A5C66),
            outline = Color(0xFFE4E4E9),
            outlineVariant = Color(0xFFEFEFF2),
            textSecondary = Color(0xFF5A5C66),
            textTertiary = Color(0xFF9295A1),
        ),
        typography = MnsTypography.default(),
        shapes = shapes,
        spacing = MnsSpacing.Default,
        sizing = MnsSizing.Default,
        elevation = MnsElevation.Light,
        borders = MnsBorders.Default,
        opacity = MnsOpacity.Default,
        motion = MnsMotion.Default,
    )

    override val dark: MnsThemeSpec = light.copy(
        id = "$id-dark",
        isDark = true,
        colors = MnsColors.dark(
            primary = Color(0xFF8B80FF),
            onPrimary = Color(0xFF14104A),
            primaryContainer = Color(0xFF2B2470),
            onPrimaryContainer = Color(0xFFDFDBFF),
            accent = Color(0xFFA197FF),
            accentContainer = Color(0xFF322A63),
            background = Color(0xFF0E0E12),
            surface = Color(0xFF17171D),
            surfaceVariant = Color(0xFF23232B),
        ),
        elevation = MnsElevation.Dark,
    )
}
