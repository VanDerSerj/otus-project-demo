package ru.otus.otuskotlin.mrosystem.biz.workers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MrosStubs.SUCCESS && state == MrosState.RUNNING }
    handle {
        state = MrosState.FINISHING
        val stub = MrosTaskStub.prepareResult {
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        taskResponse = stub
    }
}