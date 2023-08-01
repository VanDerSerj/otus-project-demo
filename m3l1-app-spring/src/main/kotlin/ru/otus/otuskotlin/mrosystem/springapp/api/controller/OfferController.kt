package ru.otus.otuskotlin.mrosystem.springapp.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.api.models.TaskOffersRequest
import ru.otus.api.models.TaskOffersResponse
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.mappers.fromTransport
import ru.otus.otuskotlin.mrosystem.mappers.toTransportOffers
import ru.otus.otuskotlin.mrosystem.springapp.service.MrosTaskBlockingProcessor

@RestController
@RequestMapping("task")
class OfferController(private val processor: MrosTaskBlockingProcessor) {

    @PostMapping("offers")
    fun searchOffers(@RequestBody request: TaskOffersRequest): TaskOffersResponse {
        val context = MrosContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportOffers()
    }
}