package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskFilterRequest
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск заданий в БД по фильтру"
    on { state == MrosState.RUNNING }
    handle {
        val request = DbTaskFilterRequest(
            titleFilter = taskFilterValidated.searchString,
            ownerId = taskFilterValidated.ownerId,
            performSide = taskFilterValidated.dealSide,
        )
        val result = taskRepo.searchTask(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            tasksRepoDone = resultAds.toMutableList()
        } else {
            state = MrosState.FAILING
            errors.addAll(result.errors)
        }
    }
}