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
class AdUpdateStubTest {

    private val processor = MrosTaskProcessor()
    val id = MrosTaskId("777")
    val title = "title 666"
    val description = "desc 666"
    val dealSide = MrosPerformSide.MECHANIC
    val visibility = MrosVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = MrosContext(
            command = MrosCommand.UPDATE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.SUCCESS,
            taskRequest = MrosTask(
                id = id,
                title = title,
                description = description,
                taskType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.taskResponse.id)
        assertEquals(title, ctx.taskResponse.title)
        assertEquals(description, ctx.taskResponse.description)
        assertEquals(dealSide, ctx.taskResponse.taskType)
        assertEquals(visibility, ctx.taskResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.UPDATE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_TITLE,
            taskRequest = MrosTask(
                id = id,
                title = "",
                description = description,
                taskType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.UPDATE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_DESCRIPTION,
            taskRequest = MrosTask(
                id = id,
                title = title,
                description = "",
                taskType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MrosContext(
            command = MrosCommand.UPDATE,
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
            command = MrosCommand.UPDATE,
            state = MrosState.NONE,
            workMode = MrosWorkMode.STUB,
            stubCase = MrosStubs.BAD_SEARCH_STRING,
            taskRequest = MrosTask(
                id = id,
                title = title,
                description = description,
                taskType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MrosTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}