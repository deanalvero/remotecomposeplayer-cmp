package io.github.deanalvero.remotecomposeplayer.demoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                platformExamples = listOf(
                    Example.PlatformExample(
                        id = "remote-source",
                        title = "Remote Source",
                        subtitle = "Render Remote Compose from a remote source",
                    ) {
                        ExperimentalMainScreen(
                            onBack = it
                        )
                    }
                )
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}