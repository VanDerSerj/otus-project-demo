
rootProject.name = "otus-demo-project2"
include("m1l1")

pluginManagement {
    val kotlinVersion: String by settings
    plugins{
        kotlin("jvm") version kotlinVersion apply false
    }
}
