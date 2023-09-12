package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskRequest
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == MrosState.RUNNING }
    handle {
        val request = DbTaskRequest(taskRepoPrepare)
        val result = taskRepo.updateTask(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            taskRepoDone = resultAd
        } else {
            state = MrosState.FAILING
            errors.addAll(result.errors)
            taskRepoDone
        }
    }
}