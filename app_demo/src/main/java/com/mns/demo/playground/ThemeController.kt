package com.mns.demo.playground

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mns.designsystem.contract.ContractSource
import com.mns.designsystem.contract.MnsDesignContractCodec
import com.mns.designsystem.theme.MnsThemeProvider
import com.mns.designsystem.theme.MnsThemeSpec
import com.mns.designsystem.theme.preset.MnsThemePresets
import com.mns.designsystem.token.MnsElevation
import com.mns.designsystem.token.MnsShapes
import com.mns.designsystem.token.MnsSpacing

/** Densidade de espaçamento oferecida no playground. */
public enum class DemoDensity(public val label: String) {
    COMPACT("Compacto"),
    DEFAULT("Padrão"),
    COMFORTABLE("Confortável"),
}

/**
 * Estado do tema do `app_demo`.
 *
 * Mantém o preset escolhido e as sobrescritas feitas ao vivo, e recompõe o
 * [MnsThemeSpec] a cada mudança. É a peça que faz o app inteiro — não só o
 * preview — reagir à alteração de um token, que é o ponto do playground:
 * ver o efeito real de um token em uma tela real, não em um quadradinho.
 */
@Stable
public class ThemeController {

    /** Preset base. Trocar reinicia as sobrescritas de forma/densidade. */
    public var provider: MnsThemeProvider by mutableStateOf(MnsThemePresets.IndigoTicket)
        private set

    /** Modo escuro. */
    public var darkMode: Boolean by mutableStateOf(false)

    /** Sobrescrita da cor primária; `null` mantém a do preset. */
    public var primaryOverride: Color? by mutableStateOf(null)

    /** Raio-base dos cards, em dp. */
    public var baseRadius: Float by mutableStateOf(DEFAULT_RADIUS_SENTINEL)

    /** Botões em pílula. */
    public var pillButtons: Boolean by mutableStateOf(false)

    /** Densidade de espaçamento. */
    public var density: DemoDensity by mutableStateOf(DemoDensity.DEFAULT)

    /** Multiplicador da escala tipográfica. */
    public var typographyScale: Float by mutableStateOf(1f)

    /** Desliga animações (respeita o mesmo caminho de acessibilidade). */
    public var reduceMotion: Boolean by mutableStateOf(false)

    /** Remove todas as sombras. */
    public var flatElevation: Boolean by mutableStateOf(false)

    /** Troca o preset e limpa as sobrescritas. */
    public fun selectProvider(next: MnsThemeProvider) {
        provider = next
        primaryOverride = null
        baseRadius = DEFAULT_RADIUS_SENTINEL
        pillButtons = false
        density = DemoDensity.DEFAULT
        typographyScale = 1f
        flatElevation = false
    }

    /** Volta tudo ao estado inicial do preset atual. */
    public fun reset() {
        selectProvider(provider)
        reduceMotion = false
    }

    /**
     * O spec resultante do preset + sobrescritas.
     *
     * Recalculado a cada leitura em composição; como todos os campos lidos são
     * `MutableState`, qualquer alteração invalida exatamente quem depende dele.
     */
    public val spec: MnsThemeSpec
        get() {
            val base = provider.specFor(darkMode)
            val effectiveRadius = if (baseRadius == DEFAULT_RADIUS_SENTINEL) {
                base.shapes.baseRadius
            } else {
                baseRadius.dp
            }
            val effectivePill = pillButtons || base.shapes.buttonRadius == null

            val colors = primaryOverride?.let { override ->
                if (darkMode) {
                    base.colors.copy(primary = override, focusRing = override, textLink = override)
                } else {
                    base.colors.copy(
                        primary = override,
                        primaryPressed = override,
                        focusRing = override,
                        textLink = override,
                    )
                }
            } ?: base.colors

            val spacing = when (density) {
                DemoDensity.COMPACT -> MnsSpacing.Compact
                DemoDensity.DEFAULT -> base.spacing
                DemoDensity.COMFORTABLE -> MnsSpacing.Comfortable
            }

            return base.copy(
                colors = colors,
                shapes = MnsShapes.fromBaseRadius(
                    base = effectiveRadius,
                    buttonRadius = base.shapes.buttonRadius,
                    pillButtons = effectivePill,
                ),
                spacing = spacing,
                typography = if (typographyScale == 1f) {
                    base.typography
                } else {
                    base.typography.scaledBy(typographyScale)
                },
                elevation = if (flatElevation) MnsElevation.Flat else base.elevation,
                motion = base.motion.copy(reduceMotion = reduceMotion),
            )
        }

    /**
     * Exporta os tokens atuais como Design Contract em JSON.
     *
     * É o caminho de volta do playground para o design: o time ajusta os
     * tokens no app, exporta o contrato e cola no repositório — sem nenhuma
     * etapa de "transcrever à mão o hex do Figma".
     */
    public fun exportContractJson(): String = MnsDesignContractCodec.encode(
        MnsDesignContractCodec.fromThemeSpec(
            spec = spec,
            source = ContractSource(
                kind = "playground",
                reference = "app_demo · ${provider.displayName}",
                notes = listOf(
                    "Gerado pelo playground do app_demo a partir do preset '${provider.id}'.",
                    "Revise contraste antes de promover para producao.",
                ),
            ),
        ),
    )

    public companion object {
        /** Sentinela que significa "use o raio do preset". */
        public const val DEFAULT_RADIUS_SENTINEL: Float = -1f

        /** Paleta de cores primárias oferecida no playground. */
        public val primaryChoices: List<Pair<String, Color>> = listOf(
            "Indigo" to Color(0xFF6255F4),
            "Tinta" to Color(0xFF0A0A0A),
            "Turquesa" to Color(0xFF5CC9C9),
            "Pervinca" to Color(0xFF8079E8),
            "Laranja" to Color(0xFFFF6B00),
            "Verde" to Color(0xFF16A34A),
            "Rosa" to Color(0xFFEC4899),
        )

        /** `Saver` para preservar o estado do tema em mudança de configuração. */
        public val Saver: Saver<ThemeController, List<Any>> = Saver(
            save = {
                listOf(
                    it.provider.id,
                    it.darkMode,
                    it.baseRadius,
                    it.pillButtons,
                    it.density.name,
                    it.typographyScale,
                    it.reduceMotion,
                    it.flatElevation,
                )
            },
            restore = { saved ->
                ThemeController().apply {
                    provider = MnsThemePresets.byId(saved[0] as String)
                        ?: MnsThemePresets.IndigoTicket
                    darkMode = saved[1] as Boolean
                    baseRadius = saved[2] as Float
                    pillButtons = saved[3] as Boolean
                    density = DemoDensity.valueOf(saved[4] as String)
                    typographyScale = saved[5] as Float
                    reduceMotion = saved[6] as Boolean
                    flatElevation = saved[7] as Boolean
                }
            },
        )
    }
}
