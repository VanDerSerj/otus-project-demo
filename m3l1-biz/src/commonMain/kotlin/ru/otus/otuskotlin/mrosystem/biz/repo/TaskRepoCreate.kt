package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskRequest
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == MrosState.RUNNING }
    handle {
        val request = DbTaskRequest(taskRepoPrepare)
        val result = taskRepo.createTask(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            taskRepoDone = resultAd
        } else {
            state = MrosState.FAILING
            errors.addAll(result.errors)
        }
    }
}