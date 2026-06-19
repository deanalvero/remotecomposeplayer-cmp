package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode

fun nodeLabel(node: PlaygroundNode): String {
    return when (node) {
        is PlaygroundNode.Column -> "Column"
        is PlaygroundNode.Row -> "Row"
        is PlaygroundNode.Canvas -> "Canvas"
        is PlaygroundNode.Text -> "Text"
    }
}
