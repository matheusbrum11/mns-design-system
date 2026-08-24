package com.mns.designsystem.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.mns.designsystem.foundation.MnsRippleIndication
import com.mns.designsystem.theme.preset.MnsIndigoTicket
import com.mns.designsystem.token.MnsBorders
import com.mns.designsystem.token.MnsColors
import com.mns.designsystem.token.MnsElevation
import com.mns.designsystem.token.MnsMotion
import com.mns.designsystem.token.MnsOpacity
import com.mns.designsystem.token.MnsShapes
import com.mns.designsystem.token.MnsSizing
import com.mns.designsystem.token.MnsSpacing
import com.mns.designsystem.token.MnsTypography

/**
 * Raiz do design system. Publica todos os tokens via `CompositionLocal` para a
 * subárvore e é o único lugar do app que precisa saber qual tema está ativo.
 *
 * ```kotlin
 * setContent {
 *     MnsTheme(provider = MnsIndigoTicket) {
 *         MnsScaffold(topBar = { ... }) { ... }
 *     }
 * }
 * ```
 *
 * @param provider fonte dos tokens. Troque este parâmetro para trocar a marca.
 * @param darkTheme se `true`, usa a variante escura do [provider]. Por padrão
 *   segue a preferência do sistema.
 * @param spec escape hatch: passe um [MnsThemeSpec] pronto e o [provider] e
 *   [darkTheme] são ignorados. É o caminho usado pelo playground do `app_demo`,
 *   que edita tokens em tempo real.
 */
@Composable
public fun MnsTheme(
    provider: MnsThemeProvider = MnsIndigoTicket,
    darkTheme: Boolean = isSystemInDarkTheme(),
    spec: MnsThemeSpec = provider.specFor(darkTheme),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMnsThemeSpec provides spec,
        LocalMnsColors provides spec.colors,
        LocalMnsTypography provides spec.typography,
        LocalMnsShapes provides spec.shapes,
        LocalMnsSpacing provides spec.spacing,
        LocalMnsSizing provides spec.sizing,
        LocalMnsElevation provides spec.elevation,
        LocalMnsBorders provides spec.borders,
        LocalMnsOpacity provides spec.opacity,
        LocalMnsMotion provides spec.motion,
        LocalContentColor provides spec.colors.onBackground,
        LocalMnsTextStyle provides spec.typography.bodyMedium,
        LocalIndication provides MnsRippleIndication(spec.colors, spec.opacity),
        content = content,
    )
}

/**
 * Acessor dos tokens do tema corrente. Prefira sempre `MnsTheme.colors.primary`
 * a qualquer literal de cor — é a regra que o code review do projeto cobra.
 */
public object MnsTheme {
    /** Spec completo em vigor. */
    public val spec: MnsThemeSpec
        @Composable @ReadOnlyComposable get() = LocalMnsThemeSpec.current

    public val colors: MnsColors
        @Composable @ReadOnlyComposable get() = LocalMnsColors.current

    public val typography: MnsTypography
        @Composable @ReadOnlyComposable get() = LocalMnsTypography.current

    public val shapes: MnsShapes
        @Composable @ReadOnlyComposable get() = LocalMnsShapes.current

    public val spacing: MnsSpacing
        @Composable @ReadOnlyComposable get() = LocalMnsSpacing.current

    public val sizing: MnsSizing
        @Composable @ReadOnlyComposable get() = LocalMnsSizing.current

    public val elevation: MnsElevation
        @Composable @ReadOnlyComposable get() = LocalMnsElevation.current

    public val borders: MnsBorders
        @Composable @ReadOnlyComposable get() = LocalMnsBorders.current

    public val opacity: MnsOpacity
        @Composable @ReadOnlyComposable get() = LocalMnsOpacity.current

    public val motion: MnsMotion
        @Composable @ReadOnlyComposable get() = LocalMnsMotion.current
}

// ─────────────────────────────────────────────────────────────────────────────
//  CompositionLocals
//  São `static` porque tokens mudam raramente (troca de tema inteiro). Isso
//  evita o custo de rastrear leitura individual em cada componente.
// ─────────────────────────────────────────────────────────────────────────────

/** Não chame diretamente: use [MnsTheme.spec]. */
public val LocalMnsThemeSpec: ProvidableCompositionLocal<MnsThemeSpec> =
    staticCompositionLocalOf { MnsIndigoTicket.light }

/** Não chame diretamente: use [MnsTheme.colors]. */
public val LocalMnsColors: ProvidableCompositionLocal<MnsColors> =
    staticCompositionLocalOf { MnsIndigoTicket.light.colors }

/** Não chame diretamente: use [MnsTheme.typography]. */
public val LocalMnsTypography: ProvidableCompositionLocal<MnsTypography> =
    staticCompositionLocalOf { MnsIndigoTicket.light.typography }

/** Não chame diretamente: use [MnsTheme.shapes]. */
public val LocalMnsShapes: ProvidableCompositionLocal<MnsShapes> =
    staticCompositionLocalOf { MnsIndigoTicket.light.shapes }

/** Não chame diretamente: use [MnsTheme.spacing]. */
public val LocalMnsSpacing: ProvidableCompositionLocal<MnsSpacing> =
    staticCompositionLocalOf { MnsIndigoTicket.light.spacing }

/** Não chame diretamente: use [MnsTheme.sizing]. */
public val LocalMnsSizing: ProvidableCompositionLocal<MnsSizing> =
    staticCompositionLocalOf { MnsIndigoTicket.light.sizing }

/** Não chame diretamente: use [MnsTheme.elevation]. */
public val LocalMnsElevation: ProvidableCompositionLocal<MnsElevation> =
    staticCompositionLocalOf { MnsIndigoTicket.light.elevation }

/** Não chame diretamente: use [MnsTheme.borders]. */
public val LocalMnsBorders: ProvidableCompositionLocal<MnsBorders> =
    staticCompositionLocalOf { MnsIndigoTicket.light.borders }

/** Não chame diretamente: use [MnsTheme.opacity]. */
public val LocalMnsOpacity: ProvidableCompositionLocal<MnsOpacity> =
    staticCompositionLocalOf { MnsIndigoTicket.light.opacity }

/** Não chame diretamente: use [MnsTheme.motion]. */
public val LocalMnsMotion: ProvidableCompositionLocal<MnsMotion> =
    staticCompositionLocalOf { MnsIndigoTicket.light.motion }

/**
 * Estilo de texto herdado pela subárvore. `MnsText` usa este valor quando
 * nenhum `style` é passado, o que permite a um container (item de lista, card)
 * definir a tipografia dos filhos sem repetir o parâmetro.
 */
public val LocalMnsTextStyle: ProvidableCompositionLocal<TextStyle> =
    staticCompositionLocalOf { MnsIndigoTicket.light.typography.bodyMedium }
