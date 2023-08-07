plugins {
    kotlin("jvm")
    java
    id ("com.bmuschko.docker-java-application")
}

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val coroutinesVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(":m2l6-common"))

    //api
    implementation(project(":m2l5-api-jackson"))
    implementation(project(":m2l6-mappers"))

    implementation(project(":m3l1-biz"))

    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation(kotlin("test"))
    testImplementation(project(":m3l1-stubs"))
}

docker {
    javaApplication {

        baseImage.set("openjdk:17")
        ports.set(listOf(8080, 5672))
        images.set(setOf("${project.name}:latest"))
        jvmArgs.set(listOf("-XX:+UseContainerSupport"))
    }
}