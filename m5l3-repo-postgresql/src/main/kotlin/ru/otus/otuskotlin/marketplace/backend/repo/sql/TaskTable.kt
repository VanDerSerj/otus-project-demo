package ru.otus.otuskotlin.marketplace.backend.repo.sql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.mrosystem.common.models.*

object TaskTable : Table("task") {
    val id = varchar("id", 128)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val owner = varchar("owner", 128)
    val visibility = enumeration("visibility", MrosVisibility::class)
    val performSide = enumeration("perform_side", MrosPerformSide::class)
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = MrosTask(
        id = MrosTaskId(res[id].toString()),
        title = res[title],
        description = res[description],
        ownerId = MrosUserId(res[owner].toString()),
        visibility = res[visibility],
        taskType = res[performSide],
        lock = MrosTaskLock(res[lock])
    )

    fun from(res: ResultRow) = MrosTask(
        id = MrosTaskId(res[id].toString()),
        title = res[title],
        description = res[description],
        ownerId = MrosUserId(res[owner].toString()),
        visibility = res[visibility],
        taskType = res[performSide],
        lock = MrosTaskLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, task: MrosTask, randomUuid: () -> String) {
        it[id] = task.id.takeIf { it != MrosTaskId.NONE }?.asString() ?: randomUuid()
        it[title] = task.title
        it[description] = task.description
        it[owner] = task.ownerId.asString()
        it[visibility] = task.visibility
        it[performSide] = task.taskType
        it[lock] = task.lock.takeIf { it != MrosTaskLock.NONE }?.asString() ?: randomUuid()
    }

}