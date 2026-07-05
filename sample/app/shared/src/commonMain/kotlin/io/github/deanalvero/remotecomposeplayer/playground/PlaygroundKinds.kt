package io.github.deanalvero.remotecomposeplayer.playground

enum class PlaygroundComponentKind {
    Column,
    Row,
    Box,
    Spacer,
    Canvas,
    Text
}

enum class PlaygroundModifierKind {
    Padding,
    Background,
    Width,
    Height
}

enum class PlaygroundDrawOperationKind {
    Arc,
    Circle,
    Line,
    Oval,
    Rect,
    RoundRect,
    Sector
}
