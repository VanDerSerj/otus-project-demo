package ru.otus.otuskotlin.mrosystem.biz.workers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskId
import ru.otus.otuskotlin.mrosystem.common.models.MrosVisibility
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MrosStubs.SUCCESS && state == MrosState.RUNNING }
    handle {
        state = MrosState.FINISHING
        val stub = MrosTaskStub.prepareResult {
            taskRequest.id.takeIf { it != MrosTaskId.NONE }?.also { this.id = it }
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            taskRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            taskRequest.taskType.takeIf { it != MrosPerformSide.NONE }?.also { this.taskType = it }
            taskRequest.visibility.takeIf { it != MrosVisibility.NONE }?.also { this.visibility = it }
        }
        taskResponse = stub
    }
}