package io.github.deanalvero.remotecomposeplayer.demoapp.examples

import remotecomposeplayercmp.sample.app.shared.generated.resources.Res

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

    val verticalScroll: Example.Document = Example.Document(
        id = "vertical-scroll",
        title = "Vertical Scroll",
        subtitle = "A scrollable column of rows driven by Remote Compose verticalScroll modifier",
        creatorDslCode = """
            Column(
                modifier = Modifier.fillMaxSize().padding(32f).verticalScroll(),
                horizontal = RcHorizontalPositioning.Center
            ) {
                repeat(50) {
                    Text(text = "Text ${'$'}{it + 1}")
                }
            }
            """
    )

    val horizontalScroll: Example.Document = Example.Document(
        id = "horizontal-scroll",
        title = "Horizontal Scroll",
        subtitle = "A scrollable row of text driven by Remote Compose horizontalScroll modifier",
        creatorDslCode = """
            Row(
                modifier = Modifier.fillMaxSize().padding(32f).horizontalScroll(),
                vertical = RcVerticalPositioning.Center
            ) {
                repeat(50) {
                    Text(text = "Text ${'$'}{it + 1}")
                }
            }
            """
    )

    val ticTacToe: Example.Document = Example.Document(
        id = "tic-tac-toe",
        title = "Tic Tac Toe",
        subtitle = "A fully playable board driven by Remote Compose integer expressions and bitwise logic",
        creatorDslCode = """
            val t0 = remoteNamedInteger("t0", 0)
            val t1 = remoteNamedInteger("t1", 0)
            val t2 = remoteNamedInteger("t2", 0)
            val t3 = remoteNamedInteger("t3", 0)
            val t4 = remoteNamedInteger("t4", 0)
            val t5 = remoteNamedInteger("t5", 0)
            val t6 = remoteNamedInteger("t6", 0)
            val t7 = remoteNamedInteger("t7", 0)
            val t8 = remoteNamedInteger("t8", 0)
            val turn = remoteNamedInteger("turn", 1)
            val gameState = remoteNamedInteger("gameState", 0)
            
            val cells = listOf(t0, t1, t2, t3, t4, t5, t6, t7, t8)
            
            fun cellValueAt(index: Int, playedIndex: Int, played: RcInteger): RcInteger {
                return if (index == playedIndex) played else cells[index]
            }
            
            fun winnerExpr(playedIndex: Int, played: RcInteger): RcInteger {
                val b0 = cellValueAt(0, playedIndex, played)
                val b1 = cellValueAt(1, playedIndex, played)
                val b2 = cellValueAt(2, playedIndex, played)
                val b3 = cellValueAt(3, playedIndex, played)
                val b4 = cellValueAt(4, playedIndex, played)
                val b5 = cellValueAt(5, playedIndex, played)
                val b6 = cellValueAt(6, playedIndex, played)
                val b7 = cellValueAt(7, playedIndex, played)
                val b8 = cellValueAt(8, playedIndex, played)
            
                val win = (b0 and b1 and b2) or
                        (b3 and b4 and b5) or
                        (b6 and b7 and b8) or
                        (b0 and b3 and b6) or
                        (b1 and b4 and b7) or
                        (b2 and b5 and b8) or
                        (b0 and b4 and b8) or
                        (b2 and b4 and b6)
            
                val fullBoard = ((b0 and 1.ri) or (b0 shr 1)) and
                    ((b1 and 1.ri) or (b1 shr 1)) and
                    ((b2 and 1.ri) or (b2 shr 1)) and
                    ((b3 and 1.ri) or (b3 shr 1)) and
                    ((b4 and 1.ri) or (b4 shr 1)) and
                    ((b5 and 1.ri) or (b5 shr 1)) and
                    ((b6 and 1.ri) or (b6 shr 1)) and
                    ((b7 and 1.ri) or (b7 shr 1)) and
                    ((b8 and 1.ri) or (b8 shr 1))
            
                val draw = fullBoard or (fullBoard shl 1)
                return win or draw
            }
            
            fun RcActionScope.play(index: Int, cell: RcInteger) {
                val played = turn + 0
                setValue(cell, played)
                setValue(gameState, winnerExpr(index, played))
                setValue(turn, (turn % 2) + 1)
            }
            
            StateLayout(gameState) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.rdp),
                    horizontal = RcHorizontalPositioning.Center,
                    vertical = RcColumnVerticalPositioning.Center,
                ) {
                    val rows = listOf(
                        listOf(0 to t0, 1 to t1, 2 to t2),
                        listOf(3 to t3, 4 to t4, 5 to t5),
                        listOf(6 to t6, 7 to t7, 8 to t8),
                    )
            
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontal = RcRowHorizontalPositioning.Center,
                            vertical = RcVerticalPositioning.Center,
                        ) {
                            row.forEach { (index, cell) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(6.rdp)
                                        .height(72.rdp)
                                        .background(0xFF1F2937.toInt())
                                        .border(
                                            1.5f,
                                            14f,
                                            RcColor(0xFF4B5563.toInt()),
                                            RcBorderShape.RoundedRectangle
                                        ),
                                    horizontal = RcHorizontalPositioning.Center,
                                    vertical = RcVerticalPositioning.Center
                                ) {
                                    StateLayout(
                                        stateIndex = cell,
                                        modifier = Modifier.fillParentMaxSize()
                                    ) {
                                        Text(
                                            text = " ",
                                            modifier = Modifier
                                                .fillParentMaxSize()
                                                .onClick {
                                                    play(index, cell)
                                                },
                                            weight = RcWeight.Bold,
                                            color = 0xFFF9FAFB.toInt()
                                        )
            
                                        Text(
                                            text = "X",
                                            modifier = Modifier.fillParentMaxSize(),
                                            weight = RcWeight.Bold,
                                            fontSize = 30.rsp,
                                            textAlign = RcTextAlign.Center,
                                            color = 0xFFF9FAFB.toInt()
                                        )
            
                                        Text(
                                            text = "O",
                                            modifier = Modifier.fillParentMaxSize(),
                                            weight = RcWeight.Bold,
                                            fontSize = 30.rsp,
                                            textAlign = RcTextAlign.Center,
                                            color = 0xFFF9FAFB.toInt()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            
                Box(
                    modifier = Modifier.fillMaxSize(),
                    horizontal = RcHorizontalPositioning.Center,
                    vertical = RcVerticalPositioning.Center
                ) {
                    Text(
                        text = "X wins!",
                        fontSize = 30.rsp,
                        weight = RcWeight.Bold,
                    )
                }
            
                Box(
                    modifier = Modifier.fillMaxSize(),
                    horizontal = RcHorizontalPositioning.Center,
                    vertical = RcVerticalPositioning.Center
                ) {
                    Text(
                        text = "O wins!",
                        fontSize = 30.rsp,
                        weight = RcWeight.Bold,
                    )
                }
            
            
                Box(
                    modifier = Modifier.fillMaxSize(),
                    horizontal = RcHorizontalPositioning.Center,
                    vertical = RcVerticalPositioning.Center
                ) {
                    Text(
                        text = "Draw!!!",
                        fontSize = 30.rsp,
                        weight = RcWeight.Bold,
                    )
                }
            }
            """
    )

    val pieChart: Example.Document = Example.Document(
        id = "pie-chart",
        title = "Pie Chart",
        subtitle = "A custom pie chart driven by Remote Compose canvas sectors and anchored text",
        creatorDslCode = """
            val data = floatArrayOf(68f, 31f)
            val names = arrayOf("Android", "iOS")
            val colors = intArrayOf(
                0xFF3DDC84.toInt(),
                0xFFFF3B30.toInt(),
            )
            
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = componentWidth()
                    val h = componentHeight()
                    val cx = w / 2f
                    val cy = h / 2f
                    val radius = min(w, h) * 0.4f
            
                    val total = data.sum()
                    var currentAngle = 0f
            
                    for (i in data.indices) {
                        val sweepAngle = (data[i] / total) * 360f
            
                        paint {
                            color(colors[i % colors.size])
                            style(RcPaintStyle.Fill)
                        }
                        drawSector(
                            cx - radius,
                            cy - radius,
                            cx + radius,
                            cy + radius,
                            currentAngle.rf,
                            sweepAngle.rf,
                        )
            
                        paint {
                            color(0xFFFFFFFF.toInt())
                            style(RcPaintStyle.Stroke)
                            strokeWidth(2f)
                        }
                        drawSector(
                            cx - radius,
                            cy - radius,
                            cx + radius,
                            cy + radius,
                            currentAngle.rf,
                            sweepAngle.rf,
                        )
            
                        val labelAngle = (currentAngle + sweepAngle / 2f) * PI.toFloat() / 180f
                        val labelRadius = radius * 0.5f
                        val lx = cx + labelRadius * cos(labelAngle)
                        val ly = cy + labelRadius * sin(labelAngle)
            
                        paint {
                            color(0xFFFFFFFF.toInt())
                            textSize(12f)
                            style(RcPaintStyle.Fill)
                            typeface(RcFontType.Default, RcWeight.Bold, italic = false)
                        }
                        drawTextAnchored(remoteText(names[i]), lx, ly, 0.5f.rf, 0.5f.rf, 0)
            
                        currentAngle += sweepAngle
                    }
                }
            }
            """
    )

    val all: List<Example> = listOf(
        Example.Playground,
        analogClock,
        digitalClock,
        ticTacToe,
        pieChart,
        verticalScroll,
        horizontalScroll
    )

    suspend fun bytesFor(example: Example.Document): ByteArray {
        return Res.readBytes("files/${example.id}.rc")
    }
}
