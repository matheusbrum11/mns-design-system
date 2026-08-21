package com.mns.designsystem.contract

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.theme.preset.MnsIndigoTicket
import com.mns.designsystem.theme.preset.MnsMonoEvents
import com.mns.designsystem.theme.preset.MnsPastelGlass
import org.junit.Test
import java.io.File

/**
 * Testes do Design Contract.
 *
 * O contrato é a fronteira pública entre design e código: um JSON gerado a
 * partir de um print vira tema sem recompilar nada. Por isso os testes aqui
 * cobrem tanto o caminho feliz quanto as mensagens de erro — um contrato que
 * falha em silêncio é pior que um contrato que não existe.
 */
class MnsDesignContractTest {

    private val minimo = """
        {
          "identity": { "id": "acme", "name": "ACME" },
          "colors": { "primary": "#6255F4" }
        }
    """.trimIndent()

    @Test
    fun `contrato minimo exige apenas identidade e cor primaria`() {
        val contrato = MnsDesignContractCodec.decode(minimo)
        assertThat(contrato.identity.id).isEqualTo("acme")
        assertThat(contrato.colors.primary).isEqualTo("#6255F4")
        assertThat(contrato.schemaVersion).isEqualTo(MnsDesignContract.CURRENT_SCHEMA_VERSION)
    }

