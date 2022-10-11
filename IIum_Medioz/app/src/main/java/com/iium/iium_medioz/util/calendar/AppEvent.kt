package com.iium.iium_medioz.util.calendar

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate

fun SimpleSharedFlow() = MutableSharedFlow<Unit>(1, 0, BufferOverflow.DROP_OLDEST)
fun MutableSharedFlow<Unit>.emit() = tryEmit(Unit)

object AppEvent {
    val onWeathyUpdated = SimpleSharedFlow()
    val onNavigateCurWeathyInCalendar = MutableSharedFlow<LocalDate>(1, 0, BufferOverflow.DROP_OLDEST)
}