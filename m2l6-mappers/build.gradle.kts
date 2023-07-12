plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":m2l5-api-jackson"))
    implementation(project(":m2l6-common"))

    testImplementation(kotlin("test-junit"))
}