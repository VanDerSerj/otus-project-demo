package ru.otus.otuskotlin.mrosystem.biz.general

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.models.MrosWorkMode
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.chain

fun ICorChainDsl<MrosContext>.stubs(title: String, block: ICorChainDsl<MrosContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MrosWorkMode.STUB && state == MrosState.RUNNING }
}