
rootProject.name = "otus-demo-project2"
include("m1l1")

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    plugins{
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}
include("m2l2")
include("m2l5-api-jackson")
include("m2l6-common")
include("m2l6-mappers")
include("m3l1-biz")
include("m3l1-stubs")
include("m3l1-app-spring")
