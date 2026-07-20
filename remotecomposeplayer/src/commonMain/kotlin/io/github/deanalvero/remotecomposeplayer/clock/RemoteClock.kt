package io.github.deanalvero.remotecomposeplayer.clock

interface RemoteClock {
    fun snapshot(): TimeSnapshot

    interface TimeSnapshot {
        val continuousSeconds: Float
        val dayOfWeek: Int
        val dayOfMonth: Int
        val dayOfYear: Int
        val year: Int
        val month: Int
        val hour: Int
        val minute: Int
        val second: Int
    }
}