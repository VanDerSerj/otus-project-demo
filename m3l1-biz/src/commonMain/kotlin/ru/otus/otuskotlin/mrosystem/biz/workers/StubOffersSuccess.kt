package ru.otus.otuskotlin.mrosystem.biz.workers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskId
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.stubOffersSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MrosStubs.SUCCESS && state == MrosState.RUNNING }
    handle {
        state = MrosState.FINISHING
        taskResponse = MrosTaskStub.prepareResult {
            taskRequest.id.takeIf { it != MrosTaskId.NONE }?.also { this.id = it }
        }
        tasksResponse.addAll(MrosTaskStub.prepareOffersList(taskResponse.title, MrosPerformSide.MECHANIC))
    }
}