import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.mrosystem.common.models.MrosTask
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskId
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskIdRequest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTaskReadTest {
    abstract val repo: ITaskRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTasks("delete") {
        override val initObjects: List<MrosTask> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MrosTaskId("ad-repo-read-notFound")

    }
}