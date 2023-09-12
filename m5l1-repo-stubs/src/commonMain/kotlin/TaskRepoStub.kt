import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.repo.*

class TaskRepoStub: ITaskRepository {

    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
        return DbTaskResponse(
            data = MrosTaskStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse {
        return DbTaskResponse(
            data = MrosTaskStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse {
        return DbTaskResponse(
            data = MrosTaskStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse {
        return DbTaskResponse(
            data = MrosTaskStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse {
        return DbTasksResponse(
            data = MrosTaskStub.prepareSearchList(filter = "", MrosPerformSide.ENGINEER),
            isSuccess = true,
        )
    }

}