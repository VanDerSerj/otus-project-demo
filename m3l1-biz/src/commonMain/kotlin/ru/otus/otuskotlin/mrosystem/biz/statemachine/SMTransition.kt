package ru.otus.otuskotlin.mrosystem.biz.statemachine

import ru.otus.otuskotlin.mrosystem.common.statemachine.SMTaskStates

data class SMTransition (
    val state: SMTaskStates,
    val description: String,
)