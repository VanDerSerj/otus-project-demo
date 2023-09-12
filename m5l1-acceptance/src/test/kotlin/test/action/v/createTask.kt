package test.action.v

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.api.models.*

suspend fun Client.createTask(task: TaskCreateObject = someCreateTask, mode: TaskDebug = debug): TaskResponseObject =
    createTask(task, mode) {
        it should haveSuccessResult
        it.task shouldNotBe null
        it.task?.apply {
            title shouldBe task.title
            description shouldBe task.description
            taskType shouldBe task.taskType
            visibility shouldBe task.visibility
        }
        it.task!!
    }

suspend fun <T> Client.createTask(
    task: TaskCreateObject = someCreateTask,
    mode: TaskDebug = debug,
    block: (TaskCreateResponse) -> T
): T =
    withClue("createTask: $task") {
        val response = sendAndReceive(
            "task/create", TaskCreateRequest(
                requestType = "create",
                debug = mode,
                task = task
            )
        ) as TaskCreateResponse

        response.asClue(block)
    }