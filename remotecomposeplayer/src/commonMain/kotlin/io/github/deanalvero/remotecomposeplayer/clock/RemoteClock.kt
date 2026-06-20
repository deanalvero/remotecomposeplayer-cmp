package io.github.deanalvero.remotecomposeplayer.clock

interface RemoteClock {
    fun snapshot(): TimeSnapshot

    interface TimeSnapshot {
        val continuousSeconds: Float
    }
}