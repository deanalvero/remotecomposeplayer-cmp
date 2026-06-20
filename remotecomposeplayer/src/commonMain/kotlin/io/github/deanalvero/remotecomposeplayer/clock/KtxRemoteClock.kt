package io.github.deanalvero.remotecomposeplayer.clock

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class KtxRemoteClock(
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()
) : RemoteClock {

    override fun snapshot(): RemoteClock.TimeSnapshot {
        val now = clock.now()
        val localDateTime = now.toLocalDateTime(timeZone)

        val continuousSeconds =
            localDateTime.hour * 3600f +
                    localDateTime.minute * 60f +
                    localDateTime.second +
                    localDateTime.nanosecond / 1_000_000_000f

        return object : RemoteClock.TimeSnapshot {
            override val continuousSeconds: Float = continuousSeconds
        }
    }
}