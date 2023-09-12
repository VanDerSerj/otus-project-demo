package test.action.v

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.api.models.*

suspend fun Client.offersTask(id: String?, mode: TaskDebug = debug): List<TaskResponseObject> = offersTask(id, mode) {
    it should haveSuccessResult
    it.tasks ?: listOf()
}

suspend fun <T> Client.offersTask(id: String?, mode: TaskDebug = debug, block: (TaskOffersResponse) -> T): T =
    withClue("searchOffers: $id") {
        val response = sendAndReceive(
            "task/offers",
            TaskOffersRequest(
                requestType = "offers",
                debug = mode,
                task = TaskReadObject(id = id),
            )
        ) as TaskOffersResponse

        response.asClue(block)
    }