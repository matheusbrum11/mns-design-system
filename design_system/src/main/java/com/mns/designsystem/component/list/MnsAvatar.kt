package com.mns.designsystem.component.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import com.mns.designsystem.component.media.MnsIcon
import com.mns.designsystem.component.text.MnsText
import com.mns.designsystem.theme.MnsTheme
import kotlin.math.absoluteValue

/**
 * Avatar de usuário ou entidade.
 *
 * Resolve o conteúdo em ordem de preferência: [painter] → [icon] → iniciais de
 * [name]. Ou seja, nunca renderiza vazio — o pior caso ainda mostra as iniciais
 * sobre uma cor derivada do nome, o que dá identidade estável ao usuário mesmo
 * sem foto.
 *
 * @param name nome usado para gerar iniciais e a cor de fundo determinística.
 * @param painter imagem do avatar, quando disponível.
 * @param icon ícone alternativo (ex.: entidade sem nome).
 * @param size diâmetro. Use `MnsTheme.sizing.avatar*`.
 * @param borderWidth anel externo — use em grupos sobrepostos para separar.
 * @param contentDescription descrição para leitores de tela; default é [name].
 */
@Composable
public fun MnsAvatar(
    name: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    icon: ImageVector? = null,
    size: Dp = MnsTheme.sizing.avatarMd,
    shape: Shape = MnsTheme.shapes.avatar,
    backgroundColor: Color? = null,
    borderWidth: Dp = MnsTheme.borders.none,
    borderColor: Color = MnsTheme.colors.surface,
    contentDescription: String? = null,
) {
    val colors = MnsTheme.colors
    val container = backgroundColor ?: deterministicColor(
        seed = name,
        palette = listOf(colors.primaryContainer, colors.accentContainer, colors.secondaryContainer),
    )
    val content = colors.onPrimaryContainer

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .let { if (borderWidth.value > 0f) it.border(borderWidth, borderColor, shape) else it }
            .clearAndSetSemantics { this.contentDescription = contentDescription ?: name },
        contentAlignment = Alignment.Center,
    ) {
        when {
            painter != null -> Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(size).clip(shape),
                contentScale = ContentScale.Crop,
            )
            icon != null -> Box(
                modifier = Modifier.size(size).clip(shape),
                contentAlignment = Alignment.Center,
            ) {
                MnsIcon(
                    imageVector = icon,
                    contentDescription = null,
                    size = size * 0.5f,
                    tint = content,
                )
            }
            else -> Box(
                modifier = Modifier
                    .size(size)
                    .clip(shape)
                    .background(container),
                contentAlignment = Alignment.Center,
            ) {
                MnsText(
                    text = initialsOf(name),
                    style = MnsTheme.typography.labelMedium.copy(fontSize = MnsTheme.typography.labelMedium.fontSize * (size.value / 40f)),
                    color = content,
                )
            }
        }
    }
}

/**
 * Pilha de avatares sobrepostos com contador de excedentes — o "+6" que aparece
 * nos cards de evento dos designs de referência.
 *
 * @param names nomes dos participantes, na ordem de exibição.
 * @param max quantos avatares exibir antes de resumir no contador.
 * @param overlap fração de sobreposição entre avatares (0f = lado a lado).
 */
@Composable
public fun MnsAvatarGroup(
    names: List<String>,
    modifier: Modifier = Modifier,
    max: Int = 3,
    size: Dp = MnsTheme.sizing.avatarSm,
    overlap: Float = 0.35f,
    showOverflowCount: Boolean = true,
) {
    val colors = MnsTheme.colors
    val visible = names.take(max)
    val overflow = (names.size - visible.size).coerceAtLeast(0)
    val step = size * (1f - overlap)

    Row(
        modifier = modifier.clearAndSetSemantics {
            contentDescription = "${names.size} participantes"
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visible.forEachIndexed { index, name ->
            MnsAvatar(
                name = name,
                modifier = Modifier.offset(x = -(step - size) * index),
                size = size,
                borderWidth = MnsTheme.borders.thick,
                borderColor = colors.surface,
            )
        }
        if (overflow > 0 && showOverflowCount) {
            Box(
                modifier = Modifier
                    .offset(x = -(step - size) * visible.size)
                    .size(size)
                    .clip(MnsTheme.shapes.avatar)
                    .border(MnsTheme.borders.thick, colors.surface, MnsTheme.shapes.avatar)
                    .background(colors.primary),
                contentAlignment = Alignment.Center,
            ) {
                MnsText(
                    text = "+$overflow",
                    style = MnsTheme.typography.labelSmall,
                    color = colors.onPrimary,
                )
            }
        }
    }
}

/** Extrai até duas iniciais de um nome. `"Alves Farhat"` → `"AF"`. */
internal fun initialsOf(name: String): String {
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().first()}${parts.last().first()}".uppercase()
    }
}

/** Escolhe uma cor de uma paleta pequena de forma estável para o mesmo nome. */
private fun deterministicColor(seed: String, palette: List<Color>): Color =
    palette[seed.hashCode().absoluteValue % palette.size]
