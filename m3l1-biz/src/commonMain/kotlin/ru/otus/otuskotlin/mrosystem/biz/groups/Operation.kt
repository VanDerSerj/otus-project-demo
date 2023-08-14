package ru.otus.otuskotlin.mrosystem.biz.groups

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosCommand
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.chain

fun ICorChainDsl<MrosContext>.operation(title: String, command: MrosCommand, block: ICorChainDsl<MrosContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == MrosState.RUNNING }
}