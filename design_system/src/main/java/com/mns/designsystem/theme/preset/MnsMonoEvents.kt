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
 * **Preset 2 de 3 — `mono-events`.**
 *
 * Extraído do design de descoberta de eventos: interface essencialmente
 * monocromática (tinta preta sobre cinza claro), com cor entrando **só** onde
 * há seleção ou destaque — menta para "escolhido", lavanda para "em alta".
 *
 * É o preset certo quando o conteúdo é visual (fotos de evento, capas): a UI
 * some e a imagem manda. Botões em pílula 100%.
 *
 * Cores-chave lidas do print de referência:
 * | Token              | Valor     | Onde aparece no design            |
 * |--------------------|-----------|-----------------------------------|
 * | `primary`          | `#0A0A0A` | Botão-pílula de fechar, chip ativo|
 * | `primaryContainer` | `#D0F2E9` | Card de categoria selecionado     |
 * | `accentContainer`  | `#F5E8FF` | Card do evento em destaque         |
 * | `background`       | `#EBEBEB` | Fundo da tela                     |
 * | `surface`          | `#FFFFFF` | Cards                             |
 */
public object MnsMonoEvents : MnsThemeProvider {

    override val id: String = "mono-events"
    override val displayName: String = "Mono Events"

    /** Tinta da marca — quase preto, com um leve viés frio. */
    public val Ink: Color = Color(0xFF0A0A0A)

    /** Menta de seleção. */
    public val Mint: Color = Color(0xFFD0F2E9)

    /** Lavanda de destaque. */
    public val Lilac: Color = Color(0xFFF5E8FF)

    private val shapes: MnsShapes = MnsShapes.fromBaseRadius(
        base = 16.dp,
        pillButtons = true,
    )

    override val light: MnsThemeSpec = MnsThemeSpec(
        id = id,
        name = displayName,
        isDark = false,
        colors = MnsColors.light(
            primary = Ink,
            onPrimary = Color.White,
            primaryContainer = Mint,
            onPrimaryContainer = Color(0xFF0B3B30),
            primaryPressed = Color(0xFF2E2E2E),
            secondary = Color(0xFF3D6F62),
            secondaryContainer = Color(0xFFE4F7F1),
            onSecondaryContainer = Color(0xFF0B3B30),
            accent = Color(0xFF8B5CF6),
            onAccent = Color.White,
            accentContainer = Lilac,
            onAccentContainer = Color(0xFF3B1A66),
            background = Color(0xFFEBEBEB),
            onBackground = Ink,
            surface = Color(0xFFFFFFFF),
            onSurface = Ink,
            surfaceVariant = Color(0xFFF9F9F9),
            onSurfaceVariant = Color(0xFF6B6B6B),
            surfaceInverse = Ink,
            outline = Color(0xFFE2E2E2),
            outlineVariant = Color(0xFFF0F0F0),
            textSecondary = Color(0xFF5C5C5C),
            textTertiary = Color(0xFF868686),
            shimmerBase = Color(0xFFE6E6E6),
            shimmerHighlight = Color(0xFFF7F7F7),
        ),
        typography = MnsTypography.default(),
        shapes = shapes,
        spacing = MnsSpacing.Default,
        sizing = MnsSizing.Default,
        // Design praticamente sem sombra: a hierarquia vem do contraste
        // entre o cinza do fundo e o branco dos cards.
        elevation = MnsElevation.Light.copy(shadowAlpha = 0.05f, ambientAlpha = 0.03f),
        borders = MnsBorders.Default,
        opacity = MnsOpacity.Default,
        motion = MnsMotion.Default,
    )

    override val dark: MnsThemeSpec = light.copy(
        id = "$id-dark",
        isDark = true,
        colors = MnsColors.dark(
            primary = Color(0xFFF2F2F2),
            onPrimary = Color(0xFF0A0A0A),
            primaryContainer = Color(0xFF163B33),
            onPrimaryContainer = Mint,
            accent = Color(0xFFC4A6FF),
            accentContainer = Color(0xFF35245C),
            onAccentContainer = Lilac,
            background = Color(0xFF101010),
            surface = Color(0xFF1A1A1A),
            surfaceVariant = Color(0xFF242424),
            outline = Color(0xFF333333),
        ),
        elevation = MnsElevation.Dark,
    )
}
