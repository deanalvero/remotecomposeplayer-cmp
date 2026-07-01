package io.github.deanalvero.remotecomposeplayer.demoapp.examples

sealed interface Example {
    val id: String
    val title: String
    val subtitle: String

    object Playground : Example {
        override val id: String = "playground"
        override val title: String = "Playground"
        override val subtitle: String = "Build and preview a document live in the editor"
    }

    data class Document(
        override val id: String,
        override val title: String,
        override val subtitle: String,
        val creatorDslCode: String
    ) : Example
}
