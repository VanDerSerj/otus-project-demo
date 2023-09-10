package ru.otus.otuskotlin.mrosystem.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosCommand
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskFilter
import ru.otus.otuskotlin.mrosystem.common.models.MrosWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

    private val command = MrosCommand.SEARCH
    private val processor by lazy { MrosTaskProcessor() }

    @Test
    fun correctEmpty() = runTest {
        val ctx = MrosContext(
            command = command,
            state = MrosState.NONE,
            workMode = MrosWorkMode.TEST,
            taskFilterRequest = MrosTaskFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MrosState.FAILING, ctx.state)
    }
}