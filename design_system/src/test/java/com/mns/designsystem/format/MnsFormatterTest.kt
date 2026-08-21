package com.mns.designsystem.format

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

/**
 * Testes dos formatadores.
 *
 * Rodam em JVM pura — não há composição envolvida. As asserções normalizam
 * espaços não-quebráveis porque o ICU muda o separador entre símbolo e número
 * conforme a versão da JVM, e travar o teste nisso gera falha falsa em CI.
 */
class MnsFormatterTest {

    private fun String.normalizado(): String =
        replace(' ', ' ').replace(' ', ' ')

    // ── Moeda ────────────────────────────────────────────────────────────────

    @Test
    fun `formata centavos em reais`() {
        assertThat(MnsCurrencyFormatter.formatCents(123456).normalizado())
            .isEqualTo("R$ 1.234,56")
    }

    @Test
    fun `formata centavos em dolares`() {
        assertThat(MnsCurrencyFormatter.formatCents(123456, MnsCurrencyFormat.USD).normalizado())
            .isEqualTo("$1,234.56")
    }

    @Test
    fun `formata centavos em euros`() {
        val texto = MnsCurrencyFormatter.formatCents(123456, MnsCurrencyFormat.EUR).normalizado()
        assertThat(texto).contains("1.234,56")
    }

    @Test
    fun `valor zero mantem as casas decimais`() {
        assertThat(MnsCurrencyFormatter.formatCents(0).normalizado()).isEqualTo("R$ 0,00")
    }

    @Test
    fun `negativo usa sinal de menos por padrao`() {
        assertThat(MnsCurrencyFormatter.formatCents(-500).normalizado()).startsWith("-")
    }

    @Test
    fun `negativo em estilo contabil usa parenteses`() {
        val formato = MnsCurrencyFormat.BRL.copy(negativeStyle = MnsNegativeStyle.PARENTHESES)
        val texto = MnsCurrencyFormatter.formatCents(-500, formato).normalizado()
        assertThat(texto).startsWith("(")
        assertThat(texto).endsWith(")")
    }

    @Test
    fun `estilo NONE omite o sinal`() {
        val formato = MnsCurrencyFormat.BRL.copy(negativeStyle = MnsNegativeStyle.NONE)
        assertThat(MnsCurrencyFormatter.formatCents(-500, formato).normalizado())
            .doesNotContain("-")
    }

    @Test
    fun `showSymbol falso remove o simbolo da moeda`() {
        val formato = MnsCurrencyFormat.BRL.copy(showSymbol = false)
        assertThat(MnsCurrencyFormatter.formatCents(123456, formato).normalizado())
            .isEqualTo("1.234,56")
    }

    @Test
    fun `fractionDigits customizado sobrescreve o padrao da moeda`() {
        val formato = MnsCurrencyFormat.BRL.copy(fractionDigits = 0)
        assertThat(MnsCurrencyFormatter.formatCents(123456, formato).normalizado())
            .isEqualTo("R$ 1.235")
    }

    @Test
    fun `moeda invalida cai no formato numerico sem quebrar`() {
        val formato = MnsCurrencyFormat.BRL.copy(currencyCode = "XYZ", showSymbol = false)
        assertThat(MnsCurrencyFormatter.format(BigDecimal("10.5"), formato).normalizado())
            .isEqualTo("10,50")
    }

    @Test
    fun `parseDigitsToCents ignora caracteres nao numericos`() {
        assertThat(MnsCurrencyFormatter.parseDigitsToCents("R$ 1.234,56")).isEqualTo(123456L)
        assertThat(MnsCurrencyFormatter.parseDigitsToCents("")).isEqualTo(0L)
        assertThat(MnsCurrencyFormatter.parseDigitsToCents("abc")).isEqualTo(0L)
    }

    @Test
    fun `parseDigitsToCents limita entradas absurdamente longas`() {
        val gigante = "9".repeat(60)
        assertThat(MnsCurrencyFormatter.parseDigitsToCents(gigante)).isGreaterThan(0L)
    }

    // ── Percentual ───────────────────────────────────────────────────────────

    @Test
    fun `formata fracao como percentual`() {
        assertThat(MnsPercentFormatter.format(0.4237).normalizado()).isEqualTo("42,4%")
    }

    @Test
    fun `formato Whole remove as casas decimais`() {
        assertThat(MnsPercentFormatter.format(0.4237, MnsPercentFormat.Whole).normalizado())
            .isEqualTo("42%")
    }

