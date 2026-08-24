package com.mns.designsystem.theme

import androidx.compose.runtime.Immutable
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
 * **O contrato de tokenização do MNS.**
 *
 * Um [MnsThemeSpec] é o pacote completo de tokens que define uma identidade
 * visual. Tudo que os componentes desenham sai daqui — não existe nenhuma cor,
 * raio ou espaçamento hard-coded dentro de um componente do design system.
 *
 * Há três formas de obter um spec, em ordem crescente de esforço:
 *
 * 1. **Usar um preset** — `MnsIndigoTicket.light`
 * 2. **Derivar de um preset** — `MnsIndigoTicket.light.copy(colors = ...)`
 * 3. **Gerar a partir de um print** — via `MnsDesignContract`, que produz um
 *    JSON de tokens que é desserializado neste mesmo tipo.
 *
 * @property id identificador estável e legível por máquina (`indigo-ticket`).
 *   Aparece no JSON do Design Contract e nos testes de snapshot.
 * @property name nome de exibição do tema.
 * @property isDark se este spec representa a variante escura.
 */
@Immutable
public data class MnsThemeSpec(
    val id: String,
    val name: String,
    val isDark: Boolean,
    val colors: MnsColors,
    val typography: MnsTypography,
    val shapes: MnsShapes,
    val spacing: MnsSpacing,
    val sizing: MnsSizing,
    val elevation: MnsElevation,
    val borders: MnsBorders,
    val opacity: MnsOpacity,
    val motion: MnsMotion,
) {
    public companion object
}

/**
 * **Interface de implementação padrão de um tema.**
 *
 * É o ponto de extensão oficial para quem consome a biblioteca: implemente
 * [MnsThemeProvider] no seu app e você passa a controlar 100% dos tokens, sem
 * fork e sem tocar em nenhum componente.
 *
 * ```kotlin
 * object MeuTemaDaEmpresa : MnsThemeProvider {
 *     override val id = "acme"
 *     override val displayName = "ACME"
 *     override val light = MnsIndigoTicket.light.copy(
 *         id = "acme",
 *         colors = MnsIndigoTicket.light.colors.copy(primary = Color(0xFFFF6B00)),
 *     )
 *     override val dark = MnsIndigoTicket.dark.copy(id = "acme-dark")
 * }
 *
 * // No app:
 * MnsTheme(provider = MeuTemaDaEmpresa) { AppRoot() }
 * ```
 *
 * Um provider que não suporta modo escuro simplesmente devolve o mesmo spec em
 * [dark] — [supportsDarkMode] passa a reportar `false` e a UI para de oferecer
 * o toggle.
 */
public interface MnsThemeProvider {
    /** Identificador estável do tema (kebab-case). */
    public val id: String

    /** Nome legível, exibido em seletores de tema. */
    public val displayName: String

    /** Tokens da variante clara. */
    public val light: MnsThemeSpec

    /** Tokens da variante escura. */
    public val dark: MnsThemeSpec

    /** `false` quando [dark] é apenas um alias de [light]. */
    public val supportsDarkMode: Boolean
        get() = light !== dark && dark.isDark

    /** Resolve o spec para o modo pedido. */
    public fun specFor(darkMode: Boolean): MnsThemeSpec =
        if (darkMode && supportsDarkMode) dark else light
}

/**
 * Provider mínimo construído a partir de dois specs. Útil quando o tema já vem
 * pronto (por exemplo, desserializado de um Design Contract) e não vale a pena
 * declarar um `object`.
 */
public class MnsSimpleThemeProvider(
    override val id: String,
    override val displayName: String,
    override val light: MnsThemeSpec,
    override val dark: MnsThemeSpec = light,
) : MnsThemeProvider
