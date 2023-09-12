plugins {
    kotlin("multiplatform")
}
kotlin {
    jvm {}
    //linuxX64 {}
    //macosX64 {}
    //macosArm64 {}

    sourceSets {
        val logbackVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(project(":m2l6-common"))
                api(project(":m3l1-biz"))

                // v2 api
                //api(project(":ok-marketplace-api-v2-kmp"))
                //api(project(":ok-marketplace-mappers-v2"))

                // biz
                //api(project(":ok-marketplace-biz"))

                // logging
                //api(project(":ok-marketplace-lib-logging-common"))
                //api(project(":ok-marketplace-lib-logging-kermit"))
                //api(project(":ok-marketplace-mappers-log1"))
                //api(project(":ok-marketplace-api-log1"))

                // Stubs
                //implementation(project(":ok-marketplace-stubs"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api("ch.qos.logback:logback-classic:$logbackVersion")

                // transport models
                api(project(":m2l5-api-jackson"))
                api(project(":m2l6-mappers"))

                // logs
                //api(project(":ok-marketplace-lib-logging-logback"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
