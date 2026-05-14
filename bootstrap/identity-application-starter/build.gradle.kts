plugins {
    id("capstone.spring.autoconfigure")
    id("capstone.base.library")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":identity:identity-core"))

    testImplementation(libs.spring.boot.starter.test)
}
