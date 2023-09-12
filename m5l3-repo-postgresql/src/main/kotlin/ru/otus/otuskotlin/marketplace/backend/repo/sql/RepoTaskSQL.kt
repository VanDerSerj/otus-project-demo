package ru.otus.otuskotlin.marketplace.backend.repo.sql

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.mrosystem.common.helpers.asMrosError
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.repo.*

class RepoTaskSQL(
    properties: SqlProperties,
    initObjects: Collection<MrosTask> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : ITaskRepository {

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
        }

        Database.connect(
            properties.url, driver, properties.user, properties.password
        )

        transaction {
            if (properties.dropDatabase) SchemaUtils.drop(TaskTable)
            SchemaUtils.create(TaskTable)
            initObjects.forEach { createTask(it) }
        }
    }

    private fun createTask(task: MrosTask): MrosTask {
        val res = TaskTable.insert {
            to(it, task, randomUuid)
        }

        return TaskTable.from(res)
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbTaskResponse): DbTaskResponse =
        transactionWrapper(block) { DbTaskResponse.error(it.asMrosError()) }

    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse = transactionWrapper {
        DbTaskResponse.success(createTask(rq.task))
    }

    private fun read(id: MrosTaskId): DbTaskResponse {
        val res = TaskTable.select {
            TaskTable.id eq id.asString()
        }.singleOrNull() ?: return DbTaskResponse.errorNotFound
        return DbTaskResponse.success(TaskTable.from(res))
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse = transactionWrapper { read(rq.id) }

    private fun update(
        id: MrosTaskId,
        lock: MrosTaskLock,
        block: (MrosTask) -> DbTaskResponse
    ): DbTaskResponse =
        transactionWrapper {
            if (id == MrosTaskId.NONE) return@transactionWrapper DbTaskResponse.errorEmptyId

            val current = TaskTable.select { TaskTable.id eq id.asString() }
                .firstOrNull()
                ?.let { TaskTable.from(it) }

            when {
                current == null -> DbTaskResponse.errorNotFound
                current.lock != lock -> DbTaskResponse.errorConcurrent(lock, current)
                else -> block(current)
            }
        }

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse =
        update(rq.task.id, rq.task.lock) {
            TaskTable.update({
                (TaskTable.id eq rq.task.id.asString()) and (TaskTable.lock eq rq.task.lock.asString())
            }) {
                to(it, rq.task, randomUuid)
            }
            read(rq.task.id)
        }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse = update(rq.id, rq.lock) {
        TaskTable.deleteWhere {
            (TaskTable.id eq rq.id.asString()) and (TaskTable.lock eq rq.lock.asString())
        }
        DbTaskResponse.success(it)
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse =
        transactionWrapper({
            val res = TaskTable.select {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != MrosUserId.NONE) {
                        add(TaskTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.performSide != MrosPerformSide.NONE) {
                        add(TaskTable.performSide eq rq.performSide)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (TaskTable.title like "%${rq.titleFilter}%")
                                    or (TaskTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbTasksResponse(data = res.map { TaskTable.from(it) }, isSuccess = true)
        }, {
            DbTasksResponse.error(it.asMrosError())
        })
}