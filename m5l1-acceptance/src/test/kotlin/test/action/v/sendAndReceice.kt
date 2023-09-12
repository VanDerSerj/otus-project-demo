package test.action.v

import fixture.client.Client
import mu.KotlinLogging
import ru.otus.api.models.IRequest
import ru.otus.api.models.IResponse
import ru.otus.otuskotlin.mrosystem.api.apiRequestSerialize
import ru.otus.otuskotlin.mrosystem.api.apiResponseDeserialize

private val log = KotlinLogging.logger {}
suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiRequestSerialize(request)
    log.info { "Send to $path\n$requestBody" }

    val responseBody = sendAndReceive("", path, requestBody)
    log.info { "Received\n$responseBody" }

    return apiResponseDeserialize(responseBody)
}