package ru.otus.otuskotlin.mrosystem.biz.repo

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskFilterRequest
import ru.otus.otuskotlin.mrosystem.common.repo.DbTasksResponse
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.repoOffers(title: String) = worker {
    this.title = title
    description = "Поиск предложений для объявления по названию"
    on { state == MrosState.RUNNING }
    handle {
        val taskRequest = taskRepoPrepare
        val filter = DbTaskFilterRequest(
            titleFilter = taskRequest.title,
            performSide = when (taskRequest.taskType) {
                MrosPerformSide.ENGINEER -> MrosPerformSide.MECHANIC
                MrosPerformSide.MECHANIC -> MrosPerformSide.ENGINEER
                MrosPerformSide.NONE -> MrosPerformSide.NONE
            }
        )
        val dbResponse = if (filter.performSide == MrosPerformSide.NONE) {
            DbTasksResponse(
                data = null,
                isSuccess = false,
                errors = listOf(
                    MrosError(
                        field = "taskType",
                        message = "Type of ad must not be empty"
                    )
                )
            )
        } else {
            taskRepo.searchTask(filter)
        }

        val resultTasks = dbResponse.data
        when {
            !resultTasks.isNullOrEmpty() -> tasksRepoDone = resultTasks.toMutableList()
            dbResponse.isSuccess -> return@handle
            else -> {
                state = MrosState.FAILING
                errors.addAll(dbResponse.errors)
            }
        }
    }
}