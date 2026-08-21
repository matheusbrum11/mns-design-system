package com.mns.designsystem.token

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Testes da camada de tokens.
 *
 * Tokens não desenham nada, então aqui não há composição: o que se verifica é
 * o **contrato matemático** — derivações, escalas e lookups — que todo o resto
 * do design system assume verdadeiro.
 */
class MnsTokenTest {

    // ── Paleta e cor ─────────────────────────────────────────────────────────

    @Test
    fun `mixWith interpola linearmente entre duas cores`() {
        val meio = Color.Black.mixWith(Color.White, 0.5f)
        assertThat(meio.red).isWithin(0.01f).of(0.5f)
        assertThat(meio.green).isWithin(0.01f).of(0.5f)
        assertThat(meio.blue).isWithin(0.01f).of(0.5f)
    }

    @Test
    fun `mixWith satura a razao fora do intervalo`() {
        assertThat(Color.Black.mixWith(Color.White, 5f)).isEqualTo(Color.White)
        assertThat(Color.Black.mixWith(Color.White, -3f)).isEqualTo(Color.Black)
    }

    @Test
    fun `contrastRatio entre preto e branco e 21`() {
        assertThat(contrastRatio(Color.Black, Color.White)).isWithin(0.1f).of(21f)
    }

    @Test
    fun `contrastRatio de uma cor com ela mesma e 1`() {
        assertThat(contrastRatio(Color.Red, Color.Red)).isWithin(0.01f).of(1f)
    }

    @Test
    fun `contentColorFor escolhe o tom de maior contraste`() {
        assertThat(Color(0xFF101010).contentColorFor()).isEqualTo(Color.White)
        assertThat(Color(0xFFF5F5F5).contentColorFor()).isEqualTo(Color.Black)
    }

    @Test
    fun `fromSeed produz rampa do escuro ao claro`() {
        val rampa = MnsColorRamp.fromSeed(Color(0xFF6255F4))
        assertThat(rampa.s60).isEqualTo(Color(0xFF6255F4))
        assertThat(rampa.s0.relativeLuminance()).isLessThan(rampa.s50.relativeLuminance())
        assertThat(rampa.s50.relativeLuminance()).isLessThan(rampa.s100.relativeLuminance())
    }

