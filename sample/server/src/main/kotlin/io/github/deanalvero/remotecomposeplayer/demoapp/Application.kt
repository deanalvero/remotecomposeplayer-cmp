package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.compose.remote.core.CoreDocument
import androidx.compose.remote.core.RcPlatformServices
import androidx.compose.remote.core.operations.Header
import androidx.compose.remote.creation.RemoteComposeWriter
import androidx.compose.remote.creation.dsl.Modifier
import androidx.compose.remote.creation.dsl.RcColumnVerticalPositioning
import androidx.compose.remote.creation.dsl.RcHorizontalPositioning
import androidx.compose.remote.creation.dsl.RcPaintStyle
import androidx.compose.remote.creation.dsl.RcProfile
import androidx.compose.remote.creation.dsl.RcStrokeCap
import androidx.compose.remote.creation.dsl.background
import androidx.compose.remote.creation.dsl.createRcBuffer
import androidx.compose.remote.creation.dsl.fillMaxSize
import androidx.compose.remote.creation.dsl.fillMaxWidth
import androidx.compose.remote.creation.dsl.padding
import androidx.compose.remote.creation.dsl.setStrokeCap
import androidx.compose.remote.creation.dsl.setStyle
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
                    Box(Modifier.fillMaxSize()) {
                        Canvas(Modifier.fillMaxSize()) {
                            val w = componentWidth()
                            val h = componentHeight()
                            val cx = (w / 2f)
                            val cy = (h / 2f)
                            val rad = min(cx, cy)
                            val strokeWidth = (rad / 6f)

                            applyPaint {
                                setStyle(RcPaintStyle.Stroke)
                                setColor(0xFF333333.toInt())
                                setStrokeWidth(rad * 0.03f)
                            }
                            drawCircle(cx, cy, rad * 0.98f)

                            val textSize = rad / 5f
                            applyPaint {
                                setColor(0xFFEEEEEE.toInt())
                                setTextSize(textSize)
                            }

                            applyPaint {
                                setColor(0xFF0000FF.toInt())
                                setStrokeWidth(strokeWidth)
                                setStrokeCap(RcStrokeCap.Round)
                            }

                            val hrHand = (hour() + (minutes() % 60f) / 60f) * 30f
                            save {
                                rotate(hrHand, cx, cy)
                                drawLine(cx, cy, cx, cy - rad / 3f)
                            }

                            applyPaint {
                                setColor(0xFF00FF00.toInt())
                                setStrokeWidth(strokeWidth)
                                setStrokeCap(RcStrokeCap.Round)
                            }
                            save {
                                rotate(minutes() * 6f, cx, cy)
                                drawLine(cx, cy, cx, cy - rad * 0.6f)
                            }

                            save {
                                rotate(seconds() * 6f, cx, cy)
                                val radius = rad * 0.1f
                                applyPaint {
                                    setStyle(RcPaintStyle.Fill)
                                    setColor(0xFFFF0000.toInt())
                                }
                                drawCircle(cx, cy - rad + (2f.rf * radius), radius)

                                rotate(70.rf, cx, cy)
                                applyPaint { setColor(0xFF000000.toInt()) }
                            }
                        }
                    }
                }
            }
            call.respondBytes(bytes)
        }
    }
}