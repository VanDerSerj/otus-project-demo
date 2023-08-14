package ru.otus.otuskotlin.mrosystem.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MrosTaskStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = stub.id,
            title = "abc",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MrosState.FAILING, ctx.state)
    assertEquals("abc", ctx.taskValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MrosState.FAILING, ctx.state)
    assertEquals("abc", ctx.taskValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = stub.id,
            title = "",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MrosState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: MrosCommand, processor: MrosTaskProcessor) = runTest {
    val ctx = MrosContext(
        command = command,
        state = MrosState.NONE,
        workMode = MrosWorkMode.TEST,
        taskRequest = MrosTask(
            id = MrosTaskId("123"),
            title = "!@#$%^&*(),.{}",
            description = "abc",
            taskType = MrosPerformSide.MECHANIC,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MrosState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}