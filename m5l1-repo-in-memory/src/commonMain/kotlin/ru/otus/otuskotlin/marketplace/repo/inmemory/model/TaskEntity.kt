package ru.otus.otuskotlin.marketplace.repo.inmemory.model

import ru.otus.otuskotlin.mrosystem.common.models.*

data class TaskEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val taskType: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: MrosTask): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        taskType = model.taskType.takeIf { it != MrosPerformSide.NONE }?.name,
        visibility = model.visibility.takeIf { it != MrosVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = MrosTask(
        id = id?.let { MrosTaskId(it) }?: MrosTaskId.NONE,
        title = title?: "",
        description = description?: "",
        ownerId = ownerId?.let { MrosUserId(it) }?: MrosUserId.NONE,
        taskType = taskType?.let { MrosPerformSide.valueOf(it) }?: MrosPerformSide.NONE,
        visibility = visibility?.let { MrosVisibility.valueOf(it) }?: MrosVisibility.NONE,
        lock = lock?.let { MrosTaskLock(it) } ?: MrosTaskLock.NONE,
    )
}
