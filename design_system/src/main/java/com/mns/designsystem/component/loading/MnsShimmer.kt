package com.mns.designsystem.component.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.theme.MnsTheme

/**
 * Aplica o efeito shimmer de carregamento ao elemento.
 *
 * O gradiente varre a caixa na diagonal, na duração de `motion.durationShimmer`.
 * Com `reduceMotion` ligado o movimento é suprimido e resta apenas a cor base —
 * shimmer em loop é um gatilho conhecido para quem tem sensibilidade a movimento.
 *
 * ```kotlin
 * Box(Modifier.size(120.dp, 16.dp).mnsShimmer(shape = MnsTheme.shapes.shimmer))
 * ```
 *
 * @param visible desliga o efeito quando `false`, sem remover o modifier —
 *   evita recriar a árvore ao terminar o carregamento.
 * @param shape recorte do bloco.
 */
public fun Modifier.mnsShimmer(
    visible: Boolean = true,
    shape: Shape? = null,
): Modifier = composed {
    val colors = MnsTheme.colors
    val motion = MnsTheme.motion
    val resolvedShape = shape ?: MnsTheme.shapes.shimmer

    if (!visible) return@composed this

    val transition = rememberInfiniteTransition(label = "mnsShimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = motion.duration(motion.durationShimmer).coerceAtLeast(1),
                easing = motion.easingLinear,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "mnsShimmerProgress",
    )

    this
        .background(colors.shimmerBase, resolvedShape)
        .drawWithCache {
            // Só o que não depende da animação fica no cache. `progress` é lido
            // dentro de `onDrawWithContent`: lê-lo aqui invalidaria o bloco a
            // cada frame, recriando o Outline (e o Path das formas arredondadas)
            // 60 vezes por segundo, para cada esqueleto na tela — exatamente a
            // alocação que `drawWithCache` existe para evitar.
            val bandWidth = size.width * 0.6f
            val gradiente = listOf(
                colors.shimmerBase,
                colors.shimmerHighlight,
                colors.shimmerBase,
            )
            val outline = resolvedShape.createOutline(size, layoutDirection, this)
            val caminho = (outline as? Outline.Rounded)?.let {
                Path().apply { addRoundRect(it.roundRect) }
            }
            onDrawWithContent {
                drawContent()
                val start = -bandWidth + (size.width + bandWidth * 2) * progress
                val brush = Brush.linearGradient(
                    colors = gradiente,
                    start = Offset(start, 0f),
                    end = Offset(start + bandWidth, size.height),
                )
                drawShimmer(outline, caminho, brush)
            }
        }
}

/**
 * Pinta o brilho respeitando o recorte da forma.
 *
 * @param caminho `Path` pré-construído para formas arredondadas, vindo do cache
 *   de [drawWithCache]. Construí-lo aqui alocaria um Path por frame.
 */
private fun DrawScope.drawShimmer(
    outline: Outline,
    caminho: Path?,
    brush: Brush,
) {
    when (outline) {
        is Outline.Rectangle -> drawRect(brush)
        is Outline.Rounded -> caminho?.let { drawPath(it, brush) }
        is Outline.Generic -> drawPath(outline.path, brush)
    }
}

/**
 * Bloco retangular de shimmer — a peça de montar de qualquer esqueleto.
 *
 * @param width largura; `null` ocupa toda a largura disponível.
 * @param height altura do bloco.
 * @param shape recorte.
 */
@Composable
public fun MnsShimmerBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 16.dp,
    shape: Shape = MnsTheme.shapes.shimmer,
    visible: Boolean = true,
) {
    Box(
        modifier = modifier
            .let { if (width != null) it.size(width, height) else it.fillMaxWidth().height(height) }
            .mnsShimmer(visible = visible, shape = shape),
    )
}

/**
 * Esqueleto de parágrafo: N linhas, com a última mais curta para imitar texto
 * real. Detalhe pequeno, mas é o que faz o placeholder parecer conteúdo em vez
 * de tabela.
 *
 * @param lines quantidade de linhas.
 * @param lastLineFraction largura da última linha, como fração da largura total.
 */
@Composable
public fun MnsShimmerParagraph(
    modifier: Modifier = Modifier,
    lines: Int = 3,
    lineHeight: Dp = 14.dp,
    lastLineFraction: Float = 0.6f,
    visible: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Carregando conteúdo" },
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
    ) {
        repeat(lines) { index ->
            val isLast = index == lines - 1
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (isLast) lastLineFraction else 1f)
                    .height(lineHeight)
                    .mnsShimmer(visible = visible),
            )
        }
    }
}

/**
 * Esqueleto de um item de lista: avatar circular + duas linhas de texto.
 * Corresponde 1:1 ao layout de `MnsListAction`, então a transição de esqueleto
 * para conteúdo não desloca nada.
 *
 * @param showAvatar inclui o círculo do avatar.
 */
@Composable
public fun MnsShimmerListItem(
    modifier: Modifier = Modifier,
    showAvatar: Boolean = true,
    visible: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Carregando item" },
        horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
    ) {
        if (showAvatar) {
            Box(
                modifier = Modifier
                    .size(MnsTheme.sizing.avatarMd)
                    .mnsShimmer(visible = visible, shape = MnsTheme.shapes.avatar),
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
        ) {
            MnsShimmerBox(height = 14.dp, visible = visible)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(12.dp)
                    .mnsShimmer(visible = visible),
            )
        }
    }
}

/**
 * Esqueleto de card com capa: imagem no topo, título e duas linhas.
 *
 * @param coverHeight altura da área de imagem.
 */
@Composable
public fun MnsShimmerCard(
    modifier: Modifier = Modifier,
    coverHeight: Dp = MnsTheme.sizing.coverHeight,
    visible: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Carregando card" },
        verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.md),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(coverHeight)
                .mnsShimmer(visible = visible, shape = MnsTheme.shapes.image),
        )
        MnsShimmerBox(height = 18.dp, visible = visible)
        MnsShimmerParagraph(lines = 2, visible = visible)
    }
}
