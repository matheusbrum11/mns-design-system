package com.mns.designsystem.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mns.designsystem.theme.MnsTheme

/** Onde o FAB é ancorado dentro do [MnsScaffold]. */
public enum class MnsFabPosition {
    /** Canto inferior direito — o padrão. */
    END,

    /** Centralizado acima da barra inferior. */
    CENTER,
}

/**
 * Container de tela do design system.
 *
 * É o "scaffold personalizado" do MNS: recebe barra superior, barra inferior,
 * FAB e uma faixa de mensagens, e entrega ao conteúdo o `PaddingValues` exato
 * que sobrou. Diferente do `Scaffold` do Material, ele:
 *
 * - mede as barras de verdade (via `SubcomposeLayout`) em vez de assumir alturas;
 * - não impõe cor nem insets — quem manda são os tokens e os slots;
 * - expõe [contentWindowInsetsPadding] para telas que querem desenhar sob as barras.
 *
 * ```kotlin
 * MnsScaffold(
 *     topBar = { MnsTopBar(title = "Checkout", onNavigateBack = ::voltar) },
 *     bottomBar = { MnsBottomNavBar(itens, ativo, ::navegar) },
 *     floatingActionButton = { MnsFab(Icons.Filled.Add, "Novo", ::criar) },
 * ) { padding ->
 *     LazyColumn(contentPadding = padding) { /* … */ }
 * }
 * ```
 *
 * @param topBar slot da barra superior.
 * @param bottomBar slot da barra inferior.
 * @param floatingActionButton slot do FAB.
 * @param fabPosition ancoragem do FAB.
 * @param banner faixa fixa entre a barra superior e o conteúdo — alertas de
 *   conexão, avisos globais.
 * @param containerColor cor de fundo da tela.
 * @param contentWindowInsetsPadding quando `false`, o conteúdo recebe padding
 *   zero e assume responsabilidade pelos insets (telas com capa sangrada).
 * @param content recebe o `PaddingValues` que representa o espaço ocupado pelas
 *   barras. **Aplique-o**: ignorá-lo é a causa nº 1 de conteúdo escondido atrás
 *   da bottom bar.
 */
@Composable
public fun MnsScaffold(
    modifier: Modifier = Modifier,
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    fabPosition: MnsFabPosition = MnsFabPosition.END,
    banner: (@Composable () -> Unit)? = null,
    containerColor: Color = MnsTheme.colors.background,
    contentColor: Color = MnsTheme.colors.onBackground,
    contentWindowInsetsPadding: Boolean = true,
    fabMargin: Dp = MnsTheme.spacing.base,
    content: @Composable (PaddingValues) -> Unit,
) {
    MnsSurface(
        modifier = modifier.fillMaxSize(),
        shape = MnsTheme.shapes.none,
        color = containerColor,
        contentColor = contentColor,
    ) {
        SubcomposeLayout(modifier = Modifier.fillMaxSize()) { constraints ->
            val layoutWidth = constraints.maxWidth
            val layoutHeight = constraints.maxHeight
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            layout(layoutWidth, layoutHeight) {
                val topPlaceables = topBar?.let {
                    subcompose(MnsScaffoldSlot.TopBar, it).map { m -> m.measure(looseConstraints) }
                }.orEmpty()
                val topHeight = topPlaceables.maxOfOrNull { it.height } ?: 0

                val bannerPlaceables = banner?.let {
                    subcompose(MnsScaffoldSlot.Banner, it).map { m -> m.measure(looseConstraints) }
                }.orEmpty()
                val bannerHeight = bannerPlaceables.maxOfOrNull { it.height } ?: 0

                val bottomPlaceables = bottomBar?.let {
                    subcompose(MnsScaffoldSlot.BottomBar, it).map { m -> m.measure(looseConstraints) }
                }.orEmpty()
                val bottomHeight = bottomPlaceables.maxOfOrNull { it.height } ?: 0

                val fabPlaceables = floatingActionButton?.let {
                    subcompose(MnsScaffoldSlot.Fab, it).map { m -> m.measure(looseConstraints) }
                }.orEmpty()
                val fabHeight = fabPlaceables.maxOfOrNull { it.height } ?: 0
                val fabWidth = fabPlaceables.maxOfOrNull { it.width } ?: 0

                val fabMarginPx = fabMargin.roundToPx()
                val contentPadding = if (contentWindowInsetsPadding) {
                    PaddingValues(
                        top = (topHeight + bannerHeight).toDp(),
                        bottom = (
                            bottomHeight +
                                if (fabHeight > 0) fabHeight + fabMarginPx else 0
                            ).toDp(),
                    )
                } else {
                    PaddingValues(0.dp)
                }

                subcompose(MnsScaffoldSlot.Content) { content(contentPadding) }
                    .forEach { it.measure(constraints).place(0, 0) }

                topPlaceables.forEach { it.place(0, 0) }
                bannerPlaceables.forEach { it.place(0, topHeight) }
                bottomPlaceables.forEach { it.place(0, layoutHeight - bottomHeight) }

                fabPlaceables.forEach {
                    val x = when (fabPosition) {
                        MnsFabPosition.END -> layoutWidth - fabWidth - fabMarginPx
                        MnsFabPosition.CENTER -> (layoutWidth - fabWidth) / 2
                    }
                    it.place(x, layoutHeight - bottomHeight - fabHeight - fabMarginPx)
                }
            }
        }
    }
}

private enum class MnsScaffoldSlot { TopBar, Banner, Content, BottomBar, Fab }

/**
 * Soma dois [PaddingValues] respeitando a direção de layout (RTL).
 * Útil quando a tela precisa combinar o padding do scaffold com o seu próprio.
 */
@Composable
public fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current
    return remember(this, other, direction) {
        PaddingValues(
            start = calculateStartPadding(direction) + other.calculateStartPadding(direction),
            top = calculateTopPadding() + other.calculateTopPadding(),
            end = calculateEndPadding(direction) + other.calculateEndPadding(direction),
            bottom = calculateBottomPadding() + other.calculateBottomPadding(),
        )
    }
}

/** Coluna de conteúdo com as margens de tela do design system já aplicadas. */
@Composable
public fun MnsScreenColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MnsTheme.spacing.base),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth().padding(contentPadding)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MnsTheme.spacing.screenHorizontal),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }
}
