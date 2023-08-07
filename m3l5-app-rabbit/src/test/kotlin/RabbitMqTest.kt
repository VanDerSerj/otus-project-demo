import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.api.models.*
import ru.otus.otuskotlin.mrosystem.api.apiMapper
import ru.otus.otuskotlin.mrosystem.api.apiRequestSerialize
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitConfig.Companion.RABBIT_PASSWORD
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitConfig.Companion.RABBIT_USER
import ru.otus.otuskotlin.mrosystem.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.mrosystem.app.rabbit.controller.RabbitController
import ru.otus.otuskotlin.mrosystem.app.rabbit.processor.RabbitDirectProcessor
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RabbitMqTest {

    companion object {
        const val EXCHANGE_TYPE = "direct"
        const val TRANSPORT_EXCHANGE = "transport-exchange"
    }

    val container =
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser(RABBIT_USER, RABBIT_PASSWORD)
        }
    val config by lazy {
        RabbitConfig(
            port = container.getMappedPort(5672),
            host = container.host
        )
    }
    val processorConfig = RabbitExchangeConfiguration(
        keyIn = "in",
        keyOut = "out",
        exchange = TRANSPORT_EXCHANGE,
        queueIn = "queue",
        queueOut = "queue-out",
        consumerTag = "consumer",
        exchangeType = EXCHANGE_TYPE
    )
    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = processorConfig
        )
    }


    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }

    @BeforeTest
    fun tearUp() {
        container.start()
        println("container started")
        GlobalScope.launch(Dispatchers.IO) {
            controller.start()
        }
        Thread.sleep(6000)
        // await when controller starts producers
        println("controller initiated")
    }

    @Test
    fun taskCreateTest() {
        println("start test")
        val processorConfig = processor.processorConfig
        val keyIn = processorConfig.keyIn

        val connection1 = ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection()
        connection1.createChannel().use { channel ->
            var responseJson = ""
            channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
            val queueOut = channel.queueDeclare().queue
            channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
            val deliverCallback = DeliverCallback { consumerTag, delivery ->
                responseJson = String(delivery.body, Charsets.UTF_8)
                println(" [x] Received by $consumerTag: '$responseJson'")
            }
            channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

            channel.basicPublish(processorConfig.exchange, keyIn, null, apiMapper.writeValueAsBytes(boltCreate))

            Thread.sleep(3000)
            // waiting for message processing
            println("RESPONSE: $responseJson")
            val response = apiMapper.readValue(responseJson, TaskCreateResponse::class.java)
            val expected = MrosTaskStub.get()

            assertEquals(expected.title, response.task?.title)
            assertEquals(expected.description, response.task?.description)
        }
    }

    private val boltCreate = with(MrosTaskStub.get()) {
        TaskCreateRequest(
            task = TaskCreateObject(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS
            )
        )
    }
}