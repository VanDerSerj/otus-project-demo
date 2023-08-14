package ru.otus.otuskotlin.mrosystem.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.*
import ru.otus.otuskotlin.mrosystem.common.stubs.MrosStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AdDeleteStubTest {

    private val processor = MrosTaskProcessor()
    val id = MrosTaskId("666")

    @Test
    fun delete() = runTest {

        val ctx = MrosContext(
            command = MrosCommand.DELETE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.SUCCESS,
            taskRequest = MrosTask(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = MrosTaskStub.get()
        assertEquals(stub.id, ctx.taskResponse.id)
        assertEquals(stub.title, ctx.taskResponse.title)
        assertEquals(stub.description, ctx.taskResponse.description)
        assertEquals(stub.taskType, ctx.taskResponse.taskType)
        assertEquals(stub.visibility, ctx.taskResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.DELETE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_ID,
            taskRequest = MrosTask(),
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.DELETE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.DB_ERROR,
            taskRequest = MrosTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.DELETE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_TITLE,
            taskRequest = MrosTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}