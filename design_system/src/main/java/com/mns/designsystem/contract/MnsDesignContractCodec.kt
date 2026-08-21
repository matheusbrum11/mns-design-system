package com.mns.designsystem.contract

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mns.designsystem.theme.MnsSimpleThemeProvider
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
import kotlinx.serialization.json.Json

/**
 * Converte entre [MnsDesignContract] (JSON portátil) e [MnsThemeSpec] (tokens
 * em memória).
 *
 * A conversão é **assimétrica de propósito**: ler um contrato preenche as
 * lacunas com derivações; exportar um spec grava só o que um print conseguiria
 * revelar. Isso mantém o JSON pequeno e legível por humanos, em vez de um dump
 * de 60 cores que ninguém revisa em pull request.
 */
public object MnsDesignContractCodec {

    /** Parser tolerante: ignora campos desconhecidos e aceita ausências. */
    public val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
        prettyPrintIndent = "  "
    }

    /**
     * Desserializa um contrato.
     *
     * @throws MnsContractException se o JSON for inválido ou o `schemaVersion`
     *   for maior que o suportado por este build.
     */
    public fun decode(rawJson: String): MnsDesignContract {
        val contract = runCatching { json.decodeFromString(MnsDesignContract.serializer(), rawJson) }
            .getOrElse { throw MnsContractException("JSON de contrato inválido: ${it.message}", it) }
        if (contract.schemaVersion > MnsDesignContract.CURRENT_SCHEMA_VERSION) {
            throw MnsContractException(
                "Contrato usa schemaVersion ${contract.schemaVersion}, mas este build " +
                    "suporta no máximo ${MnsDesignContract.CURRENT_SCHEMA_VERSION}. Atualize a biblioteca.",
            )
        }
        return contract
    }

    /** Serializa um contrato em JSON formatado. */
    public fun encode(contract: MnsDesignContract): String =
        json.encodeToString(MnsDesignContract.serializer(), contract)

    /**
     * Materializa um contrato em tokens prontos para o `MnsTheme`.
     *
     * @throws MnsContractException se alguma cor estiver em formato inválido.
     */
    public fun toThemeSpec(contract: MnsDesignContract): MnsThemeSpec {
        val isDark = contract.identity.dark
        val colors = buildColors(contract.colors, isDark)

        val baseRadius = contract.shapes.baseRadiusDp.coerceIn(0, 64).dp
        val buttonRadius = contract.shapes.buttonRadiusDp?.coerceIn(0, 64)?.dp

        val spacing = when (contract.spacing.density.lowercase()) {
            "compact" -> MnsSpacing.Compact
            "comfortable" -> MnsSpacing.Comfortable
            else -> MnsSpacing.Default
        }.let { if (contract.spacing.scale == 1f) it else it.scaledBy(contract.spacing.scale) }

        val elevation = when (contract.elevation.style.lowercase()) {
            "flat" -> MnsElevation.Flat
            "dark" -> MnsElevation.Dark
            else -> if (isDark) MnsElevation.Dark else MnsElevation.Light
        }.let { base ->
            contract.elevation.shadowAlpha?.let { base.copy(shadowAlpha = it) } ?: base
        }

        val motion = MnsMotion.Default.copy(reduceMotion = contract.motion.reduceMotion).let { base ->
            if (contract.motion.durationScale == 1f) {
                base
            } else {
                val s = contract.motion.durationScale
                base.copy(
                    durationFast = (base.durationFast * s).toInt(),
                    durationNormal = (base.durationNormal * s).toInt(),
                    durationSlow = (base.durationSlow * s).toInt(),
                    durationSlower = (base.durationSlower * s).toInt(),
                    durationShimmer = (base.durationShimmer * s).toInt(),
                )
            }
        }

        val typography = MnsTypography.default().let {
            if (contract.typography.scale == 1f) it else it.scaledBy(contract.typography.scale)
        }

        return MnsThemeSpec(
            id = contract.identity.id,
            name = contract.identity.name,
            isDark = isDark,
            colors = colors,
            typography = typography,
            shapes = MnsShapes.fromBaseRadius(
                base = baseRadius,
                buttonRadius = buttonRadius,
                pillButtons = contract.shapes.pillButtons,
            ),
            spacing = spacing,
            sizing = MnsSizing.Default,
            elevation = elevation,
            borders = MnsBorders.Default,
            opacity = MnsOpacity.Default,
            motion = motion,
        )
    }

    /**
     * Atalho: JSON → provider pronto para o `MnsTheme`.
     *
     * Quando o contrato descreve só a variante clara, a escura é derivada
     * automaticamente a partir das mesmas cores de marca.
     */
    public fun toProvider(rawJson: String): MnsThemeProvider {
        val contract = decode(rawJson)
        val spec = toThemeSpec(contract)
        val darkSpec = if (contract.identity.dark) {
            spec
        } else {
            toThemeSpec(
                contract.copy(
                    identity = contract.identity.copy(id = "${contract.identity.id}-dark", dark = true),
                ),
            )
        }
        return MnsSimpleThemeProvider(
            id = contract.identity.id,
            displayName = contract.identity.name,
            light = if (contract.identity.dark) darkSpec else spec,
            dark = darkSpec,
        )
    }

    /**
     * Exporta um [MnsThemeSpec] de volta para contrato — usado pelo playground
     * do `app_demo` para gerar o JSON dos tokens editados ao vivo.
     *
     * @param spec tokens em memória.
     * @param source rastreabilidade a gravar no contrato.
     */
    public fun fromThemeSpec(
        spec: MnsThemeSpec,
        source: ContractSource? = ContractSource(kind = "playground"),
    ): MnsDesignContract = MnsDesignContract(
        identity = ContractIdentity(id = spec.id, name = spec.name, dark = spec.isDark),
        colors = ContractColors(
            primary = spec.colors.primary.toHex(),
            onPrimary = spec.colors.onPrimary.toHex(),
            primaryContainer = spec.colors.primaryContainer.toHex(),
            onPrimaryContainer = spec.colors.onPrimaryContainer.toHex(),
            secondary = spec.colors.secondary.toHex(),
            secondaryContainer = spec.colors.secondaryContainer.toHex(),
            accent = spec.colors.accent.toHex(),
            accentContainer = spec.colors.accentContainer.toHex(),
            background = spec.colors.background.toHex(),
            surface = spec.colors.surface.toHex(),
            surfaceVariant = spec.colors.surfaceVariant.toHex(),
            outline = spec.colors.outline.toHex(),
            outlineVariant = spec.colors.outlineVariant.toHex(),
            textPrimary = spec.colors.textPrimary.toHex(),
            textSecondary = spec.colors.textSecondary.toHex(),
            textTertiary = spec.colors.textTertiary.toHex(),
            success = spec.colors.success.toHex(),
            warning = spec.colors.warning.toHex(),
            danger = spec.colors.danger.toHex(),
            info = spec.colors.info.toHex(),
        ),
        shapes = ContractShapes(
            baseRadiusDp = spec.shapes.baseRadius.value.toInt(),
            buttonRadiusDp = spec.shapes.buttonRadius?.value?.toInt(),
            pillButtons = spec.shapes.buttonRadius == null,
        ),
        spacing = ContractSpacing(density = "default", scale = 1f),
        elevation = ContractElevation(
            style = if (spec.isDark) "dark" else "light",
            shadowAlpha = spec.elevation.shadowAlpha,
        ),
        motion = ContractMotion(reduceMotion = spec.motion.reduceMotion),
        source = source,
    )

    private fun buildColors(contract: ContractColors, isDark: Boolean): MnsColors {
        val primary = contract.primary.toColor("colors.primary")
        return if (isDark) {
            MnsColors.dark(
                primary = primary,
                onPrimary = contract.onPrimary.toColorOrNull("colors.onPrimary")
                    ?: primary.autoContent(),
                primaryContainer = contract.primaryContainer.toColorOrNull("colors.primaryContainer")
                    ?: primary.blend(Color.Black, 0.62f),
                onPrimaryContainer = contract.onPrimaryContainer.toColorOrNull("colors.onPrimaryContainer")
                    ?: primary.blend(Color.White, 0.70f),
                secondary = contract.secondary.toColorOrNull("colors.secondary")
                    ?: primary.blend(Color.White, 0.22f),
                secondaryContainer = contract.secondaryContainer.toColorOrNull("colors.secondaryContainer")
                    ?: primary.blend(Color.Black, 0.66f),
                accent = contract.accent.toColorOrNull("colors.accent") ?: primary,
                accentContainer = contract.accentContainer.toColorOrNull("colors.accentContainer")
                    ?: primary.blend(Color.Black, 0.66f),
                background = contract.background.toColorOrNull("colors.background") ?: Color(0xFF0E0E12),
                surface = contract.surface.toColorOrNull("colors.surface") ?: Color(0xFF17171D),
                surfaceVariant = contract.surfaceVariant.toColorOrNull("colors.surfaceVariant")
                    ?: Color(0xFF23232B),
                outline = contract.outline.toColorOrNull("colors.outline") ?: Color(0xFF33333E),
                outlineVariant = contract.outlineVariant.toColorOrNull("colors.outlineVariant")
                    ?: Color(0xFF26262F),
                textSecondary = contract.textSecondary.toColorOrNull("colors.textSecondary")
                    ?: Color(0xFFA9A9B8),
                textTertiary = contract.textTertiary.toColorOrNull("colors.textTertiary")
                    ?: Color(0xFF7C7C8C),
                success = contract.success.toColorOrNull("colors.success") ?: Color(0xFF4ADE80),
                warning = contract.warning.toColorOrNull("colors.warning") ?: Color(0xFFFBBF24),
                danger = contract.danger.toColorOrNull("colors.danger") ?: Color(0xFFF87171),
                info = contract.info.toColorOrNull("colors.info") ?: Color(0xFF60A5FA),
            )
        } else {
            val surface = contract.surface.toColorOrNull("colors.surface") ?: Color(0xFFFFFFFF)
            val onSurface = contract.textPrimary.toColorOrNull("colors.textPrimary")
                ?: Color(0xFF15151A)
            MnsColors.light(
                primary = primary,
                onPrimary = contract.onPrimary.toColorOrNull("colors.onPrimary")
                    ?: primary.autoContent(),
                primaryContainer = contract.primaryContainer.toColorOrNull("colors.primaryContainer")
                    ?: primary.blend(Color.White, 0.88f),
                onPrimaryContainer = contract.onPrimaryContainer.toColorOrNull("colors.onPrimaryContainer")
                    ?: primary.blend(Color.Black, 0.42f),
                secondary = contract.secondary.toColorOrNull("colors.secondary")
                    ?: primary.blend(Color.Black, 0.20f),
                secondaryContainer = contract.secondaryContainer.toColorOrNull("colors.secondaryContainer")
                    ?: primary.blend(Color.White, 0.90f),
                accent = contract.accent.toColorOrNull("colors.accent") ?: primary,
                accentContainer = contract.accentContainer.toColorOrNull("colors.accentContainer")
                    ?: primary.blend(Color.White, 0.90f),
                background = contract.background.toColorOrNull("colors.background") ?: Color(0xFFF7F7F8),
                onBackground = onSurface,
                surface = surface,
                onSurface = onSurface,
                surfaceVariant = contract.surfaceVariant.toColorOrNull("colors.surfaceVariant")
                    ?: Color(0xFFF1F1F4),
                outline = contract.outline.toColorOrNull("colors.outline") ?: Color(0xFFDFDFE6),
                outlineVariant = contract.outlineVariant.toColorOrNull("colors.outlineVariant")
                    ?: Color(0xFFEDEDF2),
                success = contract.success.toColorOrNull("colors.success") ?: Color(0xFF16A34A),
                warning = contract.warning.toColorOrNull("colors.warning") ?: Color(0xFFD97706),
                danger = contract.danger.toColorOrNull("colors.danger") ?: Color(0xFFDC2626),
                info = contract.info.toColorOrNull("colors.info") ?: Color(0xFF2563EB),
                textPrimary = onSurface,
                textSecondary = contract.textSecondary.toColorOrNull("colors.textSecondary")
                    ?: Color(0xFF5B5B66),
                textTertiary = contract.textTertiary.toColorOrNull("colors.textTertiary")
                    ?: Color(0xFF8A8A96),
            )
        }
    }
}

