package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoPrepareOffers(title: String) = worker {
    this.title = title
    description = "Готовим данные к поиску предложений в БД"
    on { state == MrosState.RUNNING }
    handle {
        taskRepoPrepare = taskRepoRead.deepCopy()
        taskRepoDone = taskRepoRead.deepCopy()
    }
}