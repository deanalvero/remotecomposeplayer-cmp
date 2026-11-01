package io.github.deanalvero.remotecomposeplayer.demoapp

import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example

sealed interface CompareItem {
    val title: String

    data class Document(val example: Example.Document) : CompareItem {
        override val title: String get() = example.title
    }

    data object RemoteSource : CompareItem {
        override val title: String get() = "Remote Source"
    }
}