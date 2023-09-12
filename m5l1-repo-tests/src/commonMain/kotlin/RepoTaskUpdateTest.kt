import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskRequest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTaskUpdateTest {
    abstract val repo: ITaskRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = MrosTaskId("ad-repo-update-not-found")
    protected val lockBad = MrosTaskLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MrosTaskLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MrosTask(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = MrosUserId("owner-123"),
            visibility = MrosVisibility.VISIBLE_PUBLIC,
            taskType = MrosPerformSide.MECHANIC,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MrosTask(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MrosUserId("owner-123"),
        visibility = MrosVisibility.VISIBLE_PUBLIC,
        taskType = MrosPerformSide.MECHANIC,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        MrosTask(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = MrosUserId("owner-123"),
            visibility = MrosVisibility.VISIBLE_PUBLIC,
            taskType = MrosPerformSide.MECHANIC,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(reqUpdateSucc.taskType, result.data?.taskType)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitTasks("update") {
        override val initObjects: List<MrosTask> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }

}