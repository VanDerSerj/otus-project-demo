package ru.otus.otuskotlin.mrosystem.biz.workers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MrosStubs.SUCCESS && state == MrosState.RUNNING }
    handle {
        state = MrosState.FINISHING
        tasksResponse.addAll(MrosTaskStub.prepareSearchList(taskFilterRequest.searchString, MrosPerformSide.MECHANIC))
    }
}