    @Test
    fun `step resolve cada degrau da rampa`() {
        val rampa = MnsColorRamp.fromSeed(Color(0xFF6255F4))
        listOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 100).forEach { degrau ->
            assertThat(rampa.step(degrau)).isNotNull()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `step recusa degrau inexistente`() {
        MnsColorRamp.fromSeed(Color.Red).step(42)
    }

    @Test
    fun `MnsPalette agrega as rampas nomeadas`() {
        val palette = MnsPalette(
            brand = MnsColorRamp.fromSeed(Color(0xFF6255F4)),
            neutral = MnsColorRamp.fromSeed(Color(0xFF808080)),
            accent = MnsColorRamp.fromSeed(Color(0xFFA197FF)),
            success = MnsColorRamp.fromSeed(Color(0xFF16A34A)),
            warning = MnsColorRamp.fromSeed(Color(0xFFD97706)),
            danger = MnsColorRamp.fromSeed(Color(0xFFDC2626)),
            info = MnsColorRamp.fromSeed(Color(0xFF2563EB)),
        )
        assertThat(palette.brand.s60).isEqualTo(Color(0xFF6255F4))
        assertThat(palette.copy(neutral = palette.brand).neutral).isEqualTo(palette.brand)
    }

    // ── MnsColors ────────────────────────────────────────────────────────────

    @Test
    fun `fabrica light deriva os campos ausentes`() {
        val cores = MnsColors.light(primary = Color(0xFF6255F4))
        assertThat(cores.isLight).isTrue()
        assertThat(cores.onPrimary).isEqualTo(Color.White)
        assertThat(cores.primaryContainer).isNotEqualTo(cores.primary)
        assertThat(cores.textLink).isEqualTo(cores.primary)
    }

    @Test
    fun `fabrica dark marca o esquema como escuro`() {
        val cores = MnsColors.dark(primary = Color(0xFF8B80FF))
        assertThat(cores.isLight).isFalse()
        assertThat(cores.background.relativeLuminance())
            .isLessThan(cores.onBackground.relativeLuminance())
    }

    @Test
    fun `byRole resolve todos os papeis anunciados`() {
        val cores = MnsColors.light(primary = Color(0xFF6255F4))
        MnsColors.roleNames.forEach { papel ->
            assertThat(cores.byRoleOrNull(papel)).isNotNull()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `byRole recusa papel inexistente`() {
        MnsColors.light(primary = Color.Red).byRole("cor-do-botao-legal")
    }

    @Test
    fun `containerFor e solidFor cobrem todos os status`() {
        val cores = MnsColors.light(primary = Color(0xFF6255F4))
        MnsStatus.entries.forEach { status ->
            assertThat(cores.containerFor(status)).isNotNull()
            assertThat(cores.solidFor(status)).isNotNull()
        }
    }

    // ── Formas ───────────────────────────────────────────────────────────────

    @Test
    fun `fromBaseRadius registra o raio base para o contrato`() {
        val shapes = MnsShapes.fromBaseRadius(base = 20.dp)
        assertThat(shapes.baseRadius).isEqualTo(20.dp)
        assertThat(shapes.buttonRadius).isEqualTo(15.dp)
    }

    @Test
    fun `pillButtons zera o raio de botao registrado`() {
        val shapes = MnsShapes.fromBaseRadius(base = 16.dp, pillButtons = true)
        assertThat(shapes.buttonRadius).isNull()
        assertThat(shapes.button).isEqualTo(shapes.full)
    }

    @Test
    fun `buttonRadius explicito prevalece sobre a derivacao`() {
        val shapes = MnsShapes.fromBaseRadius(base = 12.dp, buttonRadius = 28.dp)
        assertThat(shapes.buttonRadius).isEqualTo(28.dp)
    }

    @Test
    fun `byName resolve escala e papeis de componente`() {
        val shapes = MnsShapes.fromBaseRadius()
        listOf(
            "none", "xs", "sm", "md", "lg", "xl", "xxl", "pill",
            "button", "input", "card", "chip", "bottomsheet", "dialog", "image",
            "avatar", "badge",
        ).forEach { nome ->
            assertThat(shapes.byName(nome)).isNotNull()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `byName recusa forma desconhecida`() {
        MnsShapes.fromBaseRadius().byName("squircle-maluco")
    }

    // ── Espaçamento, tipografia, movimento ───────────────────────────────────

    @Test
    fun `escala de espacamento preserva proporcao`() {
        val denso = MnsSpacing.Default.scaledBy(0.5f)
        assertThat(denso.base.value).isWithin(0.01f).of(8f)
        assertThat(denso.none).isEqualTo(MnsSpacing.Default.none)
        assertThat(MnsSpacing.Compact.base.value).isLessThan(MnsSpacing.Default.base.value)
        assertThat(MnsSpacing.Comfortable.base.value).isGreaterThan(MnsSpacing.Default.base.value)
    }

    @Test
    fun `tipografia troca a familia sem mexer no mono`() {
        val base = MnsTypography.default()
        val trocada = base.withFontFamily(androidx.compose.ui.text.font.FontFamily.Cursive)
        assertThat(trocada.bodyMedium.fontFamily)
            .isEqualTo(androidx.compose.ui.text.font.FontFamily.Cursive)
        assertThat(trocada.mono.fontFamily).isEqualTo(base.mono.fontFamily)
    }

    @Test
    fun `escala tipografica multiplica todos os tamanhos`() {
        val base = MnsTypography.default()
        val maior = base.scaledBy(2f)
        assertThat(maior.bodyMedium.fontSize.value)
            .isWithin(0.01f).of(base.bodyMedium.fontSize.value * 2)
        assertThat(maior.displayLarge.fontSize.value)
            .isWithin(0.01f).of(base.displayLarge.fontSize.value * 2)
    }

    @Test
    fun `reduceMotion zera as duracoes efetivas`() {
        assertThat(MnsMotion.Default.duration(300)).isEqualTo(300)
        assertThat(MnsMotion.None.duration(300)).isEqualTo(0)
        assertThat(MnsMotion.None.tween<Float>(200)).isNotNull()
    }

    @Test
    fun `presets de elevacao diferem no alpha da sombra`() {
        assertThat(MnsElevation.Dark.shadowAlpha).isGreaterThan(MnsElevation.Light.shadowAlpha)
        assertThat(MnsElevation.Flat.level5.value).isEqualTo(0f)
        assertThat(MnsElevation.Flat.shadowAlpha).isEqualTo(0f)
    }

    @Test
    fun `bordas e opacidades tem valores padrao coerentes`() {
        assertThat(MnsBorders.Default.hairline.value).isLessThan(MnsBorders.Default.thin.value)
        assertThat(MnsBorders.Default.focus.value).isGreaterThan(MnsBorders.Default.thick.value)
        assertThat(MnsOpacity.Default.disabled).isAtLeast(0f)
        assertThat(MnsOpacity.Default.disabled).isAtMost(1f)
        assertThat(MnsOpacity.Default.scrim).isAtLeast(0f)
        assertThat(MnsOpacity.Default.scrim).isAtMost(1f)
    }

    @Test
    fun `sizing respeita o alvo de toque minimo de 48dp`() {
        assertThat(MnsSizing.Default.touchTarget.value).isAtLeast(48f)
        assertThat(MnsSizing.Default.buttonHeightMd.value).isAtLeast(44f)
    }
}
