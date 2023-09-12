import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskRequest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTaskCreateTest {
    abstract val repo: ITaskRepository

    protected open val lockNew: MrosTaskLock = MrosTaskLock("20000000-0000-0000-0000-000000000002")

    private val createObj = MrosTask(
        title = "create object",
        description = "create object description",
        ownerId = MrosUserId("owner-123"),
        visibility = MrosVisibility.VISIBLE_PUBLIC,
        taskType = MrosPerformSide.MECHANIC,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTask(DbTaskRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: MrosTaskId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.taskType, result.data?.taskType)
        assertNotEquals(MrosTaskId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitTasks("create") {
        override val initObjects: List<MrosTask> = emptyList()
    }
}