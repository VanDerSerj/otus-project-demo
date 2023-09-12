package ru.otus.otuskotlin.mrosystem.common.repo

import ru.otus.otuskotlin.mrosystem.common.models.MrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosTask

data class DbTasksResponse(
    override val data: List<MrosTask>?,
    override val isSuccess: Boolean,
    override val errors: List<MrosError> = emptyList(),
): IDbResponse<List<MrosTask>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbTasksResponse(emptyList(), true)
        fun success(result: List<MrosTask>) = DbTasksResponse(result, true)
        fun error(errors: List<MrosError>) = DbTasksResponse(null, false, errors)
        fun error(error: MrosError) = DbTasksResponse(null, false, listOf(error))
    }

}
