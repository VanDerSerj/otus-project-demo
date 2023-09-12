package ru.otus.otuskotlin.mrosystem.common.repo

import ru.otus.otuskotlin.mrosystem.common.models.MrosTask
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskId
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskLock

data class DbTaskIdRequest(
    val id: MrosTaskId,
    val lock: MrosTaskLock = MrosTaskLock.NONE,
) {
    constructor(task: MrosTask): this(task.id, task.lock)
}