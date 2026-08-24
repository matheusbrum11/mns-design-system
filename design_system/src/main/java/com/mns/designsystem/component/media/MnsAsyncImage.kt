package com.mns.designsystem.component.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mns.designsystem.component.loading.mnsShimmer
import com.mns.designsystem.theme.MnsTheme

/**
 * Imagem remota com carregamento assíncrono.
 *
 * É o único ponto do design system que conhece a biblioteca de carregamento
 * (Coil) — do mesmo jeito que `MnsQrEncoder` é o único que conhece o ZXing.
 * Todos os componentes que aceitam uma URL (`MnsIcon`, `MnsAvatar`, `MnsCover`,
 * `MnsListLeading.RemoteThumbnail`) passam por aqui, então trocar de motor de
 * imagem no futuro não toca em nenhum componente.
 *
 * O estado de carregamento usa o **shimmer do próprio design system**, não um
 * spinner genérico: a transição de placeholder para imagem fica coerente com o
 * resto dos esqueletos do app.
 *
 * ```kotlin
 * MnsAsyncImage(
 *     model = evento.capaUrl,
 *     contentDescription = "Capa de ${evento.nome}",
 *     modifier = Modifier.fillMaxWidth().height(180.dp),
 * )
 * ```
 *
 * **Configuração:** o `ImageLoader` usado é o singleton do Coil. Para definir
 * cache, cabeçalhos ou autenticação, implemente `ImageLoaderFactory` na sua
 * `Application` — ver `docs/components/media/mns-async-image.md`.
 *
 * @param model o que carregar: `String` de URL, `Uri`, `File`, id de drawable
 *   ou um `ImageRequest` do Coil já montado.
 * @param contentDescription descrição para leitores de tela. `null` marca a
 *   imagem como decorativa.
 * @param shape recorte aplicado à imagem e ao placeholder.
 * @param contentScale como a imagem preenche a área.
 * @param colorFilter filtro de cor; use para tingir arte monocromática.
 * @param showShimmerWhileLoading exibe o shimmer enquanto carrega. Desligue em
 *   listas muito densas, onde N shimmers simultâneos competem por atenção.
 * @param fallback conteúdo exibido quando a carga falha. `null` deixa a área
 *   com a cor de superfície — sem ícone de erro, porque em card de conteúdo um
 *   ícone quebrado costuma incomodar mais que um espaço vazio.
 */
@Composable
public fun MnsAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = MnsTheme.shapes.image,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
    showShimmerWhileLoading: Boolean = true,
    fallback: (@Composable BoxScope.() -> Unit)? = null,
) {
    val semantics = if (contentDescription != null) {
        Modifier.semantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier.clip(shape).then(semantics),
        contentScale = contentScale,
        colorFilter = colorFilter,
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .mnsShimmer(visible = showShimmerWhileLoading, shape = shape),
            )

            is AsyncImagePainter.State.Error,
            is AsyncImagePainter.State.Empty,
            -> Box(
                modifier = Modifier.fillMaxSize().mnsPlaceholderBackground(),
                contentAlignment = Alignment.Center,
            ) {
                if (fallback != null) fallback()
            }

            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
        }
    }
}

/** Fundo neutro do placeholder, na cor de superfície de segundo nível. */
@Composable
private fun Modifier.mnsPlaceholderBackground(): Modifier =
    background(MnsTheme.colors.surfaceVariant)
