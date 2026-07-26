package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.builtins.serializer

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    val body = document.body ?: return
    ComposeViewport(body) {
        App(
            onNavHostReady = { navController ->
                val initialId = window.location.hash.removePrefix("#")
                if (initialId.isNotEmpty()) {
                    navController.navigate(ExampleRoute(initialId))
                }

                navController.bindToBrowserNavigation { entry ->
                    val route = entry.destination.route.orEmpty()
                    if (route.startsWith("io.github.deanalvero.remotecomposeplayer.demoapp.ExampleRoute")) {
                        "#${entry.toRoute<ExampleRoute>().id}"
                    } else {
                        ""
                    }
                }
            }
        )
    }
}