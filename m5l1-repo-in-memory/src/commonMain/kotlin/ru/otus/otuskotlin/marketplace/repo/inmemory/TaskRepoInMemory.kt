package ru.otus.otuskotlin.marketplace.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.marketplace.repo.inmemory.model.TaskEntity
import ru.otus.otuskotlin.mrosystem.common.helpers.errorRepoConcurrency
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TaskRepoInMemory(
    initObjects: List<MrosTask> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : ITaskRepository {

    private val cache = Cache.Builder<String, TaskEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(task: MrosTask) {
        val entity = TaskEntity(task)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
        val key = randomUuid()
        val task = rq.task.copy(id = MrosTaskId(key), lock = MrosTaskLock(randomUuid()))
        val entity = TaskEntity(task)
        cache.put(key, entity)
        return DbTaskResponse(
            data = task,
            isSuccess = true,
        )
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse {
        val key = rq.id.takeIf { it != MrosTaskId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbTaskResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse {
        val key = rq.task.id.takeIf { it != MrosTaskId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.task.lock.takeIf { it != MrosTaskLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newTask = rq.task.copy(lock = MrosTaskLock(randomUuid()))
        val entity = TaskEntity(newTask)
        return mutex.withLock {
            val oldTask = cache.get(key)
            when {
                oldTask == null -> resultErrorNotFound
                oldTask.lock != oldLock -> DbTaskResponse(
                    data = oldTask.toInternal(),
                    isSuccess = false,
                    errors = listOf(errorRepoConcurrency(MrosTaskLock(oldLock), oldTask.lock?.let { MrosTaskLock(it) }))
                )

                else -> {
                    cache.put(key, entity)
                    DbTaskResponse(
                        data = newTask,
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse {
        val key = rq.id.takeIf { it != MrosTaskId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != MrosTaskLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldTask = cache.get(key)
            when {
                oldTask == null -> resultErrorNotFound
                oldTask.lock != oldLock -> DbTaskResponse(
                    data = oldTask.toInternal(),
                    isSuccess = false,
                    errors = listOf(errorRepoConcurrency(MrosTaskLock(oldLock), oldTask.lock?.let { MrosTaskLock(it) }))
                )

                else -> {
                    cache.invalidate(key)
                    DbTaskResponse(
                        data = oldTask.toInternal(),
                        isSuccess = true,
                    )
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MrosUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.performSide.takeIf { it != MrosPerformSide.NONE }?.let {
                    it.name == entry.value.taskType
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbTasksResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbTaskResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MrosError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbTaskResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MrosError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbTaskResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                MrosError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}