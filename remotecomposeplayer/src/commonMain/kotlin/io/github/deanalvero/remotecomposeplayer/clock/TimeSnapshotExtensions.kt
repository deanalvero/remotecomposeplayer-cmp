package io.github.deanalvero.remotecomposeplayer.clock

fun RemoteClock.TimeSnapshot.timeInMin(): Float {
    return (hour * 60 + minute).toFloat()
}

fun RemoteClock.TimeSnapshot.timeInSec(): Float {
    return (minute * 60 + second).toFloat()
}