package ru.otus.otuskotlin.mrosystem.mappers.v1

import ru.otus.api.v1.models.*
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import ru.otus.otuskotlin.mrosystem.mappers.v1.exceptions.UnknownRequestClass

fun MrosContext.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
    is TaskOffersRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTaskId() = this?.let { MrosTaskId(it) } ?: MrosTaskId.NONE
private fun String?.toTaskWithId() = MrosTask(id = this.toTaskId())
private fun IRequest?.requestId() = this?.requestId?.let { MrosRequestId(it) } ?: MrosRequestId.NONE

private fun TaskDebug?.transportToWorkMode(): MrosWorkMode = when (this?.mode) {
    TaskRequestDebugMode.PROD -> MrosWorkMode.PROD
    TaskRequestDebugMode.TEST -> MrosWorkMode.TEST
    TaskRequestDebugMode.STUB -> MrosWorkMode.STUB
    null -> MrosWorkMode.PROD
}

private fun TaskDebug?.transportToStubCase(): MrosStubs = when (this?.stub) {
    TaskRequestDebugStubs.SUCCESS -> MrosStubs.SUCCESS
    TaskRequestDebugStubs.NOT_FOUND -> MrosStubs.NOT_FOUND
    TaskRequestDebugStubs.BAD_ID -> MrosStubs.BAD_ID
    TaskRequestDebugStubs.BAD_TITLE -> MrosStubs.BAD_TITLE
    TaskRequestDebugStubs.BAD_DESCRIPTION -> MrosStubs.BAD_DESCRIPTION
    TaskRequestDebugStubs.BAD_VISIBILITY -> MrosStubs.BAD_VISIBILITY
    TaskRequestDebugStubs.CANNOT_DELETE -> MrosStubs.CANNOT_DELETE
    TaskRequestDebugStubs.BAD_SEARCH_STRING -> MrosStubs.BAD_SEARCH_STRING
    null -> MrosStubs.NONE
}

fun MrosContext.fromTransport(request: TaskCreateRequest) {
    command = MrosCommand.CREATE
    requestId = request.requestId()
    taskRequest = request.task?.toInternal() ?: MrosTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MrosContext.fromTransport(request: TaskReadRequest) {
    command = MrosCommand.READ
    requestId = request.requestId()
    taskRequest = request.task?.id.toTaskWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MrosContext.fromTransport(request: TaskUpdateRequest) {
    command = MrosCommand.UPDATE
    requestId = request.requestId()
    taskRequest = request.task?.toInternal() ?: MrosTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MrosContext.fromTransport(request: TaskDeleteRequest) {
    command = MrosCommand.DELETE
    requestId = request.requestId()
    taskRequest = request.task?.id.toTaskWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MrosContext.fromTransport(request: TaskSearchRequest) {
    command = MrosCommand.SEARCH
    requestId = request.requestId()
    taskFilterRequest = request.taskFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MrosContext.fromTransport(request: TaskOffersRequest) {
    command = MrosCommand.OFFERS
    requestId = request.requestId()
    taskRequest = request.task?.id.toTaskWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskSearchFilter?.toInternal(): MrosTaskFilter = MrosTaskFilter(
    searchString = this?.searchString ?: ""
)

private fun TaskCreateObject.toInternal(): MrosTask = MrosTask(
    title = this.title ?: "",
    description = this.description ?: "",
    taskType = this.taskType.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun TaskUpdateObject.toInternal(): MrosTask = MrosTask(
    id = this.id.toTaskId(),
    title = this.title ?: "",
    description = this.description ?: "",
    taskType = this.taskType.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun TaskVisibility?.fromTransport(): MrosVisibility = when (this) {
    TaskVisibility.PUBLIC -> MrosVisibility.VISIBLE_PUBLIC
    TaskVisibility.ENGINEER_ONLY -> MrosVisibility.VISIBLE_TO_ENGINEER
    TaskVisibility.MECHANIC_ONLY -> MrosVisibility.VISIBLE_TO_MECHANIC
    null -> MrosVisibility.NONE
}

private fun PerformSide?.fromTransport(): MrosPerformSide = when (this) {
    PerformSide.ENGINEER -> MrosPerformSide.ENGINEER
    PerformSide.MECHANIC -> MrosPerformSide.MECHANIC
    null -> MrosPerformSide.NONE
}