    @Test
    fun `campos ausentes sao derivados ao materializar o tema`() {
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(minimo))
        assertThat(spec.id).isEqualTo("acme")
        assertThat(spec.colors.primary).isEqualTo(Color(0xFF6255F4))
        assertThat(spec.colors.onPrimary).isEqualTo(Color.White)
        assertThat(spec.colors.primaryContainer).isNotEqualTo(spec.colors.primary)
        assertThat(spec.isDark).isFalse()
    }

    @Test
    fun `chaves desconhecidas sao ignoradas`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" },
              "campoQueNaoExiste": 42
            }
        """.trimIndent()
        assertThat(MnsDesignContractCodec.decode(json).identity.id).isEqualTo("x")
    }

    @Test
    fun `schemaVersion futura e recusada com mensagem acionavel`() {
        val json = """
            {
              "schemaVersion": 99,
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" }
            }
        """.trimIndent()
        val erro = runCatching { MnsDesignContractCodec.decode(json) }.exceptionOrNull()
        assertThat(erro).isInstanceOf(MnsContractException::class.java)
        assertThat(erro).hasMessageThat().contains("Atualize a biblioteca")
    }

    @Test
    fun `json invalido vira MnsContractException`() {
        val erro = runCatching { MnsDesignContractCodec.decode("{ nao é json }") }.exceptionOrNull()
        assertThat(erro).isInstanceOf(MnsContractException::class.java)
    }

    @Test
    fun `cor mal formatada aponta o campo culpado`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "roxo" }
            }
        """.trimIndent()
        val erro = runCatching {
            MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        }.exceptionOrNull()
        assertThat(erro).isInstanceOf(MnsContractException::class.java)
        assertThat(erro).hasMessageThat().contains("colors.primary")
    }

    @Test
    fun `toColor aceita RRGGBB e AARRGGBB`() {
        assertThat("#6255F4".toColor()).isEqualTo(Color(0xFF6255F4))
        assertThat("806255F4".toColor()).isEqualTo(Color(0x806255F4))
    }

    @Test
    fun `toHex omite o alpha quando opaco`() {
        assertThat(Color(0xFF6255F4).toHex()).isEqualTo("#6255F4")
        assertThat(Color(0x806255F4).toHex()).startsWith("#80")
    }

    @Test
    fun `formas do contrato viram escala de tokens`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" },
              "shapes": { "baseRadiusDp": 20, "buttonRadiusDp": 28 }
            }
        """.trimIndent()
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        assertThat(spec.shapes.baseRadius).isEqualTo(20.dp)
        assertThat(spec.shapes.buttonRadius).isEqualTo(28.dp)
    }

    @Test
    fun `pillButtons vence o raio explicito`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" },
              "shapes": { "baseRadiusDp": 16, "buttonRadiusDp": 8, "pillButtons": true }
            }
        """.trimIndent()
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        assertThat(spec.shapes.buttonRadius).isNull()
    }

    @Test
    fun `densidade e escalas ajustam espacamento e tipografia`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" },
              "spacing": { "density": "compact", "scale": 2.0 },
              "typography": { "fontFamily": "Inter", "scale": 1.5 }
            }
        """.trimIndent()
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        assertThat(spec.spacing.base.value).isGreaterThan(16f)
        assertThat(spec.typography.bodyMedium.fontSize.value).isGreaterThan(14f)
    }

    @Test
    fun `elevacao flat e movimento reduzido sao aplicados`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" },
              "elevation": { "style": "flat" },
              "motion": { "durationScale": 0.5, "reduceMotion": true }
            }
        """.trimIndent()
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        assertThat(spec.elevation.shadowAlpha).isEqualTo(0f)
        assertThat(spec.motion.reduceMotion).isTrue()
        assertThat(spec.motion.durationNormal).isEqualTo(100)
    }

    @Test
    fun `shadowAlpha explicito sobrescreve o preset de elevacao`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X" },
              "colors": { "primary": "#000000" },
              "elevation": { "style": "dark", "shadowAlpha": 0.42 }
            }
        """.trimIndent()
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        assertThat(spec.elevation.shadowAlpha).isWithin(0.001f).of(0.42f)
    }

    @Test
    fun `contrato marcado como dark produz spec escuro`() {
        val json = """
            {
              "identity": { "id": "x", "name": "X", "dark": true, "description": "escuro" },
              "colors": { "primary": "#8B80FF" }
            }
        """.trimIndent()
        val spec = MnsDesignContractCodec.toThemeSpec(MnsDesignContractCodec.decode(json))
        assertThat(spec.isDark).isTrue()
        assertThat(spec.colors.isLight).isFalse()
    }

    @Test
    fun `toProvider deriva a variante escura quando o contrato so descreve a clara`() {
        val provider = MnsDesignContractCodec.toProvider(minimo)
        assertThat(provider.id).isEqualTo("acme")
        assertThat(provider.supportsDarkMode).isTrue()
        assertThat(provider.dark.isDark).isTrue()
        assertThat(provider.light.isDark).isFalse()
    }

    @Test
    fun `toProvider de contrato escuro usa o mesmo spec nas duas variantes`() {
        val json = """
            {
              "identity": { "id": "so-escuro", "name": "Só escuro", "dark": true },
              "colors": { "primary": "#8B80FF" }
            }
        """.trimIndent()
        val provider = MnsDesignContractCodec.toProvider(json)
        assertThat(provider.light.isDark).isTrue()
        assertThat(provider.dark.isDark).isTrue()
    }

    @Test
    fun `exportar e reimportar preserva a identidade e as cores principais`() {
        listOf(MnsIndigoTicket.light, MnsMonoEvents.light, MnsPastelGlass.light).forEach { original ->
            val contrato = MnsDesignContractCodec.fromThemeSpec(original)
            val json = MnsDesignContractCodec.encode(contrato)
            val reimportado = MnsDesignContractCodec.toThemeSpec(
                MnsDesignContractCodec.decode(json),
            )
            assertThat(reimportado.id).isEqualTo(original.id)
            assertThat(reimportado.colors.primary).isEqualTo(original.colors.primary)
            assertThat(reimportado.colors.background).isEqualTo(original.colors.background)
            assertThat(reimportado.shapes.baseRadius).isEqualTo(original.shapes.baseRadius)
        }
    }

    @Test
    fun `exportacao registra a origem do contrato`() {
        val contrato = MnsDesignContractCodec.fromThemeSpec(
            spec = MnsIndigoTicket.light,
            source = ContractSource(
                kind = "screenshot",
                reference = "image-3.png",
                generatedAt = "2026-08-20T21:00:00Z",
                notes = listOf("Cor primária lida do botão Confirm."),
            ),
        )
        assertThat(contrato.source?.kind).isEqualTo("screenshot")
        assertThat(contrato.source?.notes).hasSize(1)
        assertThat(MnsDesignContractCodec.encode(contrato)).contains("image-3.png")
    }

    @Test
    fun `contratos de exemplo do repositorio carregam e batem com os presets`() {
        val raiz = File(System.getProperty("user.dir")!!).parentFile
        val esperados = mapOf(
            "indigo-ticket" to MnsIndigoTicket.light,
            "mono-events" to MnsMonoEvents.light,
            "pastel-glass" to MnsPastelGlass.light,
        )
        esperados.forEach { (id, preset) ->
            val arquivo = File(raiz, "docs/contracts/$id.json")
            assertThat(arquivo.exists()).isTrue()

            val spec = MnsDesignContractCodec.toThemeSpec(
                MnsDesignContractCodec.decode(arquivo.readText()),
            )
            assertThat(spec.id).isEqualTo(preset.id)
            assertThat(spec.colors.primary).isEqualTo(preset.colors.primary)
            assertThat(spec.colors.background).isEqualTo(preset.colors.background)
            assertThat(spec.colors.surface).isEqualTo(preset.colors.surface)
            assertThat(spec.shapes.baseRadius).isEqualTo(preset.shapes.baseRadius)
            assertThat(spec.shapes.buttonRadius).isEqualTo(preset.shapes.buttonRadius)
        }
    }

    @Test
    fun `exportacao de preset com botao em pilula marca pillButtons`() {
        val contrato = MnsDesignContractCodec.fromThemeSpec(MnsMonoEvents.light)
        assertThat(contrato.shapes.pillButtons).isTrue()
        assertThat(contrato.shapes.buttonRadiusDp).isNull()
    }
}
