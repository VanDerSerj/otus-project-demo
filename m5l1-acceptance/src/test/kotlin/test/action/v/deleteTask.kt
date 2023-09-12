package test.action.v

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.api.models.TaskDebug
import ru.otus.api.models.TaskDeleteObject
import ru.otus.api.models.TaskDeleteRequest
import ru.otus.api.models.TaskDeleteResponse
import test.action.beValidId
import test.action.beValidLock

suspend fun Client.deleteTask(id: String?, lock: String?, mode: TaskDebug = debug) {
    withClue("deleteTask: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "task/delete",
            TaskDeleteRequest(
                requestType = "delete",
                debug = mode,
                task = TaskDeleteObject(id = id, lock = lock)
            )
        ) as TaskDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.task shouldNotBe null
            response.task?.id shouldBe id
        }
    }
}