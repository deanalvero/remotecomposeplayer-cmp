package io.github.deanalvero.remotecomposeplayer.clock

import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class KtxRemoteClock(
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()
) : RemoteClock {

    override fun snapshot(): RemoteClock.TimeSnapshot {
        val now = clock.now()
        val localDateTime = now.toLocalDateTime(timeZone)

        val hour = localDateTime.hour
        val minute = localDateTime.minute
        val second = localDateTime.second

        val continuousSeconds =
            localDateTime.hour * 3600f +
                    localDateTime.minute * 60f +
                    localDateTime.second +
                    localDateTime.nanosecond / 1_000_000_000f

        return object : RemoteClock.TimeSnapshot {
            override val continuousSeconds: Float = continuousSeconds
            override val dayOfWeek: Int = localDateTime.dayOfWeek.isoDayNumber
            override val dayOfMonth: Int = localDateTime.dayOfMonth
            override val dayOfYear: Int = localDateTime.dayOfYear
            override val year: Int = localDateTime.year
            override val month: Int = localDateTime.month.number
            override val hour: Int = hour
            override val minute: Int = minute
            override val second: Int = second
        }
    }
}