/** Falha ao ler, validar ou converter um [MnsDesignContract]. */
public class MnsContractException(
    message: String,
    cause: Throwable? = null,
) : IllegalArgumentException(message, cause)

/**
 * Converte `"#RRGGBB"` ou `"#AARRGGBB"` em [Color].
 *
 * @param field nome do campo, usado só para produzir uma mensagem de erro que
 *   diga **onde** o JSON está errado.
 * @throws MnsContractException se o formato não for reconhecido.
 */
public fun String.toColor(field: String = "cor"): Color {
    val hex = trim().removePrefix("#")
    val valid = hex.length == 6 || hex.length == 8
    if (!valid || hex.any { it.digitToIntOrNull(16) == null }) {
        throw MnsContractException(
            "Valor de cor inválido em '$field': '$this'. Use #RRGGBB ou #AARRGGBB.",
        )
    }
    val argb = if (hex.length == 6) "FF$hex" else hex
    return Color(argb.toLong(16))
}

private fun String?.toColorOrNull(field: String): Color? = this?.toColor(field)

/** Formata a cor como `#AARRGGBB`, ou `#RRGGBB` quando totalmente opaca. */
public fun Color.toHex(): String {
    fun Float.channel(): Int = (this * 255f).toInt().coerceIn(0, 255)
    val a = alpha.channel()
    val body = "%02X%02X%02X".format(red.channel(), green.channel(), blue.channel())
    return if (a == 255) "#$body" else "#%02X%s".format(a, body)
}

private fun Color.blend(other: Color, ratio: Float): Color = Color(
    red = red + (other.red - red) * ratio,
    green = green + (other.green - green) * ratio,
    blue = blue + (other.blue - blue) * ratio,
    alpha = alpha,
)

private fun Color.autoContent(): Color {
    val luminance = 0.2126f * red + 0.7152f * green + 0.0722f * blue
    return if (luminance > 0.55f) Color(0xFF15151A) else Color.White
}
