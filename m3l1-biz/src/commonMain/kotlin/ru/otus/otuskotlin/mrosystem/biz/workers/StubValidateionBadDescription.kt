package ru.otus.otuskotlin.mrosystem.biz.workers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    on { stubCase == MrosStubs.BAD_DESCRIPTION && state == MrosState.RUNNING }
    handle {
        state = MrosState.FAILING
        this.errors.add(
            MrosError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}