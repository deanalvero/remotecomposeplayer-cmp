package io.github.deanalvero.remotecomposeplayer.clock

import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class KtxRemoteClockTest {

    @Test
    fun snapshot() {
        // May 01, 2024 03:17 PM EDT
        val input = "2024-05-01T19:17:00Z"
        val clock = KtxRemoteClock(
            clock = object : Clock {
                override fun now(): Instant {
                    return Instant.parse(input)
                }
            },
            timeZone = TimeZone.of("America/Toronto")
        )
        val snapshot = clock.snapshot()
        assertEquals(3, snapshot.dayOfWeek)
        assertEquals(1, snapshot.dayOfMonth)
        assertEquals(122, snapshot.dayOfYear)
        assertEquals(2024, snapshot.year)
        assertEquals(5, snapshot.month)
        assertEquals(15, snapshot.hour)
        assertEquals(17, snapshot.minute)
        assertEquals(0, snapshot.second)
    }
}