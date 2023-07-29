package ru.otus.otuskotlin.mrosystem.springapp.service

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext

@Service
class MrosTaskBlockingProcessor {

    private val processor = MrosTaskProcessor()

    fun exec(ctx: MrosContext) = runBlocking { processor.exec(ctx) }
}