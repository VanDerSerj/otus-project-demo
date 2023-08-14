package ru.otus.otuskotlin.mrosystem.biz.statemachine

import ru.otus.otuskotlin.mrosystem.common.statemachine.SMTaskStates
import kotlin.time.Duration

data class SMAdSignal (
    val state: SMTaskStates,
    val duration: Duration,
    val views: Int,
)