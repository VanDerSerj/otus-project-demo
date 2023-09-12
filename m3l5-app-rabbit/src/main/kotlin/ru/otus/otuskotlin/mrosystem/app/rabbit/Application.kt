package ru.otus.otuskotlin.mrosystem.app.rabbit

import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.mrosystem.app.rabbit.controller.RabbitController
import ru.otus.otuskotlin.mrosystem.app.rabbit.processor.RabbitDirectProcessor
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor

fun main() {
    val config = RabbitConfig()
    val taskProcessor = MrosTaskProcessor()

    val producerConfig = RabbitExchangeConfiguration(
        keyIn = "in",
        keyOut = "out",
        exchange = "transport-exchange",
        queueIn = "queue",
        queueOut= "queue-out",
        consumerTag = "consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = producerConfig,
            processor = taskProcessor
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}