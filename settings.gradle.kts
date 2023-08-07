
rootProject.name = "otus-demo-project2"
include("m1l1")

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings
    val pluginShadow: String by settings
    val bmuschkoVersion: String by settings

    plugins{
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-spring-boot-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
        id("com.github.johnrengelman.shadow") version pluginShadow apply false

    }
}
include("m2l2")
include("m2l5-api-jackson")
include("m2l6-common")
include("m2l6-mappers")
include("m3l1-biz")
include("m3l1-stubs")
include("m3l1-app-spring")
include("m3l5-app-rabbit")
