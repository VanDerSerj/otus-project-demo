package ru.otus.otuskotlin.mrosystem.springapp.api.controller

import org.springframework.web.bind.annotation.*
import ru.otus.api.models.*
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.mappers.*
import ru.otus.otuskotlin.mrosystem.springapp.service.MrosTaskBlockingProcessor

@RestController
@RequestMapping("task")
class TaskController(private val processor: MrosTaskBlockingProcessor) {

    @PostMapping("create")
    fun createTask(@RequestBody request: TaskCreateRequest): TaskCreateResponse {
        val context = MrosContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    fun readTask(@RequestBody request: TaskReadRequest): TaskReadResponse {
        val context = MrosContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportRead()
    }

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateTask(@RequestBody request: TaskUpdateRequest): TaskUpdateResponse {
        val context = MrosContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteTask(@RequestBody request: TaskDeleteRequest): TaskDeleteResponse {
        val context = MrosContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    fun searchTask(@RequestBody request: TaskSearchRequest): TaskSearchResponse {
        val context = MrosContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportSearch()
    }

}