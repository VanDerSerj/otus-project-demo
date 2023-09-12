package docker

import fixture.docker.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1", 8080, "spring/docker-compose-spring.yml"
)