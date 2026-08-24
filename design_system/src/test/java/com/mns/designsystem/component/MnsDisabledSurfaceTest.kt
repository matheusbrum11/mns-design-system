package com.mns.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import com.google.common.truth.Truth.assertThat
import com.mns.designsystem.MnsComposeTest
import com.mns.designsystem.component.action.MnsButton
import com.mns.designsystem.component.action.MnsButtonVariant
import com.mns.designsystem.component.layout.disabledContainerColor
import org.junit.Test

/**
 * Regressão do estado desabilitado.
 *
 * `MnsSurface` aplicava `color.copy(alpha = 1f - opacity.disabled)`. Como `copy`
 * preserva o RGB e um container transparente tem RGB (0,0,0), um `MnsButton`
 * `TEXT`/`OUTLINED` desabilitado virava uma caixa preta a 62% sobre fundo claro.
 */
class MnsDisabledSurfaceTest : MnsComposeTest() {

    @Test
    fun `container transparente continua transparente ao desabilitar`() {
        val resultado = disabledContainerColor(Color.Transparent, disabledOpacity = 0.38f)
        assertThat(resultado.alpha).isEqualTo(0f)
        assertThat(resultado).isEqualTo(Color.Transparent)
    }

    @Test
    fun `container opaco esmaece na proporcao do token`() {
        val resultado = disabledContainerColor(Color(0xFF6255F4), disabledOpacity = 0.38f)
        assertThat(resultado.alpha).isWithin(0.001f).of(0.62f)
        // O matiz é preservado: só a opacidade muda.
        assertThat(resultado.red).isWithin(0.001f).of(Color(0xFF6255F4).red)
        assertThat(resultado.green).isWithin(0.001f).of(Color(0xFF6255F4).green)
        assertThat(resultado.blue).isWithin(0.001f).of(Color(0xFF6255F4).blue)
    }

    @Test
    fun `container semitransparente escala em vez de ganhar opacidade`() {
        // Regressão específica: substituir o alpha AUMENTARIA a opacidade de um
        // container que já era translúcido (0.20 viraria 0.62).
        val resultado = disabledContainerColor(Color(0x336255F4), disabledOpacity = 0.38f)
        assertThat(resultado.alpha).isLessThan(Color(0x336255F4).alpha)
    }

    @Test
    fun `botoes desabilitados de todas as variantes renderizam e ficam inativos`() {
        setThemedContent {
            Column {
                MnsButtonVariant.entries.forEach { variante ->
                    MnsButton(
                        text = variante.name,
                        onClick = {},
                        variant = variante,
                        enabled = false,
                        contentDescription = "botao-${variante.name}",
                    )
                }
            }
        }
        MnsButtonVariant.entries.forEach { variante ->
            composeRule.onNodeWithContentDescription("botao-${variante.name}")
                .assertShown()
                .assertIsNotEnabled()
        }
    }
}
