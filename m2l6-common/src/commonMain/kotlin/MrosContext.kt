package ru.otus.otuskotlin.mrosystem.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs

data class MrosContext(
    var command: MrosCommand = MrosCommand.NONE,
    var state: MrosState = MrosState.NONE,
    val errors: MutableList<MrosError> = mutableListOf(),
    var settings: MrosCorSettings = MrosCorSettings.NONE,

    var workMode: MrosWorkMode = MrosWorkMode.PROD,
    var stubCase: MrosStubs = MrosStubs.NONE,

    var requestId: MrosRequestId = MrosRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: MrosTask = MrosTask(),
    var taskFilterRequest: MrosTaskFilter = MrosTaskFilter(),

    var taskValidating: MrosTask = MrosTask(),
    var taskFilterValidating: MrosTaskFilter = MrosTaskFilter(),

    var taskValidated: MrosTask = MrosTask(),
    var taskFilterValidated: MrosTaskFilter = MrosTaskFilter(),

    var taskResponse: MrosTask = MrosTask(),
    var tasksResponse: MutableList<MrosTask> = mutableListOf(),
)
