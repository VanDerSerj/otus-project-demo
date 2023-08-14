package ru.otus.otuskotlin.mrosystem.app.rabbit.controller

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.mrosystem.app.rabbit.RabbitProcessorBase
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.rabbitLogger

class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) {
    fun start() = runBlocking {
        rabbitLogger.info("start init processors")
        processors.forEach {
            try {
                launch { it.process() }
            } catch (e: RuntimeException) {
                // логируем, что-то делаем
                e.printStackTrace()
            }

        }
    }

}