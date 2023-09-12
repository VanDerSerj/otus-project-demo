package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskIdRequest
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == MrosState.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskRepoPrepare)
        val result = taskRepo.deleteTask(request)
        if (!result.isSuccess) {
            state = MrosState.FAILING
            errors.addAll(result.errors)
        }
        taskRepoDone = taskRepoRead
    }
}