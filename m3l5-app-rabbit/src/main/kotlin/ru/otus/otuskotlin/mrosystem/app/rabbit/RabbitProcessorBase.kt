package ru.otus.otuskotlin.mrosystem.app.rabbit

import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.rabbitLogger
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import kotlin.coroutines.CoroutineContext

abstract class RabbitProcessorBase(
    private val config: RabbitConfig,
    val processorConfig: RabbitExchangeConfiguration
) {
    suspend fun process(dispatcher: CoroutineContext = Dispatchers.IO) {
        rabbitLogger.info("create connection")
        withContext(dispatcher) {
            ConnectionFactory().apply {
                host = config.host
                port = config.port
                username = config.user
                password = config.password
            }.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    val deliveryCallback = channel.getDeliveryCallback()
                    val cancelCallback = getCancelCallback()
                    runBlocking {
                        channel.describeAndListen(deliveryCallback, cancelCallback)
                    }
                }
            }
        }
    }

    /**
     * Обработка поступившего сообщения в deliverCallback
     */
    protected abstract suspend fun Channel.processMessage(message: Delivery, context: MrosContext)

    /**
     * Обработка ошибок
     */

    protected abstract fun Channel.onError(e: Throwable, context: MrosContext)


    /**
     * Callback, который вызывается при доставке сообщения консьюмеру
     */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, message ->
        runBlocking {
            val context = MrosContext().apply {
                timeStart = kotlinx.datetime.Clock.System.now()
            }
        kotlin.runCatching {
            processMessage(message, context)
        }.onFailure {
            onError(it, context)
        }
        }
    }

    /**
     * Callback, вызываемый при отмене консьюмера
     */
    private fun getCancelCallback() = CancelCallback {
        println("[$it] was cancelled")
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback
    ) {
        withContext(Dispatchers.IO) {
            println("start describing")
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            // Объявляем очередь (не сохраняется при перезагрузке сервера; неэксклюзивна - доступна другим соединениям;
            // не удаляется, если не используется)
            queueDeclare(processorConfig.queueIn, false, false, false, null)
            queueDeclare(processorConfig.queueOut, false, false, false, null)
            // связываем обменник с очередью по ключу (сообщения будут поступать в данную очередь с данного обменника при совпадении ключа)
            queueBind(processorConfig.queueIn, processorConfig.exchange, processorConfig.keyIn)
            queueBind(processorConfig.queueOut, processorConfig.exchange, processorConfig.keyOut)
            // запуск консьюмера с автоотправкой подтверждение при получении сообщения
            basicConsume(processorConfig.queueIn, true, processorConfig.consumerTag, deliverCallback, cancelCallback)
            println("finish describing")
            while (isOpen) {
                kotlin.runCatching {
                    delay(100)
                }.onFailure { e ->
                    e.printStackTrace()
                }
            }

            println("Channel for [${processorConfig.consumerTag}] was closed.")
        }


    }
}