package com.mns.designsystem.component.code

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Nível de correção de erro do QR.
 *
 * Quanto maior a correção, mais o código sobrevive a sujeira, dobra e logo
 * sobreposto — ao custo de mais módulos (código mais denso) para o mesmo dado.
 * Para ingresso impresso ou exibido em tela riscada, use [QUARTILE] ou [HIGH].
 */
public enum class MnsQrErrorCorrection(internal val zxing: ErrorCorrectionLevel) {
    /** ~7% de recuperação. Códigos curtos exibidos em tela limpa. */
    LOW(ErrorCorrectionLevel.L),

    /** ~15%. **Padrão** — bom equilíbrio. */
    MEDIUM(ErrorCorrectionLevel.M),

    /** ~25%. Use quando houver logo sobreposto. */
    QUARTILE(ErrorCorrectionLevel.Q),

    /** ~30%. Impressão em papel térmico, pulseira, ambiente hostil. */
    HIGH(ErrorCorrectionLevel.H),
}

/**
 * Codificador de QR Code do design system.
 *
 * É o único ponto da biblioteca que conhece o ZXing. Se amanhã trocarmos de
 * codificador, `MnsQrCode` não muda — ele só fala [MnsQrMatrix].
 */
public object MnsQrEncoder {

    /**
     * Codifica [content] em uma [MnsQrMatrix] **sem** zona de silêncio — a
     * margem é responsabilidade do componente de exibição, que a deriva dos
     * tokens de espaçamento.
     *
     * @param content texto a codificar. Não pode ser vazio.
     * @param errorCorrection nível de correção de erro.
     * @throws IllegalArgumentException se [content] for vazio.
     */
    public fun encode(
        content: String,
        errorCorrection: MnsQrErrorCorrection = MnsQrErrorCorrection.MEDIUM,
    ): MnsQrMatrix {
        require(content.isNotEmpty()) { "Conteúdo do QR não pode ser vazio." }
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to errorCorrection.zxing,
            EncodeHintType.MARGIN to 0,
            EncodeHintType.CHARACTER_SET to "UTF-8",
        )
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 0, 0, hints)
        val side = bitMatrix.width
        val flags = BooleanArray(side * side)
        for (y in 0 until side) {
            for (x in 0 until side) {
                flags[y * side + x] = bitMatrix.get(x, y)
            }
        }
        return MnsQrMatrix(side, flags)
    }
}
