package ru.otus.otuskotlin.mrosystem.app.rabbit.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.api.models.IRequest
import ru.otus.otuskotlin.mrosystem.api.apiMapper
import ru.otus.otuskotlin.mrosystem.app.rabbit.RabbitProcessorBase
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.helpers.addError
import ru.otus.otuskotlin.mrosystem.common.helpers.asMrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.mappers.fromTransport
import ru.otus.otuskotlin.mrosystem.mappers.toTransportTask

class RabbitDirectProcessor(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: MrosTaskProcessor = MrosTaskProcessor(),

) : RabbitProcessorBase(config, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery, context: MrosContext) {
        apiMapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this).also {
                println("TYPE: ${this::class.simpleName}")
            }
        }
        var response = processor.exec(context).run { context.toTransportTask() }
        apiMapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }


    override fun Channel.onError(e: Throwable, context: MrosContext) {
        e.printStackTrace()
        context.state = MrosState.FAILING
        context.addError(error = arrayOf(e.asMrosError()))
        val response = context.toTransportTask()
        apiMapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }


}