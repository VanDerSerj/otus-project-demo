package test.action.v

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import ru.otus.api.models.*
import test.action.beValidId

suspend fun Client.readTask(id: String?, mode: TaskDebug = debug): TaskResponseObject = readTask(id, mode) {
    it should haveSuccessResult
    it.task shouldNotBe null
    it.task!!
}

suspend fun <T> Client.readTask(id: String?, mode: TaskDebug = debug, block: (TaskReadResponse) -> T): T =
    withClue("readTask: $id") {
        id should beValidId

        val response = sendAndReceive(
            "task/read",
            TaskReadRequest(
                requestType = "read",
                debug = mode,
                task = TaskReadObject(id = id)
            )
        ) as TaskReadResponse

        response.asClue(block)
    }