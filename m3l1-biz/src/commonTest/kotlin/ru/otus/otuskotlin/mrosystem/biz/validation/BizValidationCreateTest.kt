package ru.otus.otuskotlin.mrosystem.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.models.MrosCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = MrosCommand.CREATE
    private val processor by lazy { MrosTaskProcessor() }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

}