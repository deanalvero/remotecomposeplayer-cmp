package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.remote.core.CoreDocument
import androidx.compose.remote.core.RcPlatformServices
import androidx.compose.remote.core.operations.Header
import androidx.compose.remote.creation.RemoteComposeWriter
import androidx.compose.remote.creation.dsl.Modifier
import androidx.compose.remote.creation.dsl.RcColumnVerticalPositioning
import androidx.compose.remote.creation.dsl.RcHorizontalPositioning
import androidx.compose.remote.creation.dsl.RcProfile
import androidx.compose.remote.creation.dsl.background
import androidx.compose.remote.creation.dsl.createRcBuffer
import androidx.compose.remote.creation.dsl.fillMaxWidth
import androidx.compose.remote.creation.dsl.padding
import androidx.compose.remote.creation.dsl.rsp
import androidx.compose.remote.creation.profile.Profile
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            val width = 1200
            val height = 600

            val profile = Profile(
                CoreDocument.DOCUMENT_API_LEVEL,
                0,
                RcPlatformServices.None,
                { info, profile, _ ->
                    RemoteComposeWriter(
                        profile,
                        RemoteComposeWriter.hTag(Header.DOC_WIDTH, info.width),
                        RemoteComposeWriter.hTag(Header.DOC_HEIGHT, info.height),
                        RemoteComposeWriter.hTag(Header.DOC_CONTENT_DESCRIPTION, ""),
                        RemoteComposeWriter.hTag(Header.DOC_PROFILES, 0),
                    )
                },
            )

            val bytes = createRcBuffer(
                RcProfile(profile),
                RemoteComposeWriter.hTag(Header.DOC_WIDTH, width),
                RemoteComposeWriter.hTag(Header.DOC_HEIGHT, height),
                RemoteComposeWriter.hTag(Header.DOC_CONTENT_DESCRIPTION, ""),
                RemoteComposeWriter.hTag(Header.DOC_PROFILES, 0),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().background(0xFFEEEEEE.toInt()).padding(20f),
                    horizontal = RcHorizontalPositioning.Center,
                    vertical = RcColumnVerticalPositioning.Top,
                ) {
                    Text("Sample text!", fontSize = 48.rsp, color = 0xFFFF3333.toInt())

                    Canvas {
                        drawCircle(
                            25f, 25f, 50f
                        )
                    }
                }


                drawCircle(
                    25f, 25f, 50f
                )

            }

            call.respondBytes(bytes)
        }
    }
}