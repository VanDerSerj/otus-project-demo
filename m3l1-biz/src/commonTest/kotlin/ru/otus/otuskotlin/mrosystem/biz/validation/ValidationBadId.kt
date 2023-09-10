package ru.otus.otuskotlin.mrosystem.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = MrosTaskId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MrosState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdTrim(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = MrosTaskId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MrosState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = MrosTaskId(""),
            title = "abc",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MrosState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = MrosTaskId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MrosState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}