    @Test
    fun `formato Signed prefixa positivos`() {
        assertThat(MnsPercentFormatter.format(0.032, MnsPercentFormat.Signed).normalizado())
            .isEqualTo("+3,2%")
    }

    @Test
    fun `percentual negativo recebe sinal de menos`() {
        assertThat(MnsPercentFormatter.format(-0.018, MnsPercentFormat.Signed).normalizado())
            .isEqualTo("-1,8%")
    }

    @Test
    fun `inputIsFraction falso trata o valor como percentual`() {
        val formato = MnsPercentFormat.Whole.copy(inputIsFraction = false)
        assertThat(MnsPercentFormatter.format(42.0, formato).normalizado()).isEqualTo("42%")
    }

    // ── Números compactos ────────────────────────────────────────────────────

    @Test
    fun `abaixo do limiar o numero e exibido por extenso`() {
        assertThat(MnsCompactNumberFormatter.format(980).normalizado()).isEqualTo("980")
    }

    @Test
    fun `milhares viram sufixo mil`() {
        assertThat(MnsCompactNumberFormatter.format(1_240).normalizado()).isEqualTo("1,2 mil")
    }

    @Test
    fun `milhoes e bilhoes recebem os sufixos corretos`() {
        assertThat(MnsCompactNumberFormatter.format(3_400_000).normalizado()).isEqualTo("3,4 mi")
        assertThat(MnsCompactNumberFormatter.format(2_100_000_000).normalizado()).isEqualTo("2,1 bi")
    }

    @Test
    fun `valores negativos preservam o sinal`() {
        assertThat(MnsCompactNumberFormatter.format(-1_500).normalizado()).startsWith("-1,5")
    }

    @Test
    fun `locale altera o separador decimal`() {
        assertThat(MnsCompactNumberFormatter.format(1_240, Locale.US).normalizado())
            .isEqualTo("1.2 mil")
    }

    // ── Transformações visuais ───────────────────────────────────────────────

    @Test
    fun `transformacao de moeda formata e ancora o cursor no fim`() {
        val transform = MnsCurrencyVisualTransformation()
        val resultado = transform.filter(AnnotatedString("123456"))
        assertThat(resultado.text.text.normalizado()).isEqualTo("R$ 1.234,56")
        assertThat(resultado.offsetMapping.originalToTransformed(0))
            .isEqualTo(resultado.text.text.length)
        assertThat(resultado.offsetMapping.transformedToOriginal(0)).isEqualTo(6)
    }

    @Test
    fun `transformacoes de moeda iguais sao equivalentes`() {
        val a = MnsCurrencyVisualTransformation()
        val b = MnsCurrencyVisualTransformation()
        assertThat(a).isEqualTo(b)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
        assertThat(a).isNotEqualTo(MnsCurrencyVisualTransformation(MnsCurrencyFormat.USD))
    }

    @Test
    fun `mascara insere os literais nas posicoes corretas`() {
        val cpf = MnsMaskVisualTransformation("###.###.###-##")
        assertThat(cpf.inputLength).isEqualTo(11)
        assertThat(cpf.filter(AnnotatedString("12345678901")).text.text)
            .isEqualTo("123.456.789-01")
    }

    @Test
    fun `mascara parcial para no ultimo caractere digitado`() {
        val telefone = MnsMaskVisualTransformation("(##) #####-####")
        assertThat(telefone.filter(AnnotatedString("119")).text.text).isEqualTo("(11) 9")
    }

    @Test
    fun `mascara mapeia offsets nos dois sentidos`() {
        val validade = MnsMaskVisualTransformation("##/##")
        val resultado = validade.filter(AnnotatedString("1228"))
        assertThat(resultado.text.text).isEqualTo("12/28")
        assertThat(resultado.offsetMapping.originalToTransformed(0)).isEqualTo(0)
        assertThat(resultado.offsetMapping.originalToTransformed(2)).isEqualTo(2)
        assertThat(resultado.offsetMapping.originalToTransformed(99)).isEqualTo(5)
        assertThat(resultado.offsetMapping.transformedToOriginal(3)).isEqualTo(2)
        assertThat(resultado.offsetMapping.transformedToOriginal(99)).isEqualTo(4)
    }

    @Test
    fun `mascaras iguais sao equivalentes`() {
        val a = MnsMaskVisualTransformation("##/##")
        assertThat(a).isEqualTo(MnsMaskVisualTransformation("##/##"))
        assertThat(a.hashCode()).isEqualTo(MnsMaskVisualTransformation("##/##").hashCode())
        assertThat(a).isNotEqualTo(MnsMaskVisualTransformation("###"))
    }
}
