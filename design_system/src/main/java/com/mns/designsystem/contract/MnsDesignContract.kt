package com.mns.designsystem.contract

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **Contrato de tokenização do MNS.**
 *
 * É a representação portátil (JSON) de um tema completo. Serve a três usos:
 *
 * 1. **Entrada** — o agente `mns-design-contract` lê um print de design e
 *    emite este JSON com as cores, formas e espaçamentos identificados.
 * 2. **Transporte** — o app carrega o JSON (asset, remote config, CMS) e
 *    aplica o tema sem recompilar.
 * 3. **Saída** — o playground do `app_demo` exporta os tokens editados ao vivo
 *    de volta para este formato, fechando o ciclo design → código → design.
 *
 * O contrato é deliberadamente **parcial**: só `identity` e `colors.primary`
 * são obrigatórios. Todo o resto é derivado por [MnsDesignContractCodec] a
 * partir dos defaults do design system. Um print raramente revela 60 cores; o
 * contrato precisa funcionar com o que dá para ver.
 *
 * ```json
 * {
 *   "schemaVersion": 1,
 *   "identity": { "id": "acme", "name": "ACME" },
 *   "colors": { "primary": "#6255F4", "background": "#F8F8F8" },
 *   "shapes": { "baseRadiusDp": 12, "buttonRadiusDp": 16 }
 * }
 * ```
 *
 * @property schemaVersion versão do formato. Incrementada só em mudança
 *   incompatível; o codec recusa versões que não conhece.
 * @property identity identificação do tema.
 * @property colors cores em hexadecimal.
 * @property shapes raios de canto.
 * @property typography escala tipográfica.
 * @property spacing densidade de espaçamento.
 * @property elevation estilo de sombra.
 * @property motion durações de animação.
 * @property source rastreabilidade: de onde este contrato veio.
 */
@Serializable
public data class MnsDesignContract(
    @SerialName("schemaVersion") val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val identity: ContractIdentity,
    val colors: ContractColors,
    val shapes: ContractShapes = ContractShapes(),
    val typography: ContractTypography = ContractTypography(),
    val spacing: ContractSpacing = ContractSpacing(),
    val elevation: ContractElevation = ContractElevation(),
    val motion: ContractMotion = ContractMotion(),
    val source: ContractSource? = null,
) {
    public companion object {
        /** Versão de schema que este build entende. */
        public const val CURRENT_SCHEMA_VERSION: Int = 1
    }
}

/**
 * Identificação do tema.
 *
 * @property id slug estável em kebab-case. Vira o `MnsThemeSpec.id`.
 * @property name nome de exibição.
 * @property dark se `true`, o contrato descreve a variante escura.
 * @property description resumo em uma linha da intenção visual.
 */
@Serializable
public data class ContractIdentity(
    val id: String,
    val name: String,
    val dark: Boolean = false,
    val description: String? = null,
)

/**
 * Cores do contrato, em `#RRGGBB` ou `#AARRGGBB`.
 *
 * Apenas [primary] é obrigatória. Qualquer campo nulo é derivado pelas fábricas
 * `MnsColors.light` / `MnsColors.dark`, que interpolam a partir da primária.
 *
 * @property primary cor da ação principal. **Obrigatória.**
 * @property onPrimary conteúdo sobre a primária; derivada por contraste se ausente.
 * @property primaryContainer fundo suave da família primária.
 * @property onPrimaryContainer conteúdo sobre o container primário.
 * @property secondary cor de apoio.
 * @property secondaryContainer fundo suave secundário.
 * @property accent cor de destaque pontual.
 * @property accentContainer fundo suave de destaque.
 * @property background fundo da tela.
 * @property surface fundo de cards e sheets.
 * @property surfaceVariant superfície de segundo nível (inputs, linhas).
 * @property outline cor de bordas visíveis.
 * @property outlineVariant cor de divisores.
 * @property textPrimary texto de maior ênfase.
 * @property textSecondary texto de apoio.
 * @property textTertiary texto de menor ênfase.
 * @property success cor de sucesso.
 * @property warning cor de atenção.
 * @property danger cor de erro/destrutivo.
 * @property info cor informativa.
 */
@Serializable
public data class ContractColors(
    val primary: String,
    val onPrimary: String? = null,
    val primaryContainer: String? = null,
    val onPrimaryContainer: String? = null,
    val secondary: String? = null,
    val secondaryContainer: String? = null,
    val accent: String? = null,
    val accentContainer: String? = null,
    val background: String? = null,
    val surface: String? = null,
    val surfaceVariant: String? = null,
    val outline: String? = null,
    val outlineVariant: String? = null,
    val textPrimary: String? = null,
    val textSecondary: String? = null,
    val textTertiary: String? = null,
    val success: String? = null,
    val warning: String? = null,
    val danger: String? = null,
    val info: String? = null,
)

/**
 * Raios de canto identificados no design.
 *
 * @property baseRadiusDp raio dos cards — é dele que sai a escala inteira.
 * @property buttonRadiusDp raio dos botões; `null` deriva de [baseRadiusDp].
 * @property pillButtons quando `true`, botões viram pílula (50%) e
 *   [buttonRadiusDp] é ignorado.
 */
@Serializable
public data class ContractShapes(
    val baseRadiusDp: Int = 16,
    val buttonRadiusDp: Int? = null,
    val pillButtons: Boolean = false,
)

/**
 * Escala tipográfica.
 *
 * @property fontFamily nome da família (`"Inter"`, `"Poppins"`). Puramente
 *   informativo: registrar a fonte é responsabilidade do app, que passa a
 *   `FontFamily` para `MnsTypography.default()`.
 * @property scale multiplicador aplicado a toda a escala. `1.0` = padrão.
 */
@Serializable
public data class ContractTypography(
    val fontFamily: String? = null,
    val scale: Float = 1.0f,
)

/**
 * Densidade de espaçamento.
 *
 * @property density `"compact"`, `"default"` ou `"comfortable"`.
 * @property scale multiplicador extra aplicado sobre a densidade escolhida.
 */
@Serializable
public data class ContractSpacing(
    val density: String = "default",
    val scale: Float = 1.0f,
)

/**
 * Estilo de sombra.
 *
 * @property style `"light"`, `"dark"` ou `"flat"`.
 * @property shadowAlpha sobrescreve a opacidade da sombra projetada.
 */
@Serializable
public data class ContractElevation(
    val style: String = "light",
    val shadowAlpha: Float? = null,
)

/**
 * Movimento.
 *
 * @property durationScale multiplica todas as durações. `0f` desliga animações.
 * @property reduceMotion força o modo sem movimento.
 */
@Serializable
public data class ContractMotion(
    val durationScale: Float = 1.0f,
    val reduceMotion: Boolean = false,
)

/**
 * Rastreabilidade do contrato.
 *
 * @property kind origem: `"screenshot"`, `"figma"`, `"manual"`, `"playground"`.
 * @property reference caminho do print, URL do arquivo de design ou nota livre.
 * @property generatedAt timestamp ISO-8601 da geração.
 * @property notes observações do agente sobre o que foi inferido versus lido.
 *   É o campo que evita alguém tratar uma cor chutada como cor oficial.
 */
@Serializable
public data class ContractSource(
    val kind: String,
    val reference: String? = null,
    val generatedAt: String? = null,
    val notes: List<String> = emptyList(),
)
