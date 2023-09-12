package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MrosState.RUNNING }
    handle {
        taskRepoRead = taskValidated.deepCopy()
        taskRepoPrepare = taskRepoRead

    }
}