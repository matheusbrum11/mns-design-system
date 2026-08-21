package com.mns.demo

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mns.demo.catalog.DemoCatalog
import com.mns.demo.playground.ThemeController
import com.mns.demo.screen.ComponentScreen
import com.mns.demo.screen.HomeScreen
import com.mns.demo.screen.TokensScreen
import com.mns.designsystem.component.status.MnsEmptyState
import com.mns.designsystem.theme.MnsTheme

/** Rotas do app de demonstração. */
internal object DemoRoutes {
    const val HOME: String = "home"
    const val TOKENS: String = "tokens"
    const val COMPONENT: String = "component/{id}"

    fun component(id: String): String = "component/$id"
}

/**
 * Raiz do `app_demo`.
 *
 * O [ThemeController] vive aqui, acima do `NavHost`: é o que garante que uma
 * alteração de token feita na tela de Tokens seja visível na home e na tela de
 * qualquer componente — o app inteiro é o preview.
 *
 * @param navController injetável para que os testes de integração naveguem
 *   diretamente para uma rota sem depender de toques encadeados.
 */
@Composable
public fun MnsDemoApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    themeController: ThemeController = rememberSaveable(saver = ThemeController.Saver) { ThemeController() },
) {
    val spec = themeController.spec

    // Os ícones das barras do sistema não fazem parte da composição, então
    // precisam ser avisados na mão. `colors.isLight` existe exatamente para
    // isso: sem esta ponte, o relógio some no dark mode.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = spec.colors.isLight
                isAppearanceLightNavigationBars = spec.colors.isLight
            }
        }
    }

    MnsTheme(
        provider = themeController.provider,
        darkTheme = themeController.darkMode,
        spec = spec,
    ) {
        NavHost(
            navController = navController,
            startDestination = DemoRoutes.HOME,
            modifier = modifier,
        ) {
            composable(DemoRoutes.HOME) {
                HomeScreen(
                    theme = themeController,
                    onComponentClick = { navController.navigate(DemoRoutes.component(it.id)) },
                    onOpenTokens = { navController.navigate(DemoRoutes.TOKENS) },
                )
            }
            composable(DemoRoutes.TOKENS) {
                TokensScreen(
                    theme = themeController,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(DemoRoutes.COMPONENT) { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                val component = DemoCatalog.byId(id)
                if (component == null) {
                    MnsEmptyState(
                        title = "Componente não encontrado",
                        description = "Nenhuma entrada do catálogo com id \"$id\".",
                    )
                } else {
                    ComponentScreen(
                        component = component,
                        theme = themeController,
                        onBack = { navController.popBackStack() },
                        onOpenDocs = { /* Ver docs/README.md — o app não embute os .md. */ },
                    )
                }
            }
        }
    }
}
