import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.models.MrosTask
import ru.otus.otuskotlin.mrosystem.common.models.MrosUserId
import ru.otus.otuskotlin.mrosystem.common.repo.DbTaskFilterRequest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTaskSearchTest {
    abstract val repo: ITaskRepository

    protected open val initializedObjects: List<MrosTask> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchDealSide() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(performSide = MrosPerformSide.MECHANIC))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitTasks("search") {

        val searchOwnerId = MrosUserId("owner-124")
        override val initObjects: List<MrosTask> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", taskType = MrosPerformSide.MECHANIC),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5", taskType = MrosPerformSide.MECHANIC),
        )
    }
}