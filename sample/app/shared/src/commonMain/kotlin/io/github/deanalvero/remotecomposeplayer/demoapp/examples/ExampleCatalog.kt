package io.github.deanalvero.remotecomposeplayer.demoapp.examples

object ExampleCatalog {
    val analogClock: Example.Document = Example.Document(
        id = "analog-clock",
        title = "Analog Clock",
        subtitle = "A ticking clock face driven by Remote Compose float expressions",
        creatorDslCode = """
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
            """
    )

    val digitalClock: Example.Document = Example.Document(
        id = "digital-clock",
        title = "Digital Clock",
        subtitle = "A ticking digital display driven by Remote Compose float-to-text expressions",
        creatorDslCode = """
            Column(
                modifier = Modifier.fillMaxSize().background(0xFF121212.toInt()),
                horizontal = RcHorizontalPositioning.Center,
                vertical = RcColumnVerticalPositioning.Center
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val w = componentWidth()
                    val h = componentHeight()
                    val cx = w / 2f
                    val cy = h / 2f

                    val days = remoteArrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    val months = remoteArrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

                    val dayName = days[dayOfWeek() - 1f.rf]
                    val monthName = months[month() - 1f.rf]

                    val trueMinutes = minutes() - (hour() * 60f.rf)
                    val trueSeconds = seconds() - (trueMinutes * 60f.rf)

                    val hStr = createTextFromFloat(hour(), 2, 0, 12)
                    val mStr = createTextFromFloat(trueMinutes, 2, 0, 12)
                    val sStr = createTextFromFloat(trueSeconds, 2, 0, 12)

                    val dStr = createTextFromFloat(dayOfMonth(), 1, 0, 4)

                    val timeString = hStr + ":" + mStr + ":" + sStr
                    val dateString = dayName + ", " + monthName + " " + dStr

                    applyPaint {
                        setColor(0xFF00E676.toInt())
                        setTextSize(w / 16f)
                    }
                    drawTextAnchored(timeString, cx, cy - (w / 20f), 0.5f.rf, 0.5f.rf)

                    applyPaint {
                        setColor(0xFFBDBDBD.toInt())
                        setTextSize(w / 25f)
                    }
                    drawTextAnchored(dateString, cx, cy + (w / 12f), 0.5f.rf, 0.5f.rf)
                }
            }
            """
    )

    val all: List<Example> = listOf(
        Example.Playground,
        analogClock,
        digitalClock
    )

    fun bytesFor(example: Example.Document): ByteArray = when (example.id) {
        analogClock.id -> SampleDocuments.analogClock()
        digitalClock.id -> SampleDocuments.digitalClock()
        else -> error("No bundled bytes registered for example '${example.id}'")
    }
}
