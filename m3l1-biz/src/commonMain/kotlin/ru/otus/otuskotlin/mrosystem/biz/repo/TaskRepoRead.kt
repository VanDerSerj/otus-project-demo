package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskIdRequest
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == MrosState.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskValidated)
        val result = taskRepo.readTask(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            taskRepoRead = resultAd
        } else {
            state = MrosState.FAILING
            errors.addAll(result.errors)
        }
    }
}