package ru.otus.otuskotlin.mrosystem.common.repo

import ru.otus.otuskotlin.mrosystem.common.helpers.errorEmptyId as mrosErrorEmptyId
import ru.otus.otuskotlin.mrosystem.common.helpers.errorNotFound as mrosErrorNotFound
import ru.otus.otuskotlin.mrosystem.common.helpers.errorRepoConcurrency
import ru.otus.otuskotlin.mrosystem.common.models.MrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosTask
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskLock

data class DbTaskResponse(
    override val data: MrosTask?,
    override val isSuccess: Boolean,
    override val errors: List<MrosError> = emptyList()
): IDbResponse<MrosTask> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbTaskResponse(null, true)
        fun success(result: MrosTask) = DbTaskResponse(result, true)
        fun error(errors: List<MrosError>, data: MrosTask? = null) = DbTaskResponse(data, false, errors)
        fun error(error: MrosError, data: MrosTask? = null) = DbTaskResponse(data, false, listOf(error))

        val errorEmptyId = error(mrosErrorEmptyId)

        fun errorConcurrent(lock: MrosTaskLock, task: MrosTask?) = error(
            errorRepoConcurrency(lock, task?.lock?.let { MrosTaskLock(it.asString()) }),
            task
        )

        val errorNotFound = error(mrosErrorNotFound)
    }
}