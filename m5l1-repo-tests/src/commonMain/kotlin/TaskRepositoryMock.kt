import ru.otus.otuskotlin.mrosystem.common.repo.*

class TaskRepositoryMock(
    private val invokeCreateAd: (DbTaskRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadAd: (DbTaskIdRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateAd: (DbTaskRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteAd: (DbTaskIdRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchAd: (DbTaskFilterRequest) -> DbTasksResponse = { DbTasksResponse.MOCK_SUCCESS_EMPTY },
) : ITaskRepository {
    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
        return invokeCreateAd(rq)
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse {
        return invokeReadAd(rq)
    }

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse {
        return invokeUpdateAd(rq)
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse {
        return invokeDeleteAd(rq)
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse {
        return invokeSearchAd(rq)
    }
}