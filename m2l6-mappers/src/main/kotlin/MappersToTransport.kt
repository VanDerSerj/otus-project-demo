package ru.otus.otuskotlin.mrosystem.mappers

import ru.otus.api.models.*
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.mappers.exceptions.UnknownMrosCommand

fun MrosContext.toTransportTask(): IResponse = when (val cmd = command) {
    MrosCommand.CREATE -> toTransportCreate()
    MrosCommand.READ -> toTransportRead()
    MrosCommand.UPDATE -> toTransportUpdate()
    MrosCommand.DELETE -> toTransportDelete()
    MrosCommand.SEARCH -> toTransportSearch()
    MrosCommand.OFFERS -> toTransportOffers()
    MrosCommand.NONE -> throw UnknownMrosCommand(cmd)
}

fun MrosContext.toTransportCreate() = TaskCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MrosState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MrosContext.toTransportRead() = TaskReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MrosState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MrosContext.toTransportUpdate() = TaskUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MrosState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MrosContext.toTransportDelete() = TaskDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MrosState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MrosContext.toTransportSearch() = TaskSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MrosState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    tasks = tasksResponse.toTransportTask()
)

fun MrosContext.toTransportOffers() = TaskOffersResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MrosState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    tasks = tasksResponse.toTransportTask()
)

fun List<MrosTask>.toTransportTask(): List<TaskResponseObject>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun MrosContext.toTransportInit() = TaskInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun MrosTask.toTransportTask(): TaskResponseObject = TaskResponseObject(
    id = id.takeIf { it != MrosTaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MrosUserId.NONE }?.asString(),
    taskType = taskType.toTransportTask(),
    visibility = visibility.toTransportTask(),
    permissions = permissionsClient.toTransportTask(),
)

private fun Set<MrosTaskPermissionClient>.toTransportTask(): Set<TaskPermissions>? = this
    .map { it.toTransportTask() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MrosTaskPermissionClient.toTransportTask() = when (this) {
    MrosTaskPermissionClient.READ -> TaskPermissions.READ
    MrosTaskPermissionClient.UPDATE -> TaskPermissions.UPDATE
    MrosTaskPermissionClient.MAKE_VISIBLE_OWNER -> TaskPermissions.MAKE_VISIBLE_OWN
    MrosTaskPermissionClient.MAKE_VISIBLE_GROUP -> TaskPermissions.MAKE_VISIBLE_GROUP
    MrosTaskPermissionClient.MAKE_VISIBLE_PUBLIC -> TaskPermissions.MAKE_VISIBLE_PUBLIC
    MrosTaskPermissionClient.DELETE -> TaskPermissions.DELETE
}

private fun MrosVisibility.toTransportTask(): TaskVisibility? = when (this) {
    MrosVisibility.VISIBLE_PUBLIC -> TaskVisibility.PUBLIC
    MrosVisibility.VISIBLE_TO_MECHANIC -> TaskVisibility.MECHANIC_ONLY
    MrosVisibility.VISIBLE_TO_ENGINEER -> TaskVisibility.MECHANIC_ONLY
    MrosVisibility.NONE -> null
}

private fun MrosPerformSide.toTransportTask(): PerformSide? = when (this) {
    MrosPerformSide.ENGINEER -> PerformSide.ENGINEER
    MrosPerformSide.MECHANIC -> PerformSide.MECHANIC
    MrosPerformSide.NONE -> null
}

private fun List<MrosError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MrosError.toTransportTask() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
