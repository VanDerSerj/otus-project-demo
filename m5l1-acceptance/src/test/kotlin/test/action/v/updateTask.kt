package test.action.v

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.api.models.TaskUpdateRequest
import ru.otus.api.models.TaskUpdateResponse
import ru.otus.api.models.*
import test.action.beValidId
import test.action.beValidLock

suspend fun Client.updateTask(id: String?, lock: String?, task: TaskUpdateObject, mode: TaskDebug = debug): TaskResponseObject =
    updateTask(id, lock, task, mode) {
        it should haveSuccessResult
        it.task shouldNotBe null
        it.task?.apply {
            if (task.title != null)
                title shouldBe task.title
            if (task.description != null)
                description shouldBe task.description
            if (task.taskType != null)
                taskType shouldBe task.taskType
            if (task.visibility != null)
                visibility shouldBe task.visibility
        }
        it.task!!
    }

suspend fun <T> Client.updateTask(id: String?, lock: String?, task: TaskUpdateObject, mode: TaskDebug = debug, block: (TaskUpdateResponse) -> T): T =
    withClue("updated: $id, lock: $lock, set: $task") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "task/update", TaskUpdateRequest(
                requestType = "update",
                debug = mode,
                task = task.copy(id = id, lock = lock)
            )
        ) as TaskUpdateResponse

        response.asClue(block)
    }