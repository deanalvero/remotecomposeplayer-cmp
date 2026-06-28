package io.github.deanalvero.remotecomposeplayer.demoapp.examples

object ExampleCatalog {
    val analogClock: Example.Document = Example.Document(
        id = "analog-clock",
        title = "Analog Clock",
        subtitle = "A ticking clock face driven by Remote Compose float expressions"
    )

    val all: List<Example> = listOf(
        Example.Playground,
        analogClock,
    )

    fun bytesFor(example: Example.Document): ByteArray = when (example.id) {
        analogClock.id -> SampleDocuments.analogClock()
        else -> error("No bundled bytes registered for example '${example.id}'")
    }
}
