package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.ExampleCatalog
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.ExampleDetailScreen
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.ExamplesListScreen

@Composable
fun ExamplesApp(
    modifier: Modifier = Modifier,
    platformExamples: List<Example> = emptyList(),
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onDownload: (filename: String, bytes: ByteArray) -> Unit = { _, _ -> },
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val allExamples = remember(platformExamples) { ExampleCatalog.all + platformExamples }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ExamplesListRoute) {
        composable<ExamplesListRoute> {
            ExamplesListScreen(
                examples = allExamples,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onExampleSelected = { navController.navigate(ExampleRoute(it.id)) },
                modifier = modifier
            )
        }
        composable<ExampleRoute> { backStackEntry ->
            when (val example = allExamples.find { it.id == backStackEntry.toRoute<ExampleRoute>().id }) {
                Example.Playground -> PlaygroundApp(
                    modifier = modifier,
                    onDownload = { onDownload("playground.rc", it) },
                    onBack = { navController.popBackStack() }
                )
                is Example.Document -> ExampleDetailScreen(
                    example = example,
                    modifier = modifier,
                    onDownload = { onDownload("${example.id}.rc", it) },
                    onBack = { navController.popBackStack() }
                )
                is Example.PlatformExample -> example.screen.invoke { navController.popBackStack() }
                null -> LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
    }

    LaunchedEffect(navController) { onNavHostReady(navController) }
}