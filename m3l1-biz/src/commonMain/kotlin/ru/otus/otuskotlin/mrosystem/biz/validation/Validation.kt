package ru.otus.otuskotlin.mrosystem.biz.validation

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.chain

fun ICorChainDsl<MrosContext>.validation(block: ICorChainDsl<MrosContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MrosState.RUNNING }
}