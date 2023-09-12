plugins {
    kotlin("jvm")
}

dependencies {
    val testcontainersVersion: String by project
    val kotestVersion: String by project
    val ktorVersion: String by project
    val coroutinesVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    val rabbitVersion: String by project
    val postgresDriverVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m2l5-api-jackson"))

    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")

    implementation("com.rabbitmq:amqp-client:$rabbitVersion")

    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    testImplementation("io.ktor:ktor-client-okhttp-jvm:$ktorVersion")

    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    testImplementation("org.postgresql:postgresql:$postgresDriverVersion")
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn(":m3l1-app-spring:dockerBuildImage")
        dependsOn(":m3l5-app-rabbit:dockerBuildImage")

    }
}
