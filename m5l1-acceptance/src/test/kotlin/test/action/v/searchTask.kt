package test.action.v

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.api.models.*

suspend fun Client.searchTask(search: TaskSearchFilter, mode: TaskDebug = debug): List<TaskResponseObject> = searchTask(search, mode) {
    it should haveSuccessResult
    it.tasks ?: listOf()
}

suspend fun <T> Client.searchTask(search: TaskSearchFilter, mode: TaskDebug = debug, block: (TaskSearchResponse) -> T): T =
    withClue("searchTask: $search") {
        val response = sendAndReceive(
            "task/search",
            TaskSearchRequest(
                requestType = "search",
                debug = mode,
                taskFilter = search,
            )
        ) as TaskSearchResponse

        response.asClue(block)
    }