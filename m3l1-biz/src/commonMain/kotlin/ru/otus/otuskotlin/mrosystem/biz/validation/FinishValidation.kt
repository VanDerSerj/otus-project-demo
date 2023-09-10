package ru.otus.otuskotlin.mrosystem.biz.validation

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == MrosState.RUNNING }
    handle {
        taskValidated = taskValidating
    }
}

fun ICorChainDsl<MrosContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == MrosState.RUNNING }
    handle {
        taskFilterValidated = taskFilterValidating
    }
}