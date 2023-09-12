package test

import docker.RabbitDockerCompose
import fixture.BaseFunSpec
import fixture.client.RabbitClient

class AccRabbitTest : BaseFunSpec(RabbitDockerCompose, {
    val client = RabbitClient(RabbitDockerCompose)

    testStubApi(client)
})