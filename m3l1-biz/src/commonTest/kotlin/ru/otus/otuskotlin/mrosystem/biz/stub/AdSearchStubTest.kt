package ru.otus.otuskotlin.mrosystem.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class AdSearchStubTest {

    private val processor = MrosTaskProcessor()
    val filter = MrosTaskFilter(searchString = "bolt")

    @Test
    fun read() = runTest {

        val ctx = MrosContext(
            command = MrosCommand.SEARCH,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.SUCCESS,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.tasksResponse.size > 1)
        val first = ctx.tasksResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (MrosTaskStub.get()) {
            assertEquals(taskType, first.taskType)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.SEARCH,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_ID,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.SEARCH,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.DB_ERROR,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.SEARCH,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_TITLE,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}